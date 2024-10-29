package conductance.api.machine;

import lombok.Getter;

public final class MetaTick {

	private final Runnable action;
	@Getter
	private boolean valid = true;

	public MetaTick(final Runnable action) {
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
