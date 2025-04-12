package conductance.api.machine;

import lombok.Getter;

public final class MachineRunnable {

	private final Runnable action;
	@Getter
	private boolean valid = true;

	public MachineRunnable(final Runnable action) {
		this.action = action;
	}

	public void tick() {
		if (this.valid) {
			this.action.run();
		}
	}

	public void invalidate() {
		this.valid = false;
	}
}
