package conductance.core.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
abstract class PrimitiveCodecSerializer<T> extends Serializer<T> {

	private final PrimitiveCodec<T> codec;

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.serialize(data -> this.codec.write(NbtOps.INSTANCE, data));
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, @Nullable final Tag tag, final HolderLookup.Provider registries) {
		this.deserialize(tag, Tag.class, data -> this.codec.read(NbtOps.INSTANCE, tag).getOrThrow());
	}

	public static final class BooleanSerializer extends PrimitiveCodecSerializer<Boolean> {

		BooleanSerializer() {
			super(Codec.BOOL);
		}
	}

	public static final class ByteSerializer extends PrimitiveCodecSerializer<Byte> {

		ByteSerializer() {
			super(Codec.BYTE);
		}
	}

	public static final class ShortSerializer extends PrimitiveCodecSerializer<Short> {

		ShortSerializer() {
			super(Codec.SHORT);
		}
	}

	public static final class IntSerializer extends PrimitiveCodecSerializer<Integer> {

		IntSerializer() {
			super(Codec.INT);
		}
	}

	public static final class LongSerializer extends PrimitiveCodecSerializer<Long> {

		LongSerializer() {
			super(Codec.LONG);
		}
	}

	public static final class FloatSerializer extends PrimitiveCodecSerializer<Float> {

		FloatSerializer() {
			super(Codec.FLOAT);
		}
	}

	public static final class DoubleSerializer extends PrimitiveCodecSerializer<Double> {

		DoubleSerializer() {
			super(Codec.DOUBLE);
		}
	}

	public static final class CharSerializer extends PrimitiveCodecSerializer<Character> {

		CharSerializer() {
			super(CharSerializer.CODEC);
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

		StringSerializer() {
			super(Codec.STRING);
		}
	}
}
