package conductance.core.data;

import java.lang.reflect.Field;
import com.google.common.base.Strings;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.Persisted;

public final class ManagedFieldWrapper {

	@Getter
	private final Field field;
	@Getter
	private final String name;
	@Getter
	private final boolean persisted;
	@Nullable
	@Getter
	private final String persistenceKey;

	private ManagedFieldWrapper(final Field field) {
		this.field = field;
		this.name = field.getName();
		this.persisted = field.isAnnotationPresent(Persisted.class);
		this.persistenceKey = this.makePersistenceKey(field);
	}

	@Nullable
	private String makePersistenceKey(final Field reflectField) {
		if (this.persisted) {
			final Persisted values = reflectField.getAnnotation(Persisted.class);
			return !Strings.isNullOrEmpty(values.value()) ? values.value() : reflectField.getName();
		}
		return null;
	}

	@Nullable
	static ManagedFieldWrapper create(final Field field) {

	}
}
