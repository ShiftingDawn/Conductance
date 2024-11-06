package nl.appelgebakje22.xdata.ref;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import lombok.Getter;
import nl.appelgebakje22.xdata.Lazy;
import nl.appelgebakje22.xdata.api.Persisted;
import nl.appelgebakje22.xdata.api.Synchronized;
import org.jetbrains.annotations.Nullable;

public class ReferenceKey {

	@Getter
	private final String name;
	@Getter
	private final boolean isPersisted;
	@Getter
	private final boolean isSynchronized;
	@Getter
	private final Field rawField;
	@Getter
	private final Type rawType;

	private final Lazy<String> persistenceKey;
	private final Lazy<String> syncKey;

	private ReferenceKey(final String name, final Field rawField, final Type rawType) {
		rawField.setAccessible(true);
		this.name = name;
		this.isPersisted = rawField.isAnnotationPresent(Persisted.class);
		this.isSynchronized = rawField.isAnnotationPresent(Synchronized.class);
		this.rawField = rawField;
		this.rawType = rawType;
		this.persistenceKey = Lazy.of(() -> this.isPersisted ? ReferenceKey.getNotEmptyOrFallback(rawField.getAnnotation(Persisted.class).key(), name) : null);
		this.syncKey = Lazy.of(() -> this.isSynchronized ? ReferenceKey.getNotEmptyOrFallback(rawField.getAnnotation(Synchronized.class).key(), name) : null);
	}

	@Nullable
	public String getPersistenceKey() {
		return this.persistenceKey.get();
	}

	@Nullable
	public String getSyncKey() {
		return this.syncKey.get();
	}

	private static String getNotEmptyOrFallback(@Nullable final String str, final String fallback) {
		return str == null || str.isBlank() ? fallback : str.trim();
	}

	public static ReferenceKey of(final Field field) {
		return new ReferenceKey(field.getName(), field, field.getGenericType());
	}
}
