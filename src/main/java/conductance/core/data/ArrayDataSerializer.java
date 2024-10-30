package conductance.core.data;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.DataSerializer;

@SuppressWarnings({"unchecked", "rawtypes"})
final class ArrayDataSerializer implements DataSerializer<Object> {

	static final BiFunction<DataSerializer<?>, Class<?>, ArrayDataSerializer> FACTORY = Util.memoize(ArrayDataSerializer::new);

	private final DataSerializer contentSerializer;
	private final Class<?> contentType;

	ArrayDataSerializer(final DataSerializer<?> contentSerializer, final Class<?> contentType) {
		this.contentSerializer = contentSerializer;
		this.contentType = contentType;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isArray();
	}

	@Override
	public Tag serialize(final Object value, final HolderLookup.Provider provider) {
		return Util.make(new ListTag(), list -> {
			for (int i = 0; i < Array.getLength(value); ++i) {
				this.contentSerializer.serialize(Array.get(value, i), provider);
			}
		});
	}

	@Override
	@Nullable
	public Object deserialize(final Tag tag, final HolderLookup.Provider provider) {
		if (!(tag instanceof final ListTag list)) {
			return null;
		}
		final Object result = Array.newInstance(this.contentType, list.size());
		for (int i = 0; i < Array.getLength(result); ++i) {
			final Object item = this.contentSerializer.deserialize(list.get(i), provider);
			Array.set(result, i, item);
		}
		return result;
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf, final Object value) {
		buf.writeVarInt(Array.getLength(value));
		for (int i = 0; i < Array.getLength(value); ++i) {
			this.contentSerializer.toNetwork(buf, Array.get(value, i));
		}
	}

	@Override
	@Nullable
	public Object fromNetwork(final RegistryFriendlyByteBuf buf) {
		final Object result = Array.newInstance(this.contentType, buf.readVarInt());
		for (int i = 0; i < Array.getLength(result); ++i) {
			final Object item = this.contentSerializer.fromNetwork(buf);
			Array.set(result, i, item);
		}
		return result;
	}
}
