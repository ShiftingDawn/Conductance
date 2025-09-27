package conductance.api.machine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MachineTick {

	private final Runnable action;
	private @Getter boolean valid = true;

	public void tick() {
		if (this.valid) {
			this.action.run();
		}
	}

	public void invalidate() {
		this.valid = false;
	}
}
