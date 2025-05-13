package conductance.core.sync.serializers;

import java.util.Collection;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.SyncFieldSerializerRegisterImpl;
import conductance.core.sync.ref.ArrayReference;
import conductance.core.sync.ref.CollectionReference;
import conductance.core.sync.ref.ReferenceImpl;

public class ArraySerializer extends Serializer<Serializer<?>[]> {

	@Override
	public @Nullable Tag serialize(final Reference ref) {
		final ListTag list = new ListTag();
		final ReferenceImpl[] arrayRefs = ArraySerializer.makeRefs(ref, this.getData().length);
		for (int i = 0; i < this.getData().length; ++i) {
			final Serializer<?> serializer = this.getData()[i];
			final CompoundTag entry = new CompoundTag();
			entry.putInt("i", i);
			entry.putInt("sid", serializer.getSid());
			final Tag dataTag = serializer.serialize(arrayRefs[i]);
			if (dataTag != null) {
				entry.put("dat", dataTag);
			}
		}
		return list;
	}

	@Override
	public void deserialize(final Reference ref, final Tag tag) {
		final ListTag array = this.testTag(tag, ListTag.class);
		final Serializer<?>[] arr = new Serializer[array.size()];
		final ReferenceImpl[] arrayRefs = ArraySerializer.makeRefs(ref, arr.length);
		for (int j = 0; j < arr.length; ++j) {
			final CompoundTag entry = this.testTag(array.get(j), CompoundTag.class);
			final int i = entry.getInt("i");
			arr[i] = CAPI.syncHelper().getSerializerById(entry.getInt("sid"));
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), entry.getInt("sid")));
			}
			final Tag dataTag = entry.get("dat");
			if (dataTag != null) {
				arr[i].deserialize(arrayRefs[i], dataTag);
			}
		}
		this.setData(arr);
	}

	@Override
	public void toNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		final ReferenceImpl[] arrayRefs = ArraySerializer.makeRefs(ref, this.getData().length);
		buf.writeVarInt(this.getData().length);
		for (int i = 0; i < this.getData().length; ++i) {
			final Serializer<?> serializer = this.getData()[i];
			buf.writeVarInt(serializer.getSid());
			serializer.toNetwork(arrayRefs[i], buf);
		}
	}

	@Override
	public void fromNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		final Serializer<?>[] arr = new Serializer[buf.readVarInt()];
		final ReferenceImpl[] arrayRefs = ArraySerializer.makeRefs(ref, arr.length);
		for (int i = 0; i < arr.length; ++i) {
			final int sid = buf.readVarInt();
			arr[i] = SyncFieldSerializerRegisterImpl.INSTANCE.getSerializerById(sid);
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), sid));
			}
			arr[i].fromNetwork(arrayRefs[i], buf);
		}
		this.setData(arr);
	}

	private static ReferenceImpl[] makeRefs(final Reference arrayRef, final int length) {
		final Class<?> fieldType = arrayRef.getKey().getRawField().getType();
		if (fieldType.isArray()) {
			final Object array = arrayRef.getValueHolder().get();
			final Class<?> arrayType = fieldType.getComponentType();
			final ReferenceImpl[] result = new ReferenceImpl[length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = ArrayReference.of(arrayRef.getKey(), array, i, arrayType);
			}
			return result;
		} else if (Collection.class.isAssignableFrom(fieldType)) {
			final Collection<?> collection = (Collection<?>) arrayRef.getValueHolder().get();
			final ReferenceImpl[] result = new ReferenceImpl[length];
			for (int i = 0; i < result.length; ++i) {
				result[i] = CollectionReference.of(arrayRef.getKey(), collection, i);
			}
			return result;
		} else {
			throw new IllegalStateException("Could not create %ss for unknown array field-type %s".formatted(ReferenceImpl.class.getName(), fieldType));
		}
	}

	public static ArraySerializer of(final Serializer<?>[] data) {
		return Util.make(new ArraySerializer(), serializer -> serializer.setData(data));
	}
}
