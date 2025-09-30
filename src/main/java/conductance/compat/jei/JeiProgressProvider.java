package conductance.compat.jei;

import conductance.api.machine.gui.ProgressProvider;

final class JeiProgressProvider implements ProgressProvider {

	@Override
	public int getMaxProgress() {
		return 0;
	}

	@Override
	public int getCurrentProgress() {
		return 0;
	}
}
