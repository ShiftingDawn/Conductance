package conductance.api.machine.data.serializer;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public abstract class PrimitiveDataSerializer<T> implements DataSerializer<T> {

	@Getter
	@Setter
	private T data;

	@Nullable
	public static DataSerializer<?> tryGetPrimitiveSerializer(final Object object) {
		return switch (object) {
			case final Boolean value -> BooleanSerializer.of(value);
			case final Byte value -> ByteSerializer.of(value);
			case final Short value -> ShortSerializer.of(value);
			case final Integer value -> IntegerSerializer.of(value);
			case final Long value -> LongSerializer.of(value);
			case final Float value -> FloatSerializer.of(value);
			case final Double value -> DoubleSerializer.of(value);
			case final Character value -> CharacterSerializer.of(value);
			default -> null;
		};
	}

	public static class BooleanSerializer extends PrimitiveDataSerializer<Boolean> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return ByteTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData(((ByteTag) tag).getAsByte() != 0);
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeBoolean(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readBoolean());
		}

		public static BooleanSerializer of(final boolean value) {
			return Util.make(new BooleanSerializer(), serializer -> serializer.setData(value));
		}
	}

	public static class ByteSerializer extends PrimitiveDataSerializer<Byte> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return ByteTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData(((ByteTag) tag).getAsByte());
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeByte(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readByte());
		}

		public static ByteSerializer of(final byte value) {
			return Util.make(new ByteSerializer(), serializer -> serializer.setData(value));
		}
	}

	public static class ShortSerializer extends PrimitiveDataSerializer<Short> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return ShortTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData(((ShortTag) tag).getAsShort());
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeShort(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readShort());
		}

		public static ShortSerializer of(final short value) {
			return Util.make(new ShortSerializer(), serializer -> serializer.setData(value));
		}
	}

	public static class IntegerSerializer extends PrimitiveDataSerializer<Integer> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return IntTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData(((IntTag) tag).getAsInt());
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeVarInt(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readVarInt());
		}

		public static IntegerSerializer of(final int value) {
			return Util.make(new IntegerSerializer(), serializer -> serializer.setData(value));
		}
	}

	public static class LongSerializer extends PrimitiveDataSerializer<Long> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return LongTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData(((IntTag) tag).getAsLong());
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeVarLong(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readVarLong());
		}

		public static LongSerializer of(final long value) {
			return Util.make(new LongSerializer(), serializer -> serializer.setData(value));
		}
	}

	public static class FloatSerializer extends PrimitiveDataSerializer<Float> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return FloatTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData(((FloatTag) tag).getAsFloat());
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeFloat(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readFloat());
		}

		public static FloatSerializer of(final float value) {
			return Util.make(new FloatSerializer(), serializer -> serializer.setData(value));
		}
	}

	public static class DoubleSerializer extends PrimitiveDataSerializer<Double> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return DoubleTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData(((DoubleTag) tag).getAsDouble());
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeDouble(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readDouble());
		}

		public static DoubleSerializer of(final double value) {
			return Util.make(new DoubleSerializer(), serializer -> serializer.setData(value));
		}
	}

	public static class CharacterSerializer extends PrimitiveDataSerializer<Character> {

		@Override
		public @Nullable Tag serialize(final HolderLookup.Provider registries) {
			return IntTag.valueOf(this.getData());
		}

		@Override
		public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
			this.setData((char) ((IntTag) tag).getAsInt());
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			buf.writeChar(this.getData());
		}

		@Override
		public void fromNetwork(final RegistryFriendlyByteBuf buf) {
			this.setData(buf.readChar());
		}

		public static CharacterSerializer of(final char value) {
			return Util.make(new CharacterSerializer(), serializer -> serializer.setData(value));
		}
	}
}
