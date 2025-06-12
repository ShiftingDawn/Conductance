package conductance.api.sync;

import org.jetbrains.annotations.Nullable;

public interface ReferenceSynchronizedListener<T> {

	void onReferenceSynchronized(@Nullable T oldValue, @Nullable T newValue);
}
