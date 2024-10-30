package conductance.core.data;

import java.lang.reflect.Field;
import com.google.common.base.Strings;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.Persisted;
import conductance.api.machine.data.Synchronized;

public final class ManagedFieldWrapper {

	@Getter
	private final Field field;
	@Getter
	private final Class<?> fieldType;
	@Getter
	private final String name;
	@Getter
	private final boolean persisted;
	@Getter
	private final boolean synced;
	@Nullable
	@Getter
	private final String persistenceKey;

	private ManagedFieldWrapper(final Field field, final boolean persisted, final boolean synced) {
		this.field = field;
		this.fieldType = field.getType();
		this.name = field.getName();
		this.persisted = persisted;
		this.synced = synced;
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

	public String getSyncKey() {
		return this.name;
	}

	@Nullable
	static ManagedFieldWrapper create(final Field field) {
		final boolean persisted = field.isAnnotationPresent(Persisted.class);
		final boolean synced = field.isAnnotationPresent(Synchronized.class);
		if (persisted || synced) {
			return new ManagedFieldWrapper(field, persisted, synced);
		}
		return null;
	}
}
