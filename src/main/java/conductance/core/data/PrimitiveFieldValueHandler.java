package conductance.core.data;

import java.lang.reflect.Field;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import conductance.api.machine.data.ManagedFieldValueHandler;

public abstract class PrimitiveFieldValueHandler<T> implements ManagedFieldValueHandler<T> {

	@Override
	public boolean equals(final T value1, final T value2) {
		return value1 == value2;
	}

	public static final class HandlerBoolean extends PrimitiveFieldValueHandler<Boolean> {

		@Override
		public Class<Boolean> getFieldType() {
			return Boolean.TYPE;
		}

		@Override
		public Boolean getValue(final Field field, final Object instance) {
			try {
				return field.getBoolean(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Boolean value) {
			return ByteTag.valueOf(value);
		}

		@Override
		public Boolean deserialize(final Tag nbt) {
			return ((ByteTag) nbt).getAsByte() == ByteTag.ONE.getAsByte();
		}
	}

	public static final class HandlerByte extends PrimitiveFieldValueHandler<Byte> {

		@Override
		public Class<Byte> getFieldType() {
			return Byte.TYPE;
		}

		@Override
		public Byte getValue(final Field field, final Object instance) {
			try {
				return field.getByte(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Byte value) {
			return ByteTag.valueOf(value);
		}

		@Override
		public Byte deserialize(final Tag nbt) {
			return ((ByteTag) nbt).getAsByte();
		}
	}

	public static final class HandlerShort extends PrimitiveFieldValueHandler<Short> {

		@Override
		public Class<Short> getFieldType() {
			return Short.TYPE;
		}

		@Override
		public Short getValue(final Field field, final Object instance) {
			try {
				return field.getShort(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Short value) {
			return ShortTag.valueOf(value);
		}

		@Override
		public Short deserialize(final Tag nbt) {
			return ((ShortTag) nbt).getAsShort();
		}
	}

	public static final class HandlerInteger extends PrimitiveFieldValueHandler<Integer> {

		@Override
		public Class<Integer> getFieldType() {
			return Integer.TYPE;
		}

		@Override
		public Integer getValue(final Field field, final Object instance) {
			try {
				return field.getInt(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Integer value) {
			return IntTag.valueOf(value);
		}

		@Override
		public Integer deserialize(final Tag nbt) {
			return ((IntTag) nbt).getAsInt();
		}
	}

	public static final class HandlerLong extends PrimitiveFieldValueHandler<Long> {

		@Override
		public Class<Long> getFieldType() {
			return Long.TYPE;
		}

		@Override
		public Long getValue(final Field field, final Object instance) {
			try {
				return field.getLong(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Long value) {
			return LongTag.valueOf(value);
		}

		@Override
		public Long deserialize(final Tag nbt) {
			return ((LongTag) nbt).getAsLong();
		}
	}

	public static final class HandlerFloat extends PrimitiveFieldValueHandler<Float> {

		@Override
		public Class<Float> getFieldType() {
			return Float.TYPE;
		}

		@Override
		public Float getValue(final Field field, final Object instance) {
			try {
				return field.getFloat(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Float value) {
			return FloatTag.valueOf(value);
		}

		@Override
		public Float deserialize(final Tag nbt) {
			return ((FloatTag) nbt).getAsFloat();
		}
	}

	public static final class HandlerDouble extends PrimitiveFieldValueHandler<Double> {

		@Override
		public Class<Double> getFieldType() {
			return Double.TYPE;
		}

		@Override
		public Double getValue(final Field field, final Object instance) {
			try {
				return field.getDouble(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Double value) {
			return DoubleTag.valueOf(value);
		}

		@Override
		public Double deserialize(final Tag nbt) {
			return ((DoubleTag) nbt).getAsDouble();
		}
	}

	public static final class HandlerCharacter extends PrimitiveFieldValueHandler<Character> {

		@Override
		public Class<Character> getFieldType() {
			return Character.TYPE;
		}

		@Override
		public Character getValue(final Field field, final Object instance) {
			try {
				return field.getChar(instance);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Character value) {
			return StringTag.valueOf(String.valueOf(value));
		}

		@Override
		public Character deserialize(final Tag nbt) {
			return nbt.getAsString().charAt(0);
		}
	}

	private PrimitiveFieldValueHandler() {
	}
}
