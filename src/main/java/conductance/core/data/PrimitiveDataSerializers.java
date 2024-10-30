package conductance.core.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import com.google.common.primitives.Primitives;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.DataSerializer;

abstract class PrimitiveDataSerializers<T> implements DataSerializer<T> {

	private final Class<T> typeClass;

	protected PrimitiveDataSerializers(final Class<T> typeClass) {
		this.typeClass = typeClass;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type == this.typeClass || type == Primitives.wrap(this.typeClass);
	}

	public static class SerializerBoolean extends PrimitiveDataSerializers<Boolean> {

		SerializerBoolean() {
			super(Boolean.TYPE);
		}

		@Override
		public Tag serialize(final Boolean value, final HolderLookup.Provider provider) {
			return ByteTag.valueOf(value);
		}

		@Override
		public @Nullable Boolean deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final NumericTag numericTag && numericTag.getAsByte() == 1;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Boolean value) {
			buf.writeBoolean(value);
		}

		@Override
		public @Nullable Boolean fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readBoolean();
		}
	}

	public static class SerializerByte extends PrimitiveDataSerializers<Byte> {

		SerializerByte() {
			super(Byte.TYPE);
		}

		@Override
		public Tag serialize(final Byte value, final HolderLookup.Provider provider) {
			return ByteTag.valueOf(value);
		}

		@Override
		public @Nullable Byte deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final NumericTag numericTag ? numericTag.getAsByte() : 0;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Byte value) {
			buf.writeByte(value);
		}

		@Override
		public @Nullable Byte fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readByte();
		}
	}

	public static class SerializerShort extends PrimitiveDataSerializers<Short> {

		SerializerShort() {
			super(Short.TYPE);
		}

		@Override
		public Tag serialize(final Short value, final HolderLookup.Provider provider) {
			return ShortTag.valueOf(value);
		}

		@Override
		public @Nullable Short deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final NumericTag numericTag ? numericTag.getAsShort() : 0;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Short value) {
			buf.writeShort(value);
		}

		@Override
		public @Nullable Short fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readShort();
		}
	}

	public static class SerializerInteger extends PrimitiveDataSerializers<Integer> {

		SerializerInteger() {
			super(Integer.TYPE);
		}

		@Override
		public Tag serialize(final Integer value, final HolderLookup.Provider provider) {
			return IntTag.valueOf(value);
		}

		@Override
		public @Nullable Integer deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final NumericTag numericTag ? numericTag.getAsInt() : 0;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Integer value) {
			buf.writeVarInt(value);
		}

		@Override
		public @Nullable Integer fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readVarInt();
		}
	}

	public static class SerializerLong extends PrimitiveDataSerializers<Long> {

		SerializerLong() {
			super(Long.TYPE);
		}

		@Override
		public Tag serialize(final Long value, final HolderLookup.Provider provider) {
			return LongTag.valueOf(value);
		}

		@Override
		public @Nullable Long deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final NumericTag numericTag ? numericTag.getAsLong() : 0;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Long value) {
			buf.writeVarLong(value);
		}

		@Override
		public @Nullable Long fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readVarLong();
		}
	}

	public static class SerializerFloat extends PrimitiveDataSerializers<Float> {

		SerializerFloat() {
			super(Float.TYPE);
		}

		@Override
		public Tag serialize(final Float value, final HolderLookup.Provider provider) {
			return FloatTag.valueOf(value);
		}

		@Override
		public @Nullable Float deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final NumericTag numericTag ? numericTag.getAsFloat() : 0;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Float value) {
			buf.writeFloat(value);
		}

		@Override
		public @Nullable Float fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readFloat();
		}
	}

	public static class SerializerDouble extends PrimitiveDataSerializers<Double> {

		SerializerDouble() {
			super(Double.TYPE);
		}

		@Override
		public Tag serialize(final Double value, final HolderLookup.Provider provider) {
			return DoubleTag.valueOf(value);
		}

		@Override
		public @Nullable Double deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final NumericTag numericTag ? numericTag.getAsDouble() : 0;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Double value) {
			buf.writeDouble(value);
		}

		@Override
		public @Nullable Double fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readDouble();
		}
	}

	public static class SerializerCharacter extends PrimitiveDataSerializers<Character> {

		SerializerCharacter() {
			super(Character.TYPE);
		}

		@Override
		public Tag serialize(final Character value, final HolderLookup.Provider provider) {
			return StringTag.valueOf(String.valueOf(value));
		}

		@Override
		public @Nullable Character deserialize(final Tag tag, final HolderLookup.Provider provider) {
			return tag instanceof final StringTag stringTag && !stringTag.getAsString().isEmpty() ? stringTag.getAsString().charAt(0) : 0;
		}

		@Override
		public void toNetwork(final RegistryFriendlyByteBuf buf, final Character value) {
			buf.writeChar(value);
		}

		@Override
		public @Nullable Character fromNetwork(final RegistryFriendlyByteBuf buf) {
			return buf.readChar();
		}
	}
}
