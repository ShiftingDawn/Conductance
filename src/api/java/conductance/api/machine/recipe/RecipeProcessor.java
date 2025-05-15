package conductance.api.machine.recipe;

import java.util.List;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineRunnable;
import conductance.api.machine.capability.MachineCapability;
import conductance.api.machine.sync.OnSynchronized;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.Synchronized;
import conductance.api.util.IOMode;

public class RecipeProcessor extends MachineCapability {

	public enum State {
		IDLE, WORKING, WAITING, PAUSED
	}

	private final MachineRecipeProviderConfigAdapter config;
	private final RecipeCapabilityHolder capabilityHolder;
	@Getter
	@Persisted
	@Synchronized
	@OnSynchronized(method = "onStateChanged")
	private State state = State.IDLE;
	@Nullable
	@Getter
	@Persisted
	protected IRecipe currentRecipe;
	@Nullable
	@Getter
	@Persisted
	protected IRecipe lastRecipeReal;
	@Nullable
	@Getter
	@Persisted
	protected IRecipe lastRecipe;
	@Persisted
	@Getter
	protected int progress;
	@Getter
	@Persisted
	protected int progressMax;
	@Getter
	@Persisted
	protected int fuelTime;
	@Getter
	@Persisted
	protected int fuelTimeMax;
	@Getter
	protected long timeStamp;
	protected boolean isDirty;
	@Persisted
	@Getter
	protected long totalContinuousRunningTime;
	@Nullable
	protected MachineRunnable tickable;

	public RecipeProcessor(final MachineBlockEntity<?> machine, final MachineRecipeProviderConfigAdapter config, final RecipeCapabilityHolder capabilityHolder) {
		super(machine);
		this.config = config;
		this.capabilityHolder = capabilityHolder;
	}

	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unused")
	protected void onStateChanged(final State newValue, final State oldValue) {
		this.getMachineBlockEntity().scheduleRenderUpdate();
		//TODO sounds
		//updateSound();
	}

	public void tick() {
		if (!this.isPaused()) {
			if (!this.isIdle() && this.lastRecipe != null) {
				if (this.isWaiting() && this.getMachineBlockEntity().getTimerOffset() % 5 == 0) {
					this.tickRecipe();
				} else {
					if (this.progress < this.progressMax) {
						this.tickRecipe();
					}
					if (this.progress >= this.progressMax) {
						this.completeRecipe();
					}
				}
			} else if (this.lastRecipe != null) {
				this.findNewRecipe();
			} else if (!this.config.keepTickables() || this.getMachineBlockEntity().getTimerOffset() % 5 == 0) {
				this.findNewRecipe();
			}
		}
		if (this.fuelTime > 0) {
			--this.fuelTime;
		} else if (this.isPaused() || (this.lastRecipe == null && this.isIdle() && !this.config.keepTickables() && !this.isDirty)) {
			if (this.tickable != null) {
				this.tickable.invalidate();
				this.tickable = null;
			}
		}
	}

	public void tickRecipe() {
		assert this.lastRecipe != null;
		if (this.handleRecipeFuel(this.lastRecipe)) {
			if (this.testPerTick(this.lastRecipe)) {
				this.handlePerTick(this.lastRecipe, IOMode.INPUT);
				this.handlePerTick(this.lastRecipe, IOMode.OUTPUT);
				this.config.onWorking();
				this.setState(State.WORKING);
				++this.progress;
				++this.totalContinuousRunningTime;
			} else {
				this.setWaiting();
			}
		}
	}

	public void completeRecipe() {
		this.config.afterWorking();
		if (this.lastRecipe != null) {
			this.handle(this.lastRecipe, IOMode.OUTPUT);
			if (this.test(this.lastRecipe) && this.testPerTick(this.lastRecipe)) {
				this.processFoundRecipe(this.lastRecipe);
			} else {
				this.setState(State.IDLE);
				this.progress = 0;
				this.progressMax = 0;
			}
		}
	}

