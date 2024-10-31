package conductance.core.datahandlers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.NbtDataSerializer;

public final class NbtSerializableValueHandler implements ManagedFieldValueHandler {

	@Override
	public boolean canHandle(final Class<?> type) {
		return INBTSerializable.class.isAssignableFrom(type);
	}

	@Override
	public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
		final Object data = field.get();
		if (!(data instanceof final INBTSerializable<?> inbtSerializable)) {
			throw new IllegalArgumentException("Field %s is not INBTSerializable".formatted(data));
		}
		final Tag tag = inbtSerializable.serializeNBT(registries);
		return NbtDataSerializer.of(tag);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
		final Object data = field.get();
		if (!(data instanceof final INBTSerializable inbtSerializable)) {
			throw new IllegalArgumentException("Field %s is not INBTSerializable".formatted(data));
		}
		if (!(value instanceof final NbtDataSerializer nbtDataSerializer)) {
			throw new IllegalArgumentException("Serializer %s is not %s".formatted(value.getClass().getName(), NbtDataSerializer.class.getName()));
		}
		inbtSerializable.deserializeNBT(registries, nbtDataSerializer.getData());
	}
}
