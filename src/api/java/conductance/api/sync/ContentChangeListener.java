package conductance.api.sync;

import org.jetbrains.annotations.Nullable;

public interface ContentChangeListener {

	void setContentChangeListener(@Nullable Runnable callback);

	@Nullable
	Runnable getContentChangeListener();

	default void addContentChangeListener(final Runnable listener) {
		final Runnable current = this.getContentChangeListener();
		if (current == null) {
			this.setContentChangeListener(listener);
		} else {
			this.setContentChangeListener(() -> {
				current.run();
				listener.run();
			});
		}
	}
}
