package conductance.core.sync;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import net.neoforged.neoforge.common.util.Lazy;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.ReferenceKey;
import conductance.api.machine.sync.SpecialHandled;
import conductance.api.machine.sync.Synchronized;

final class ReferenceKeyImpl implements ReferenceKey {

	@Getter
	private final String name;
	private final boolean hasSpecialHandling;
	@Getter
	private final boolean isPersisted;
	@Getter
	private final boolean isSynchronized;
	@Getter
	private final Field rawField;
	@Getter
	private final Type rawType;

	@Setter
	@Getter
	@Nullable
	private Method specialHandlerTestDirtyMethod = null;
	@Setter
	@Getter
	@Nullable
	private Method specialHandlerSerializeMethod = null;
	@Setter
	@Getter
	@Nullable
	private Method specialHandlerDeserializeMethod = null;

	private final Lazy<String> persistenceKey;
	private final Lazy<String> syncKey;

	private ReferenceKeyImpl(final String name, final Field rawField, final Type rawType) {
		rawField.setAccessible(true);
		this.name = name;
		this.hasSpecialHandling = rawField.isAnnotationPresent(SpecialHandled.class);
		this.isPersisted = rawField.isAnnotationPresent(Persisted.class);
		this.isSynchronized = rawField.isAnnotationPresent(Synchronized.class);
		this.rawField = rawField;
		this.rawType = rawType;
		this.persistenceKey = Lazy.of(() -> this.isPersisted ? ReferenceKeyImpl.getNotEmptyOrFallback(rawField.getAnnotation(Persisted.class).key(), name) : null);
		this.syncKey = Lazy.of(() -> this.isSynchronized ? ReferenceKeyImpl.getNotEmptyOrFallback(rawField.getAnnotation(Synchronized.class).key(), name) : null);
	}

	@Override
	@Nullable
	public String getPersistenceKey() {
		return this.persistenceKey.get();
	}

	@Override
	@Nullable
	public String getSyncKey() {
		return this.syncKey.get();
	}

	@Override
	public boolean hasSpecialHandling() {
		return this.hasSpecialHandling;
	}

	private static String getNotEmptyOrFallback(@Nullable final String str, final String fallback) {
		return str == null || str.isBlank() ? fallback : str.trim();
	}

	public static ReferenceKeyImpl of(final Field field) {
		return new ReferenceKeyImpl(field.getName(), field, field.getGenericType());
	}
}
