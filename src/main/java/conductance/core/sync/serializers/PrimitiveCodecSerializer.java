package conductance.core.sync.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

public class PrimitiveCodecSerializer<T> extends Serializer<T> {

	private final PrimitiveCodec<T> codec;
	private final StreamCodec<ByteBuf, T> streamCodec;

	public PrimitiveCodecSerializer(final PrimitiveCodec<T> codec, final StreamCodec<ByteBuf, T> streamCodec) {
		this.codec = codec;
		this.streamCodec = streamCodec;
	}

	@Override
	public @Nullable Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.codec.write(NbtOps.INSTANCE, this.getData());
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		this.setData(this.codec.read(NbtOps.INSTANCE, tag).getOrThrow());
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.streamCodec.encode(buf, this.getData());
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.setData(this.streamCodec.decode(buf));
	}

	public static final class BooleanSerializer extends PrimitiveCodecSerializer<Boolean> {

		public BooleanSerializer() {
			super(Codec.BOOL, ByteBufCodecs.BOOL);
		}
	}

	public static final class ByteSerializer extends PrimitiveCodecSerializer<Byte> {

		public ByteSerializer() {
			super(Codec.BYTE, ByteBufCodecs.BYTE);
		}
	}

	public static final class ShortSerializer extends PrimitiveCodecSerializer<Short> {

		public ShortSerializer() {
			super(Codec.SHORT, ByteBufCodecs.SHORT);
		}
	}

	public static final class IntSerializer extends PrimitiveCodecSerializer<Integer> {

		public IntSerializer() {
			super(Codec.INT, ByteBufCodecs.VAR_INT);
		}
	}

	public static final class LongSerializer extends PrimitiveCodecSerializer<Long> {

		public LongSerializer() {
			super(Codec.LONG, ByteBufCodecs.VAR_LONG);
		}
	}

	public static final class FloatSerializer extends PrimitiveCodecSerializer<Float> {

		public FloatSerializer() {
			super(Codec.FLOAT, ByteBufCodecs.FLOAT);
		}
	}

	public static final class DoubleSerializer extends PrimitiveCodecSerializer<Double> {

		public DoubleSerializer() {
			super(Codec.DOUBLE, ByteBufCodecs.DOUBLE);
		}
	}

	public static final class CharSerializer extends PrimitiveCodecSerializer<Character> {

		public CharSerializer() {
			super(CharSerializer.CODEC, ByteBufCodecs.VAR_INT.map(i -> (char) i.intValue(), c -> (int) c));
		}

		private static final PrimitiveCodec<Character> CODEC = new PrimitiveCodec<>() {

			@Override
			public <T> DataResult<Character> read(final DynamicOps<T> ops, final T input) {
				return ops.getNumberValue(input).map(number -> (char) number.intValue());
			}

			@Override
			public <T> T write(final DynamicOps<T> ops, final Character value) {
				return ops.createInt(value);
			}

			@Override
			public String toString() {
				return "Char";
			}
		};
	}

	public static final class StringSerializer extends PrimitiveCodecSerializer<String> {

		public StringSerializer() {
			super(Codec.STRING, ByteBufCodecs.STRING_UTF8);
		}
	}
}
