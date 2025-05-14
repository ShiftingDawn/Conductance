package conductance.api.machine.sync;

import org.jetbrains.annotations.Nullable;

public interface ReferenceSynchronizedListener<T> {

	void onReferenceSynchronized(@Nullable T oldValue, @Nullable T newValue);
}
