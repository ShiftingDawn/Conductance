package conductance.api.machine;

import lombok.RequiredArgsConstructor;
import conductance.api.machine.gui.ProgressProvider;

@RequiredArgsConstructor
public final class RecipeHandlerProgressProvider implements ProgressProvider {

	private final RecipeHandler handler;

	@Override
	public int getMaxProgress() {
		return this.handler.getProgressMax();
	}

	@Override
	public int getCurrentProgress() {
		return this.handler.getProgressCurrent();
	}
}
