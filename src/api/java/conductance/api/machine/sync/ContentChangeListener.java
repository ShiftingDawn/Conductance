package conductance.api.machine.sync;

import org.jetbrains.annotations.Nullable;

public interface ContentChangeListener {

	void setContentChangeListener(@Nullable Runnable callback);

	@Nullable
	Runnable getContentChangeListener();
}
