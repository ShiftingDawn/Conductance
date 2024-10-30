package conductance.core.data;

import java.lang.reflect.Field;
import java.util.Objects;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

final class NbtSerializableValueHandler implements ManagedFieldValueHandler<Object> {

	@Override
	public boolean canHandle(final Class<?> type) {
		return INBTSerializable.class.isAssignableFrom(type);
	}

	@Override
	public boolean equals(final INBTSerializable<?> value1, final INBTSerializable<?> value2) {
		return Objects.equals(value1, value2);
	}

	@Override
	public @Nullable Object readFromField(final Field field, final Object instance) {
		try {
			field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeToField(final Field field, final Object instance, @Nullable final Object value) {
		final INBTSerializable<?> current = this.readFromField(field, instance);
		if (current == null) {
			return;
		}
	}
}
