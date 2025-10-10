package conductance.api.machine.gui;

import lombok.RequiredArgsConstructor;
import conductance.api.machine.RecipeHandler;

@RequiredArgsConstructor
public final class RecipeHandlerProgressProvider implements ProgressProvider {

	private final RecipeHandler handler;

	@Override
	public long getMaxProgress() {
		return this.handler.getProgressMax();
	}

	@Override
	public long getCurrentProgress() {
		return this.handler.getProgressCurrent();
	}
}
