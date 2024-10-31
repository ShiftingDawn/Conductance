package conductance.api.machine.data.serializer;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;

public class ArraySerializer implements DataSerializer<DataSerializer<?>[]> {

	@Getter
	@Setter
	private DataSerializer<?>[] data;

	@Override
	public Tag serialize(final HolderLookup.Provider registries) {
		return Util.make(new ListTag(), list -> {
			for (int i = 0; i < this.getData().length; ++i) {
				final DataSerializer<?> serializer = this.getData()[i];
				final CompoundTag entry = new CompoundTag();
				entry.putInt("sid", serializer.getId());
				final Tag dataTag = serializer.serialize(registries);
				if (dataTag != null) {
					entry.put("d", dataTag);
				}
				list.add(entry);
			}
		});
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		if (!(tag instanceof final ListTag listTag)) {
			throw new IllegalStateException("Trying to deserialize non-ListTag: " + tag);
		}
		final DataSerializer<?>[] arr = new DataSerializer[listTag.size()];
		for (int i = 0; i < arr.length; ++i) {
			final Tag entryTag = listTag.get(i);
			if (!(entryTag instanceof final CompoundTag entry)) {
				throw new IllegalStateException("Trying to deserialize non-CompoundTag at position " + i + ", data: " + tag);
			}
			arr[i] = CAPI.managedDataRegistry().makeSerializer(entry.getInt("sid"));
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create DataSerializer with id " + entry.getInt("sid"));
			}
			final Tag dataTag = entry.get("d");
			if (dataTag != null) {
				arr[i].deserialize(dataTag, registries);
			}
		}
		this.setData(arr);
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(this.getData().length);
		for (final DataSerializer<?> serializer : this.getData()) {
			buf.writeVarInt(serializer.getId());
			serializer.toNetwork(buf);
		}
	}

	@Override
	public void fromNetwork(final RegistryFriendlyByteBuf buf) {
		final DataSerializer<?>[] arr = new DataSerializer<?>[buf.readVarInt()];
		for (int i = 0; i < arr.length; ++i) {
			final int serializerId = buf.readVarInt();
			final DataSerializer<?> serializer = CAPI.managedDataRegistry().makeSerializer(serializerId);
			if (serializer == null) {
				throw new IllegalStateException("Could not create DataSerializer with id " + serializerId);
			}
			serializer.fromNetwork(buf);
		}
		this.setData(arr);
	}
}
