package conductance.core.sync;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import conductance.api.CAPI;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

class ArraySerializer extends Serializer<Serializer<?>[]> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.serialize(data -> {
			final ListTag list = new ListTag();
			final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, data.length);
			for (int i = 0; i < data.length; ++i) {
				final Serializer<?> serializer = data[i];
				final CompoundTag entry = new CompoundTag();
				entry.putInt("i", i);
				entry.putInt("sid", serializer.getSid());
				final Tag dataTag = serializer.serialize(operation, arrayRefs[i], registries);
				if (dataTag != null) {
					entry.put("dat", dataTag);
				}
			}
			return list;
		});
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, @Nullable final Tag tag, final HolderLookup.Provider registries) {
		this.deserialize(tag, ListTag.class, array -> {
			final Serializer<?>[] arr = new Serializer[array.size()];
			final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, arr.length);
			for (int j = 0; j < arr.length; ++j) {
				final CompoundTag entry = this.testTag(array.get(j), CompoundTag.class);
				final int i = entry.getInt("i");
				arr[i] = CAPI.syncHelper().getSerializerById(entry.getInt("sid"));
				if (arr[i] == null) {
					throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), entry.getInt("sid")));
				}
				final Tag dataTag = entry.get("dat");
				if (dataTag != null) {
					arr[i].deserialize(operation, arrayRefs[i], dataTag, registries);
				}
			}
			return arr;
		});
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.write(buf, data -> {
			final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, data.length);
			buf.writeVarInt(data.length);
			for (int i = 0; i < data.length; ++i) {
				final Serializer<?> serializer = data[i];
				buf.writeVarInt(serializer.getSid());
				serializer.toNetwork(operation, arrayRefs[i], buf, registries);
			}
		});
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.read(buf, () -> {
			final Serializer<?>[] arr = new Serializer[buf.readVarInt()];
			final Reference[] arrayRefs = ArraySerializer.makeRefs(ref, arr.length);
			for (int i = 0; i < arr.length; ++i) {
				final int sid = buf.readVarInt();
				arr[i] = SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerById(sid);
				if (arr[i] == null) {
					throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), sid));
				}
				arr[i].fromNetwork(operation, arrayRefs[i], buf, registries);
			}
			return arr;
		});
	}

	private static Reference[] makeRefs(final Reference arrayRef, final int length) {
		final Class<?> fieldType = arrayRef.getKey().getRawField().getType();
		if (fieldType.isArray()) {
			final Object array = arrayRef.getValueHolder().get();
			final Class<?> arrayType = fieldType.getComponentType();
			final Reference[] result = new Reference[length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = ReferenceHelper.forArray(arrayRef.getKey(), array, i, arrayType);
			}
			return result;
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			final Collection<?> collection = (Collection<?>) arrayRef.getValueHolder().get();
			final Reference[] result = new Reference[length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = ReferenceHelper.forCollection(arrayRef.getKey(), collection, i);
			}
			return result;
		} else {
			throw new IllegalStateException("Could not create %s for unknown array field-type %s".formatted(Reference.class.getName(), fieldType));
		}
	}

	public static ArraySerializer of(final Serializer<?>[] data) {
		return Util.make(new ArraySerializer(), serializer -> serializer.setData(data));
	}
}