	public void findNewRecipe() {
		if (!this.isDirty && this.lastRecipe != null && this.test(this.lastRecipe) && this.testPerTick(this.lastRecipe)) {
			final IRecipe recipe = this.lastRecipe;
			this.lastRecipe = null;
			this.lastRecipeReal = null;
			this.processFoundRecipe(recipe);
		} else {
			this.lastRecipe = null;
			this.lastRecipeReal = null;
			this.findAllRecipes().stream().findFirst().ifPresent(this::processFoundRecipe);
		}
	}

	public void processFoundRecipe(final IRecipe recipe) {
		if (this.handleRecipeFuel(recipe)) {
			this.config.beforeWorking();
			this.handle(recipe, IOMode.INPUT);
			this.handlePerTick(recipe, IOMode.INPUT);
			this.isDirty = false;
			this.lastRecipeReal = recipe;
			this.lastRecipe = this.getMachineBlockEntity().getMachineType().getRecipeModifier().apply(this.getMachineBlockEntity(), this.lastRecipeReal);
			assert this.lastRecipe != null;
			this.setState(State.WORKING);
			this.progress = 0;
			this.progressMax = this.lastRecipe.getProcessTime();
		}
	}

	private boolean handleRecipeFuel(final IRecipe recipe) {
		//TODO handle fuelles recipes
		return true;//this.fuelTime > 0;
	}

	public void markDirty() {
		this.isDirty = true;
	}

	public void reset() {
		this.isDirty = false;
		this.lastRecipe = null;
		this.lastRecipeReal = null;
		this.progress = 0;
		this.progressMax = 0;
		this.fuelTime = 0;
		this.state = State.IDLE;
		this.updateTickable();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.updateTickable();
	}

	public void updateTickable() {
		if ((this.isPaused() && this.fuelTime == 0) || !this.config.isProcessingAvailable()) {
			if (this.tickable != null) {
				this.tickable.invalidate();
				this.tickable = null;
			}
		} else {
			this.tickable = this.getMachineBlockEntity().addTick(this.tickable, this::tick);
		}
	}

	public void setState(final State newState) {
		if (this.state != newState) {
			if (this.state == State.WORKING) {
				this.totalContinuousRunningTime = 0;
			}
			this.getMachineBlockEntity().onStateChanged(this.state, newState);
			this.state = newState;
			this.updateTickable();
		}
	}

	public void setPaused(final boolean isPaused) {
		if (isPaused) {
			this.setState(State.PAUSED);
		} else if (this.lastRecipe != null && this.progressMax > 0) {
			this.setState(State.WORKING);
		} else {
			this.setState(State.IDLE);
		}
	}

	public void setWaiting() {
		this.setState(State.WAITING);
		this.config.onWaiting();
	}

	public boolean isIdle() {
		return this.state == State.IDLE;
	}

	public boolean isWaiting() {
		return this.state == State.WAITING;
	}

	public boolean isPaused() {
		return this.state == State.PAUSED;
	}

	public boolean isWorking() {
		return this.state == State.WORKING;
	}

	public boolean isActive() {
		return this.isWorking() || this.isWaiting() || (this.isPaused() && this.lastRecipe != null && this.progressMax > 0);
	}

	public double getProgressPercentage() {
		return this.progressMax == 0 ? 0 : this.progress / (this.progressMax * 1.0);
	}

	private List<IRecipe> findAllRecipes() {
		return CAPI.recipeHelper().findRecipes(this.config.getRecipeType(), this.getRecipeManager(), this.capabilityHolder, this.config.getOutputLimits());
	}

	public final RecipeManager getRecipeManager() {
		return this.getMachineBlockEntity().getLevel().getRecipeManager();
	}

	public final boolean test(final IRecipe recipe) {
		return CAPI.recipeHelper().test(recipe, this.capabilityHolder);
	}

	public final boolean testPerTick(final IRecipe recipe) {
		return CAPI.recipeHelper().testPerTick(recipe, this.capabilityHolder);
	}

	public final boolean handle(final IRecipe recipe, final IOMode ioMode) {
		return CAPI.recipeHelper().handle(recipe, ioMode, this.capabilityHolder);
	}

	public final boolean handlePerTick(final IRecipe recipe, final IOMode ioMode) {
		return CAPI.recipeHelper().handlePerTick(recipe, ioMode, this.capabilityHolder);
	}
}
