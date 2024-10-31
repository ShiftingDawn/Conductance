package conductance.core.data;

import net.minecraft.Util;
import com.google.common.primitives.Primitives;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.PrimitiveDataSerializer;

abstract class PrimitiveValueHandler implements ManagedFieldValueHandler {

	private final Class<?> typeClass;

	PrimitiveValueHandler(final Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type == this.typeClass || type == Primitives.wrap(this.typeClass);
	}

	public static class BooleanHandler extends PrimitiveValueHandler {

		BooleanHandler() {
			super(boolean.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final boolean result = field.getField().getBoolean(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.BooleanSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.BooleanSerializer booleanSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setBoolean(field.getInstance(), booleanSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class ByteHandler extends PrimitiveValueHandler {

		ByteHandler() {
			super(byte.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final byte result = field.getField().getByte(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.ByteSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.ByteSerializer byteSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setByte(field.getInstance(), byteSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class ShortHandler extends PrimitiveValueHandler {

		ShortHandler() {
			super(short.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final short result = field.getField().getShort(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.ShortSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.ShortSerializer shortSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setShort(field.getInstance(), shortSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class IntegerHandler extends PrimitiveValueHandler {

		IntegerHandler() {
			super(int.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final int result = field.getField().getInt(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.IntegerSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.IntegerSerializer integerSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setInt(field.getInstance(), integerSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class LongHandler extends PrimitiveValueHandler {

		LongHandler() {
			super(long.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final long result = field.getField().getLong(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.LongSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.LongSerializer longSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setLong(field.getInstance(), longSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class FloatHandler extends PrimitiveValueHandler {

		FloatHandler() {
			super(float.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final float result = field.getField().getFloat(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.FloatSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.FloatSerializer floatSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setFloat(field.getInstance(), floatSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class DoubleHandler extends PrimitiveValueHandler {

		DoubleHandler() {
			super(double.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final double result = field.getField().getDouble(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.DoubleSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.DoubleSerializer doubleSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setDouble(field.getInstance(), doubleSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class CharacterHandler extends PrimitiveValueHandler {

		CharacterHandler() {
			super(char.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field) {
			try {
				final char result = field.getField().getChar(field.getInstance());
				return Util.make(new PrimitiveDataSerializer.CharacterSerializer(), s -> s.setData(result));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value) {
			if (!(value instanceof final PrimitiveDataSerializer.CharacterSerializer charSerializer)) {
				throw new IllegalStateException("Invalid serializer");
			}
			try {
				field.getField().setChar(field.getInstance(), charSerializer.getData());
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
