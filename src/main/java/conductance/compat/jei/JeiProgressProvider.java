package conductance.compat.jei;

import conductance.api.machine.gui.ProgressProvider;

final class JeiProgressProvider implements ProgressProvider {

	@Override
	public int getMaxProgress() {
		return 200;
	}

	@Override
	public int getCurrentProgress() {
		return (int) (System.currentTimeMillis() % 10_000) / 50;
	}
}
