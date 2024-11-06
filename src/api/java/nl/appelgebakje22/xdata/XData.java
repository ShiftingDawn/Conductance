package nl.appelgebakje22.xdata;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.Pair;
import nl.appelgebakje22.xdata.api.Checker;
import nl.appelgebakje22.xdata.handlers.BooleanHandler;
import nl.appelgebakje22.xdata.handlers.ByteHandler;
import nl.appelgebakje22.xdata.handlers.CharHandler;
import nl.appelgebakje22.xdata.handlers.DoubleHandler;
import nl.appelgebakje22.xdata.handlers.EnumHandler;
import nl.appelgebakje22.xdata.handlers.FloatHandler;
import nl.appelgebakje22.xdata.handlers.IManagedHandler;
import nl.appelgebakje22.xdata.handlers.IntHandler;
import nl.appelgebakje22.xdata.handlers.LongHandler;
import nl.appelgebakje22.xdata.handlers.ShortHandler;
import nl.appelgebakje22.xdata.serializers.ArraySerializer;
import nl.appelgebakje22.xdata.serializers.BooleanSerializer;
import nl.appelgebakje22.xdata.serializers.ByteSerializer;
import nl.appelgebakje22.xdata.serializers.CharSerializer;
import nl.appelgebakje22.xdata.serializers.DoubleSerializer;
import nl.appelgebakje22.xdata.serializers.FloatSerializer;
import nl.appelgebakje22.xdata.serializers.IManagedSerializer;
import nl.appelgebakje22.xdata.serializers.IntSerializer;
import nl.appelgebakje22.xdata.serializers.LongSerializer;
import nl.appelgebakje22.xdata.serializers.ShortSerializer;
import nl.appelgebakje22.xdata.serializers.StringSerializer;
import nl.appelgebakje22.xdata.serializers.UUIDSerializer;
import static nl.appelgebakje22.xdata.XDataRegister.register;

public final class XData {

	public static void init(final Runnable registerCallback) {
		register(BooleanSerializer.class, BooleanSerializer::new, new BooleanHandler());
		register(ByteSerializer.class, ByteSerializer::new, new ByteHandler());
		register(ShortSerializer.class, ShortSerializer::new, new ShortHandler());
		register(IntSerializer.class, IntSerializer::new, new IntHandler());
		register(LongSerializer.class, LongSerializer::new, new LongHandler());
		register(FloatSerializer.class, FloatSerializer::new, new FloatHandler());
		register(DoubleSerializer.class, DoubleSerializer::new, new DoubleHandler());
		register(CharSerializer.class, CharSerializer::new, new CharHandler());

		register(ArraySerializer.class, ArraySerializer::new);

		register(StringSerializer.class, StringSerializer::new, String.class, true);
		register(UUIDSerializer.class, UUIDSerializer::new, UUID.class, true);
		//		register(AdapterSerializer.class, AdapterSerializer::new, BaseAdapter.class, false);

		register(StringSerializer.class, StringSerializer::new, new EnumHandler());
		register(IManagedSerializer.class, IManagedSerializer::new, new IManagedHandler());

		registerCallback.run();

		XDataRegister.freeze();
	}

	public static void init() {
		XData.init(() -> {
		});
	}

	public static <T> T make(final T obj, final Consumer<T> mod) {
		mod.accept(obj);
		return obj;
	}

	public static <A, B, Z> BiFunction<A, B, Z> memoize(final BiFunction<A, B, Z> memoBiFunction) {
		return new BiFunction<>() {

			private final Map<Pair<A, B>, Z> cache = new ConcurrentHashMap<>();

			@Override
			public Z apply(final A a, final B b) {
				return this.cache.computeIfAbsent(Pair.of(a, b), key -> memoBiFunction.apply(key.first(), key.second()));
			}

			@Override
			public String toString() {
				return "memoize/2[function=" + memoBiFunction + ", size=" + this.cache.size() + "]";
			}
		};
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static boolean isEqual(final Object value1, final Object value2) {
		if ((value1 == null) != (value2 == null)) {
			return false;
		}
		if (value1 == null) {
			return true;
		}
		if (!value1.getClass().isAssignableFrom(value2.getClass()) && !value2.getClass().isAssignableFrom(value1.getClass())) {
			return false;
		}
		final Checker checker1 = XDataRegister.getChecker(value1.getClass());
		final Checker checker2 = XDataRegister.getChecker(value2.getClass());
		if (checker1 != checker2) {
			return false;
		}
		return checker1 == null ? Objects.equals(value1, value2) : checker1.equals(value1, value2) && checker2.equals(value1, value2);
	}
}
