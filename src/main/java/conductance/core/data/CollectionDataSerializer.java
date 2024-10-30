package conductance.core.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.DataSerializer;

@SuppressWarnings({"unchecked", "rawtypes"})
final class CollectionDataSerializer implements DataSerializer<Collection> {

	static final BiFunction<DataSerializer<?>, Class<?>, CollectionDataSerializer> FACTORY = Util.memoize(CollectionDataSerializer::new);

	private final DataSerializer contentSerializer;

	CollectionDataSerializer(final DataSerializer<?> contentSerializer, final Class<?> contentType) {
		this.contentSerializer = contentSerializer;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isArray();
	}

	@Override
	public Tag serialize(final Collection value, final HolderLookup.Provider provider) {
		return Util.make(new ListTag(), list -> value.forEach(item -> list.add(this.contentSerializer.serialize(item, provider)));
	}

	@Override
	@Nullable
	public Collection deserialize(final Tag tag, final HolderLookup.Provider provider) {
		if (!(tag instanceof final ListTag list)) {
			return null;
		}
		final Collection result = new ArrayList();
		for (final Tag item : list) {
			result.add(this.contentSerializer.deserialize(item, provider));
		}
		return result;
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf, final Collection value) {
		buf.writeVarInt(value.size());
		for (int i = 0; i < Array.getLength(value); ++i) {
			this.contentSerializer.toNetwork(buf, Array.get(value, i));
		}
	}

	@Override
	@Nullable
	public Collection fromNetwork(final RegistryFriendlyByteBuf buf) {
		final Collection result = new ArrayList();
		for (int i = 0; i < buf.readVarInt(); ++i) {
			result.add(this.contentSerializer.fromNetwork(buf));
		}
		return result;
	}
}
