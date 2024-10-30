package conductance.api.machine;

import java.lang.reflect.Field;
import java.util.Objects;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

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

	@SuppressWarnings("unchecked")
	@Override
	public @Nullable T getValue(final Field field, final Object instance) {
		try {
			return (T) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SneakyThrows
	@Override
	public void setValue(final Field field, final Object instance, @Nullable final T value) {
		field.set(instance, value);
	}

	@Override
	public boolean equals(final T value1, final T value2) {
		return Objects.equals(value1, value2);
	}
}
