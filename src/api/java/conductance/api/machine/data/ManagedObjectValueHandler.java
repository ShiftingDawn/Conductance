package conductance.api.machine.data;

import java.lang.reflect.Field;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public abstract class ManagedObjectValueHandler<T> implements ManagedFieldValueHandler<T> {

	private final Class<T> typeClass;
	private final boolean shallowHandlerCheck;

	protected ManagedObjectValueHandler(final Class<T> typeClass, final boolean shallowHandlerCheck) {
		this.typeClass = typeClass;
		this.shallowHandlerCheck = shallowHandlerCheck;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return this.shallowHandlerCheck ? type == this.typeClass : this.typeClass.isAssignableFrom(type);
	}

	@Override
	public boolean equals(final T value1, final T value2) {
		return Objects.equals(value1, value2);
	}

	@SuppressWarnings("unchecked")
	@Override
	public @Nullable T readFromField(final Field field, final Object instance) {
		try {
			return (T) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeToField(final Field field, final Object instance, @Nullable final T value) {
		try {
			field.set(instance, value);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
