package conductance.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.network.codec.StreamCodec;
import io.netty.buffer.ByteBuf;

public record DispatchedMapStreamCodec<B extends ByteBuf, K, V>(StreamCodec<B, K> keyCodec, Function<K, StreamCodec<B, ? extends V>> valueCodecFunction) implements StreamCodec<B, Map<K, V>> {

	@Override
	public void encode(final B buffer, final Map<K, V> map) {
		buffer.writeInt(map.size());
		for (final Map.Entry<K, V> entry : map.entrySet()) {
			this.keyCodec.encode(buffer, entry.getKey());
			this.encodeValue(buffer, this.valueCodecFunction.apply(entry.getKey()), entry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private <T, V2 extends V> void encodeValue(final B buffer, final StreamCodec<B, V2> codec, final V input) {
		codec.encode(buffer, (V2) input);
	}

	@Override
	public Map<K, V> decode(final B b) {
		final HashMap<K, V> result = new HashMap<>();
		for (int i = 0; i < b.readInt(); ++i) {
			final K key = this.keyCodec.decode(b);
			final V value = this.valueCodecFunction.apply(key).decode(b);
			result.put(key, value);
		}
		return result;
	}
}
