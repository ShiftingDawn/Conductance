package conductance.api.sync;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import org.jetbrains.annotations.Nullable;

public interface ReferenceKey {

	@Nullable
	String getPersistenceKey();

	@Nullable
	String getSyncKey();

	String getName();

	boolean hasSpecialHandling();

	boolean isPersisted();

	boolean isSynchronized();

	Field getRawField();

	Type getRawType();
}
