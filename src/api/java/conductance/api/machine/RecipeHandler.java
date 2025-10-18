package conductance.api.machine;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.api.IWorkable;
import conductance.api.recipe.DummyMachineRecipe;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;

public class RecipeHandler extends MachineCapability {

	private final @Getter RecipeCapabilityHolder holder;
	private @Nullable MachineTick tick;
	private @Nullable MachineRecipe lastRecipeReal;
	private @Nullable MachineRecipe lastRecipe;
	private @Getter int progressMax = -1;
	private @Getter int progressCurrent = 0;
	private @Getter RecipeHandlerStatus status = RecipeHandlerStatus.IDLE;

	protected RecipeHandler(final String key, final BaseBlockEntity owner, final RecipeCapabilityHolder holder) {
		super(key, owner);
		this.holder = holder;
		this.addChangedListener(owner::syncToClient);
	}

	public RecipeHandler(final BaseBlockEntity owner, final RecipeCapabilityHolder holder) {
		this("recipe", owner, holder);
	}

	@Override
	public void serialize(final ValueOutput output) {
		if (this.lastRecipe != null && !(this.lastRecipe instanceof DummyMachineRecipe)) {
			output.putString("type", this.lastRecipe.getType().getId().toString());
			output.store("data", this.lastRecipe.getType().getRecipeSerializer().codec().codec(), this.lastRecipe);
		}
		if (this.lastRecipeReal != null && !(this.lastRecipeReal instanceof DummyMachineRecipe)) {
			output.putString("realtype", this.lastRecipeReal.getType().getId().toString());
			output.store("realdata", this.lastRecipeReal.getType().getRecipeSerializer().codec().codec(), this.lastRecipeReal);
		}
		output.putInt("cur", this.progressCurrent);
		output.putInt("max", this.progressMax);
		output.store("status", RecipeHandlerStatus.CODEC, this.status);
	}

	@Override
	public void deserialize(final ValueInput input) {
		input.getString("type").ifPresent(typeString -> {
			final MachineRecipeType recipeType = CAPI.regs().recipeTypes().getValue(ResourceLocation.parse(typeString));
			if (recipeType == this.holder.getRecipeType() && recipeType != null) {
				input.read("data", recipeType.getRecipeSerializer().codec().codec()).ifPresent(recipe -> {
					this.lastRecipe = recipe;
				});
			}
		});
		input.getString("realtype").ifPresent(typeString -> {
			final MachineRecipeType recipeType = CAPI.regs().recipeTypes().getValue(ResourceLocation.parse(typeString));
			if (recipeType == this.holder.getRecipeType() && recipeType != null) {
				input.read("realdata", recipeType.getRecipeSerializer().codec().codec()).ifPresent(recipe -> {
					this.lastRecipeReal = recipe;
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
		final @Nullable RecipePair newRecipe = this.findRecipe();
		if (newRecipe == null) {
			this.reset();
		} else {
			this.setupRecipe(newRecipe);
		}
	}

	protected void setStatus(final RecipeHandlerStatus status) {
		if (status != this.status) {
			this.status = status;
			this.updateWorkingState(null);
			this.setChanged();
		}
	}

	private void updateWorkingState(@Nullable final Boolean forcedState) {
		if (this.getOwner() instanceof final IWorkable workable) {
			workable.setWorking(Objects.requireNonNullElse(forcedState, this.status == RecipeHandlerStatus.PROCESSING));
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
		if (this.getOwner().isServerSide()) {
			this.tick = this.getOwner().addTick(this::tick, this.tick);
			this.updateWorkingState(null);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.revalidateTick();
		this.updateWorkingState(null);
	}

	protected @Nullable RecipePair findRecipe() {
		if (this.holder.getRecipeType() == null) {
			return null;
		}
		for (final RecipeHolder<MachineRecipe> candidateHolder : this.holder.getRecipeType().getRecipes()) {
			final MachineRecipe candidate = candidateHolder.value();
			final MachineRecipe recipe = this.holder.getRecipeModifier() == null ? candidate : this.holder.getRecipeModifier().modifyRecipe(candidate);
			if (this.testRecipe(recipe)) {
				return new RecipePair(candidate, recipe);
			}
		}
		return null;
	}

	protected boolean testRecipe(final MachineRecipe recipe) {
		return CAPI.recipeHelper().test(recipe, this.getOwner(), this.holder) && CAPI.recipeHelper().testPerTick(recipe, this.getOwner(), this.holder);
	}

	protected void setupRecipe(final RecipePair recipe) {
		this.lastRecipeReal = recipe.realRecipe();
		this.lastRecipe = recipe.getUsableRecipe();
		this.progressMax = this.lastRecipe.getRecipeDuration();
		this.progressCurrent = 0;
		this.setStatus(RecipeHandlerStatus.PROCESSING);
		CAPI.recipeHelper().handle(this.lastRecipe, IO.IN, this.holder);
		this.setChanged();
	}

	protected void progressRecipe(final MachineRecipe recipe) {
		if (!CAPI.recipeHelper().testPerTick(recipe, this.getOwner(), this.holder)) {
			switch (this.holder.getPerTickFailureAction()) {
				case NOTHING -> {
					if (this.tick != null) {
						this.tick.invalidate();
					}
				}
				case REGRESS -> {
					this.progressCurrent = Math.max(0, this.progressCurrent - 2);
					if (this.progressCurrent == 0 && this.tick != null) {
						this.tick.invalidate();
						this.updateWorkingState(false);
					}
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
				this.setupRecipe(new RecipePair(this.lastRecipeReal, this.lastRecipe));
			} else {
				this.findAndSetupRecipe();
			}
		}
		this.setChanged();
	}
}
