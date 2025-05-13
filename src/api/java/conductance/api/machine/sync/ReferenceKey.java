package conductance.api.machine.sync;

import org.jetbrains.annotations.Nullable;

public interface ReferenceKey {

	@Nullable String getPersistenceKey();

	@Nullable String getSyncKey();

	String getName();

	boolean isPersisted();

	boolean isSynchronized();

	java.lang.reflect.Field getRawField();

	java.lang.reflect.Type getRawType();
}
