package conductance.core.sync;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.Lazy;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.sync.Persisted;
import conductance.api.sync.ReferenceKey;
import conductance.api.sync.SpecialHandled;
import conductance.api.sync.Synchronized;
import conductance.Conductance;

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
	@Getter
	private final Object containingInstance;

	@Setter
	@Nullable
	private Method specialHandlerTestDirtyMethod = null;
	@Setter
	@Nullable
	private Method specialHandlerSerializeMethod = null;
	@Setter
	@Nullable
	private Method specialHandlerDeserializeMethod = null;

	private final Lazy<String> persistenceKey;
	private final Lazy<String> syncKey;

	private ReferenceKeyImpl(final String name, final Field rawField, final Type rawType, final Object containingInstance) {
		rawField.setAccessible(true);
		this.name = name;
		this.hasSpecialHandling = rawField.isAnnotationPresent(SpecialHandled.class);
		this.isPersisted = rawField.isAnnotationPresent(Persisted.class);
		this.isSynchronized = rawField.isAnnotationPresent(Synchronized.class);
		this.rawField = rawField;
		this.rawType = rawType;
		this.containingInstance = containingInstance;
		this.persistenceKey = Lazy.of(() -> this.isPersisted ? ReferenceKeyImpl.getNotEmptyOrFallback(rawField.getAnnotation(Persisted.class).key(), name) : null);
		this.syncKey = Lazy.of(() -> this.isSynchronized ? ReferenceKeyImpl.getNotEmptyOrFallback(rawField.getAnnotation(Synchronized.class).key(), name) : null);
	}

	public boolean specialTestDirty(final Object value) {
		try {
			assert this.specialHandlerTestDirtyMethod != null;
			return (boolean) this.specialHandlerTestDirtyMethod.invoke(this.containingInstance, value);
		} catch (final InvocationTargetException | IllegalAccessException e) {
			Conductance.LOGGER.error("An error occurred while calling field dirty test method", e);
			throw new RuntimeException(e);
		}
	}

	public CompoundTag specialSerialize(final Object value) {
		try {
			assert this.specialHandlerSerializeMethod != null;
			return (CompoundTag) this.specialHandlerSerializeMethod.invoke(this.containingInstance, value);
		} catch (final InvocationTargetException | IllegalAccessException e) {
			Conductance.LOGGER.error("An error occurred while calling field serialize method", e);
			throw new RuntimeException(e);
		}
	}

	public Object specialDeserialize(final CompoundTag nbt) {
		try {
			assert this.specialHandlerDeserializeMethod != null;
			return this.specialHandlerDeserializeMethod.invoke(this.containingInstance, nbt);
		} catch (final InvocationTargetException | IllegalAccessException e) {
			Conductance.LOGGER.error("An error occurred while calling field deserialize method", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getPersistenceKey() {
		return this.persistenceKey.get();
	}

	@Override
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

	public static ReferenceKeyImpl of(final Field field, final Object containingInstance) {
		return new ReferenceKeyImpl(field.getName(), field, field.getGenericType(), containingInstance);
	}
}
