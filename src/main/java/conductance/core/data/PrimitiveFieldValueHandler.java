package conductance.core.data;

import java.lang.reflect.Field;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import com.google.common.primitives.Primitives;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

abstract class PrimitiveFieldValueHandler<T> implements ManagedFieldValueHandler<T> {

	private final Class<T> typeClass;

	protected PrimitiveFieldValueHandler(final Class<T> typeClass) {
		this.typeClass = typeClass;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type == this.typeClass || type == Primitives.wrap(this.typeClass);
	}

	@Override
	public boolean equals(@Nullable final T value1, @Nullable final T value2) {
		return value1 == value2;
	}

	public static final class HandlerBoolean extends PrimitiveFieldValueHandler<Boolean> {

		HandlerBoolean() {
			super(Boolean.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Boolean value) {
			try {
				field.setBoolean(instance, value != null && value);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(final Boolean value, final HolderLookup.Provider registries) {
			return ByteTag.valueOf(value);
		}

		@Override
		public Boolean deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return ((ByteTag) nbt).getAsByte() == ByteTag.ONE.getAsByte();
		}
	}

	public static final class HandlerByte extends PrimitiveFieldValueHandler<Byte> {

		HandlerByte() {
			super(Byte.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Byte value) {
			try {
				field.setByte(instance, value == null ? 0 : value);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(@Nullable final Byte value, final HolderLookup.Provider registries) {
			return ByteTag.valueOf(value != null ? value : 0);
		}

		@Override
		public Byte deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return ((ByteTag) nbt).getAsByte();
		}
	}

	public static final class HandlerShort extends PrimitiveFieldValueHandler<Short> {

		HandlerShort() {
			super(Short.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Short value) {
			try {
				field.setShort(instance, value != null ? value : 0);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(@Nullable final Short value, final HolderLookup.Provider registries) {
			return ShortTag.valueOf(value != null ? value : 0);
		}

		@Override
		public Short deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return ((ShortTag) nbt).getAsShort();
		}
	}

	public static final class HandlerInteger extends PrimitiveFieldValueHandler<Integer> {

		HandlerInteger() {
			super(Integer.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Integer value) {
			try {
				field.setInt(instance, value != null ? value : 0);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(@Nullable final Integer value, final HolderLookup.Provider registries) {
			return IntTag.valueOf(value != null ? value : 0);
		}

		@Override
		public Integer deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return ((IntTag) nbt).getAsInt();
		}
	}

	public static final class HandlerLong extends PrimitiveFieldValueHandler<Long> {

		HandlerLong() {
			super(Long.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Long value) {
			try {
				field.setLong(instance, value != null ? value : 0);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(@Nullable final Long value, final HolderLookup.Provider registries) {
			return LongTag.valueOf(value != null ? value : 0);
		}

		@Override
		public Long deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return ((LongTag) nbt).getAsLong();
		}
	}

	public static final class HandlerFloat extends PrimitiveFieldValueHandler<Float> {

		HandlerFloat() {
			super(Float.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Float value) {
			try {
				field.setFloat(instance, value != null ? value : 0f);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(@Nullable final Float value, final HolderLookup.Provider registries) {
			return FloatTag.valueOf(value != null ? value : 0f);
		}

		@Override
		public Float deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return ((FloatTag) nbt).getAsFloat();
		}
	}

	public static final class HandlerDouble extends PrimitiveFieldValueHandler<Double> {

		HandlerDouble() {
			super(Double.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Double value) {
			try {
				field.setDouble(instance, value != null ? value : 0.0);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(@Nullable final Double value, final HolderLookup.Provider registries) {
			return DoubleTag.valueOf(value != null ? value : 0.0);
		}

		@Override
		public Double deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return ((DoubleTag) nbt).getAsDouble();
		}
	}

	public static final class HandlerCharacter extends PrimitiveFieldValueHandler<Character> {

		HandlerCharacter() {
			super(Character.TYPE);
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
		public void setValue(final Field field, final Object instance, @Nullable final Character value) {
			try {
				field.setChar(instance, value != null ? value : 0);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Tag serialize(@Nullable final Character value, final HolderLookup.Provider registries) {
			return StringTag.valueOf(value != null ? String.valueOf(value) : "");
		}

		@Override
		public Character deserialize(final Tag nbt, final HolderLookup.Provider registries) {
			return nbt.getAsString().charAt(0);
		}
	}
}
