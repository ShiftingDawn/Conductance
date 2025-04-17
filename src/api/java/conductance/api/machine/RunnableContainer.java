package conductance.api.machine;

import org.jetbrains.annotations.Nullable;

public interface RunnableContainer {

	@Nullable
	MachineRunnable addTick(Runnable action);

	@Nullable
	default MachineRunnable addTick(@Nullable final MachineRunnable previous, final Runnable action) {
		if (previous == null || !previous.isValid()) {
			return this.addTick(action);
		}
		return previous;
	}
}
