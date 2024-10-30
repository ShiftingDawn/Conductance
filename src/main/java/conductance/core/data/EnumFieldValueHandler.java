package conductance.core.data;

import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

public class EnumFieldValueHandler implements ManagedFieldValueHandler<Enum<?>> {

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isEnum();
	}

	@Override
	public boolean equals(final Enum<?> value1, final Enum<?> value2) {
		return value1 == value2;
	}

	@Override
	public @Nullable Enum<?> readFromField(final Field field, final Object instance) {
		try {
			return (Enum<?>) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeToField(final Field field, final Object instance, @Nullable final Enum<?> value) {
		try {
			field.set(instance, value);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
