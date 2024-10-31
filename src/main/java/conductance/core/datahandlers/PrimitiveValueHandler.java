package conductance.core.datahandlers;

import net.minecraft.core.HolderLookup;
import com.google.common.primitives.Primitives;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.PrimitiveDataSerializer;

public abstract class PrimitiveValueHandler implements ManagedFieldValueHandler {

	private final Class<?> typeClass;

	PrimitiveValueHandler(final Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type == this.typeClass || type == Primitives.wrap(this.typeClass);
	}

	public static class BooleanHandler extends PrimitiveValueHandler {

		public BooleanHandler() {
			super(boolean.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.BooleanSerializer)) {
				throw new IllegalStateException("Field does not hold a boolean!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.BooleanSerializer booleanSerializer)) {
				throw new IllegalStateException("Expected a Boolean serializer, got: " + value.getClass().getName());
			}
			field.set(booleanSerializer.getData());
		}
	}

	public static class ByteHandler extends PrimitiveValueHandler {

		public ByteHandler() {
			super(byte.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.ByteSerializer)) {
				throw new IllegalStateException("Field does not hold a byte!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.ByteSerializer byteSerializer)) {
				throw new IllegalStateException("Expected a Byte serializer, got: " + value.getClass().getName());
			}
			field.set(byteSerializer.getData());
		}
	}

	public static class ShortHandler extends PrimitiveValueHandler {

		public ShortHandler() {
			super(short.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.ShortSerializer)) {
				throw new IllegalStateException("Field does not hold a short!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.ShortSerializer shortSerializer)) {
				throw new IllegalStateException("Expected a Short serializer, got: " + value.getClass().getName());
			}
			field.set(shortSerializer.getData());
		}
	}

	public static class IntegerHandler extends PrimitiveValueHandler {

		public IntegerHandler() {
			super(int.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.IntegerSerializer)) {
				throw new IllegalStateException("Field does not hold an integer!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.IntegerSerializer integerSerializer)) {
				throw new IllegalStateException("Expected a Integer serializer, got: " + value.getClass().getName());
			}
			field.set(integerSerializer.getData());
		}
	}

	public static class LongHandler extends PrimitiveValueHandler {

		public LongHandler() {
			super(long.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.LongSerializer)) {
				throw new IllegalStateException("Field does not hold a long!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.LongSerializer longSerializer)) {
				throw new IllegalStateException("Expected a Long serializer, got: " + value.getClass().getName());
			}
			field.set(longSerializer.getData());
		}
	}

	public static class FloatHandler extends PrimitiveValueHandler {

		public FloatHandler() {
			super(float.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.FloatSerializer)) {
				throw new IllegalStateException("Field does not hold a float!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.FloatSerializer floatSerializer)) {
				throw new IllegalStateException("Expected a Float serializer, got: " + value.getClass().getName());
			}
			field.set(floatSerializer.getData());
		}
	}

	public static class DoubleHandler extends PrimitiveValueHandler {

		public DoubleHandler() {
			super(double.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.DoubleSerializer)) {
				throw new IllegalStateException("Field does not hold a double!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.DoubleSerializer doubleSerializer)) {
				throw new IllegalStateException("Expected a Double serializer, got: " + value.getClass().getName());
			}
			field.set(doubleSerializer.getData());
		}
	}

	public static class CharacterHandler extends PrimitiveValueHandler {

		public CharacterHandler() {
			super(char.class);
		}

		@Override
		public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
			final DataSerializer<?> result = PrimitiveDataSerializer.tryGetPrimitiveSerializer(field.get());
			if (!(result instanceof PrimitiveDataSerializer.CharacterSerializer)) {
				throw new IllegalStateException("Field does not hold a character!");
			}
			return result;
		}

		@Override
		public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
			if (!(value instanceof final PrimitiveDataSerializer.CharacterSerializer characterSerializer)) {
				throw new IllegalStateException("Expected a Character serializer, got: " + value.getClass().getName());
			}
			field.set(characterSerializer.getData());
		}
	}
}
