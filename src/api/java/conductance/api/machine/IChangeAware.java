package conductance.api.machine;

import org.jetbrains.annotations.Nullable;

public interface IChangeAware {

	void setChangeListener(@Nullable Runnable listener);

	@Nullable
	Runnable getChangeListener();

	default void addChangeListener(final Runnable runnable) {
		final Runnable current = this.getChangeListener();
		if (current == null) {
			this.setChangeListener(runnable);
		} else {
			this.setChangeListener(() -> {
				current.run();
				runnable.run();
			});
		}
	}
}
