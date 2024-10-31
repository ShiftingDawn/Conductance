package conductance.core.datahandlers;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.StringSerializer;

public final class EnumValueHandler implements ManagedFieldValueHandler {

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isArray();
	}

	@Override
	public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
		final Object data = field.get();
		if (data instanceof final StringRepresentable stringRepresentable) {
			return StringSerializer.of(stringRepresentable.getSerializedName());
		} else {
			return StringSerializer.of(((Enum<?>) data).name());
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
		final Object currentData = field.get();
		if (!currentData.getClass().isEnum()) {
			throw new IllegalStateException("Field is not an enum");
		}
		if (!(value instanceof final StringSerializer stringSerializer)) {
			throw new IllegalArgumentException("Serializer %s is not %s".formatted(value.getClass().getName(), StringSerializer.class.getName()));
		}
		final Enum newValue = EnumValueHandler.getEnum((Class<Enum>) currentData.getClass(), stringSerializer.getData());
		field.set(newValue);
	}

	@Nullable
	private static <T extends Enum<T>> T getEnum(final Class<T> type, final String name) {
		for (final T constant : type.getEnumConstants()) {
			if (EnumValueHandler.getEnumName(constant).equals(name)) {
				return constant;
			}
		}
		return null;
	}

	private static String getEnumName(final Enum<?> constant) {
		return constant instanceof final StringRepresentable provider ? provider.getSerializedName() : constant.name();
	}
}
