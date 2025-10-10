package conductance.compat.jei;

import conductance.api.machine.gui.ProgressProvider;

final class JeiProgressProvider implements ProgressProvider {

	@Override
	public long getMaxProgress() {
		return 200;
	}

	@Override
	public long getCurrentProgress() {
		return (int) (System.currentTimeMillis() % 10_000) / 50;
	}
}
