package conductance.core.sync;

import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.sync.Operation;
import conductance.api.sync.Reference;
import conductance.api.sync.ReferenceHandler;
import conductance.api.sync.Serializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
abstract class PrimitiveHandler<T> implements ReferenceHandler {

	private final Class<T> cls1;
	@Nullable
	private final Class<T> cls2;
	private final Class<? extends PrimitiveCodecSerializer<T>> serializerType;
	private final Function<Object, Serializer<T>> reader;

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return this.cls1.equals(clazz) || (this.cls2 != null && this.cls2.equals(clazz));
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.reader.apply(ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer, final HolderLookup.Provider registries) {
		final PrimitiveCodecSerializer<T> serializer = this.testSerializer(rawSerializer, this.serializerType);
		ref.getValueHolder().set(serializer.getData());
	}

	public static final class BooleanHandler extends PrimitiveHandler<Boolean> {

		BooleanHandler() {
			super(boolean.class, Boolean.class, PrimitiveCodecSerializer.BooleanSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.BooleanSerializer(), s -> s.setData((boolean) val)));
		}
	}

	public static final class ByteHandler extends PrimitiveHandler<Byte> {

		ByteHandler() {
			super(byte.class, Byte.class, PrimitiveCodecSerializer.ByteSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.ByteSerializer(), s -> s.setData((byte) val)));
		}
	}

	public static final class ShortHandler extends PrimitiveHandler<Short> {

		ShortHandler() {
			super(short.class, Short.class, PrimitiveCodecSerializer.ShortSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.ShortSerializer(), s -> s.setData((short) val)));
		}
	}

	public static final class IntHandler extends PrimitiveHandler<Integer> {

		IntHandler() {
			super(int.class, Integer.class, PrimitiveCodecSerializer.IntSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.IntSerializer(), s -> s.setData((int) val)));
		}
	}

	public static final class LongHandler extends PrimitiveHandler<Long> {

		LongHandler() {
			super(long.class, Long.class, PrimitiveCodecSerializer.LongSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.LongSerializer(), s -> s.setData((long) val)));
		}
	}

	public static final class FloatHandler extends PrimitiveHandler<Float> {

		FloatHandler() {
			super(float.class, Float.class, PrimitiveCodecSerializer.FloatSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.FloatSerializer(), s -> s.setData((float) val)));
		}
	}

	public static final class DoubleHandler extends PrimitiveHandler<Double> {

		DoubleHandler() {
			super(double.class, Double.class, PrimitiveCodecSerializer.DoubleSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.DoubleSerializer(), s -> s.setData((double) val)));
		}
	}

	public static final class CharHandler extends PrimitiveHandler<Character> {

		CharHandler() {
			super(char.class, Character.class, PrimitiveCodecSerializer.CharSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.CharSerializer(), s -> s.setData((char) val)));
		}
	}

	public static final class StringHandler extends PrimitiveHandler<String> {

		StringHandler() {
			super(String.class, null, PrimitiveCodecSerializer.StringSerializer.class,
					val -> Util.make(new PrimitiveCodecSerializer.StringSerializer(), s -> s.setData((String) val)));
		}
	}
}
