package conductance.core.data;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

public class EnumFieldValueHandler implements ManagedFieldValueHandler<Enum<?>> {

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isEnum();
	}

	@Override
	public @Nullable Enum<?> getValue(final Field field, final Object instance) {
		try {
			return (Enum<?>) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValue(final Field field, final Object instance, @Nullable final Enum<?> value) {
		try {
			field.set(instance, value);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(final Enum<?> value1, final Enum<?> value2) {
		return value1 == value2;
	}

	@Override
	public Tag serialize(final Enum<?> value, final HolderLookup.Provider registries) {
		return StringTag.valueOf(value.toString().toLowerCase(Locale.ROOT));
	}

	@Override
	public @Nullable Enum<?> deserialize(final Tag nbt, final HolderLookup.Provider registries) {
		return null;
	}

	@Override
	public @Nullable Enum<?> deserialize(final Class<Enum<?>> fieldType, final Tag nbt, final HolderLookup.Provider registries) {
		return Arrays.stream(fieldType.getEnumConstants()).filter(constant -> constant.toString().toLowerCase(Locale.ROOT).equals(nbt.getAsString())).findFirst().orElse(null);
	}
}
