package conductance.sync;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;

public class NBTSerializableHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return INBTSerializable.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		final INBTSerializable<?> serializable = (INBTSerializable<?>) ref.getValueHolder().get();
		assert serializable != null;
		return Util.make(new TagSerializer(), serializer -> serializer.setData(serializable.serializeNBT(registries)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer, final HolderLookup.Provider registries) {
		final TagSerializer serializer = this.testSerializer(rawSerializer, TagSerializer.class);
		final INBTSerializable<Tag> serializable = (INBTSerializable<Tag>) ref.getValueHolder().get();
		assert serializable != null;
		serializable.deserializeNBT(registries, serializer.getData());
	}
}
