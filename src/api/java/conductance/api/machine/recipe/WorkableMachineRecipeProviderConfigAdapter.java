package conductance.api.machine.recipe;

import conductance.api.machine.IWorkable;

public interface WorkableMachineRecipeProviderConfigAdapter extends MachineRecipeProviderConfigAdapter, IWorkable {

	@Override
	default boolean canWork() {
		return !this.getRecipeProcessor().isPaused();
	}

	@Override
	default void setCanWork(final boolean canWork) {
		this.getRecipeProcessor().setPaused(canWork);
	}

	@Override
	default int getProgress() {
		return this.getRecipeProcessor().getProgress();
	}

	@Override
	default int getProgressMax() {
		return this.getRecipeProcessor().getProgressMax();
	}

	@Override
	default boolean isWorking() {
		return this.getRecipeProcessor().isActive();
	}
}
