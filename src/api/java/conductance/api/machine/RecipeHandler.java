package conductance.api.machine;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.recipe.DummyMachineRecipe;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;

public class RecipeHandler extends MachineCapability {

	private final @Getter RecipeCapabilityHolder holder;
	private @Nullable MachineTick tick;
	private @Nullable MachineRecipe lastRecipe;
	private @Getter int progressMax = -1;
	private @Getter int progressCurrent = 0;
	private RecipeHandlerStatus status = RecipeHandlerStatus.IDLE;

	protected RecipeHandler(final String key, final MachineBlockEntity<?> machine, final RecipeCapabilityHolder holder) {
		super(key, machine);
		this.holder = holder;
		this.addChangedListener(machine::syncToClient);
	}

	public RecipeHandler(final MachineBlockEntity<?> machine, final RecipeCapabilityHolder holder) {
		this("recipe", machine, holder);
	}

	@Override
	public void serialize(final ValueOutput output) {
		if (this.lastRecipe != null && !(this.lastRecipe instanceof DummyMachineRecipe)) {
			output.putString("type", this.lastRecipe.getType().getId().toString());
			output.store("data", this.lastRecipe.getType().getRecipeSerializer().codec().codec(), this.lastRecipe);
		}
		output.putInt("cur", this.progressCurrent);
		output.putInt("max", this.progressMax);
		output.store("status", RecipeHandlerStatus.CODEC, this.status);
	}

	@Override
	public void deserialize(final ValueInput input) {
		input.getString("type").ifPresent(typeString -> {
			final MachineRecipeType recipeType = CAPI.regs().recipeTypes().getValue(ResourceLocation.parse(typeString));
			if (recipeType == this.holder.getRecipeType()) {
				input.read("data", recipeType.getRecipeSerializer().codec().codec()).ifPresent(recipe -> {
					this.lastRecipe = recipe;
				});
			}
		});
		this.progressCurrent = input.getIntOr("cur", 0);
		this.progressMax = input.getIntOr("max", -1);
		this.setStatus(input.read("status", RecipeHandlerStatus.CODEC).orElse(RecipeHandlerStatus.IDLE));
	}

	public void tick() {
		if (this.status == RecipeHandlerStatus.IDLE) {
			this.findAndSetupRecipe();
		}
		if (this.status == RecipeHandlerStatus.PROCESSING) {
			if (this.lastRecipe != null) {
				this.progressRecipe(this.lastRecipe);
			} else {
				this.setStatus(RecipeHandlerStatus.IDLE);
			}
		}
	}

	protected void findAndSetupRecipe() {
		final MachineRecipe newRecipe = this.findRecipe();
		if (newRecipe == null) {
			this.reset();
		} else {
			this.setupRecipe(newRecipe);
		}
	}

	protected void setStatus(final RecipeHandlerStatus status) {
		if (status != this.status) {
			this.status = status;
			this.getMachine().setWorkingState(this.status == RecipeHandlerStatus.PROCESSING);
			this.setChanged();
		}
	}

	protected void reset() {
		this.setStatus(RecipeHandlerStatus.IDLE);
		this.progressMax = -1;
		this.progressCurrent = 0;
		this.lastRecipe = null;
		if (this.tick != null) {
			this.tick.invalidate();
		}
		this.setChanged();
	}

	public void revalidateTick() {
		if (this.getMachine().isServerSide()) {
			this.tick = this.getMachine().addTick(this::tick, this.tick);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.revalidateTick();
	}

	protected @Nullable MachineRecipe findRecipe() {
		//TODO cache recipes for the machine's recipetype
		final ServerLevel level = (ServerLevel) this.getMachine().getLevel();
		assert level != null;
		final List<MachineRecipe> allRecipes = level.recipeAccess().getRecipes().stream()
			.filter(recipeHolder -> recipeHolder.value().getType() == this.holder.getRecipeType())
			.map(recipeHolder -> (MachineRecipe) recipeHolder.value()).toList();
		for (final MachineRecipe candidate : allRecipes) {
			if (this.testRecipe(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	protected boolean testRecipe(final MachineRecipe recipe) {
		return CAPI.recipeHelper().test(recipe, this.holder) && CAPI.recipeHelper().testPerTick(recipe, this.holder);
	}

	protected void setupRecipe(final MachineRecipe recipe) {
		this.lastRecipe = recipe;
		this.progressMax = recipe.getRecipeDuration();
		this.progressCurrent = 0;
		this.setStatus(RecipeHandlerStatus.PROCESSING);
		CAPI.recipeHelper().handle(recipe, IO.IN, this.holder);
		CAPI.recipeHelper().handlePerTick(recipe, IO.IN, this.holder);
		CAPI.recipeHelper().handlePerTick(recipe, IO.OUT, this.holder);
		this.setChanged();
	}

	protected void progressRecipe(final MachineRecipe recipe) {
		if (!CAPI.recipeHelper().testPerTick(recipe, this.holder)) {
			switch (this.holder.getPerTickFailureAction()) {
				case NOTHING -> {
					if (this.tick != null) {
						this.tick.invalidate();
					}
				}
				case REGRESS -> {
					this.progressCurrent = Math.min(0, this.progressCurrent - 2);
					this.setChanged();
				}
				case VOID -> {
					this.reset();
					this.setChanged();
				}
			}
			return;
		}
		CAPI.recipeHelper().handlePerTick(recipe, IO.IN, this.holder);
		CAPI.recipeHelper().handlePerTick(recipe, IO.OUT, this.holder);
		++this.progressCurrent;
		if (this.progressCurrent >= this.progressMax) {
			CAPI.recipeHelper().handle(recipe, IO.OUT, this.holder);
			if (this.testRecipe(recipe)) {
				this.setupRecipe(recipe);
			} else {
				this.findAndSetupRecipe();
			}
		}
		this.setChanged();
	}
}
