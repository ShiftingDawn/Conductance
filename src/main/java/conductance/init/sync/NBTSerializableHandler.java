package conductance.init.sync;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import conductance.api.sync.Operation;
import conductance.api.sync.Reference;
import conductance.api.sync.ReferenceHandler;
import conductance.api.sync.Serializer;
import conductance.Conductance;

public class NBTSerializableHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return INBTSerializable.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		final INBTSerializable<?> serializable = (INBTSerializable<?>) ref.getValueHolder().get();
		return Util.make(new TagSerializer(), serializer -> {
			if (serializable != null) {
				serializer.setData(serializable.serializeNBT(registries));
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer, final HolderLookup.Provider registries) {
		final TagSerializer serializer = this.testSerializer(rawSerializer, TagSerializer.class);
		if (serializer.getData() == null) {
			Conductance.LOGGER.warn("Encountered should not happen ", new Exception());
			return;
		}
		final INBTSerializable<Tag> serializable = (INBTSerializable<Tag>) ref.getValueHolder().get();
		if (serializable != null) {
			serializable.deserializeNBT(registries, serializer.getData());
		}
	}
}
