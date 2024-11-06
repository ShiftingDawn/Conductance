package nl.appelgebakje22.xdata;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import nl.appelgebakje22.xdata.api.Checker;
import nl.appelgebakje22.xdata.api.Copier;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.handlers.ArrayHandler;
import nl.appelgebakje22.xdata.handlers.CollectionHandler;
import nl.appelgebakje22.xdata.handlers.SimpleObjectHandler;
import nl.appelgebakje22.xdata.serializers.ArraySerializer;
import org.jetbrains.annotations.Nullable;

public final class XDataRegister {

	private static final int DEFAULT_PRIORITY = 1000;
	private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

	private static final Object2IntMap<Class<?>> SERIALIZER_REGISTRY = new Object2IntArrayMap<>();
	private static final Int2ObjectMap<Supplier<? extends Serializer<?>>> SERIALIZER_FACTORIES = new Int2ObjectArrayMap<>();
	private static final AtomicInteger SERIALIZER_ID_HOLDER = new AtomicInteger(10); //Reserve a couple for the future

	private static final Object2IntMap<ReferenceHandler> HANDLERS_UNSORTED = new Object2IntArrayMap<>();
	private static final List<ReferenceHandler> HANDLERS_SORTED = new ArrayList<>();
	private static final Object2IntMap<ReferenceHandler> HANDLER_TO_SERIALIZER_MAPPING = new Object2IntArrayMap<>();
	private static final Object2ObjectMap<Class<?>, ReferenceHandler> HANDLER_TYPE_CACHE = new Object2ObjectArrayMap<>();

	private static final List<Copier<?>> COPIERS = new ArrayList<>();
	private static final Object2ObjectMap<Class<?>, Copier<?>> COPIER_TYPE_CACHE = new Object2ObjectArrayMap<>();
	private static final List<Checker<?>> CHECKERS = new ArrayList<>();
	private static final Object2ObjectMap<Class<?>, Checker<?>> CHECKER_TYPE_CACHE = new Object2ObjectArrayMap<>();

	public static <T extends Serializer<?>> int register(final Class<T> serializerType, final Supplier<T> factory) {
		if (XDataRegister.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Serializer.class.getName(), serializerType.getName()));
		}
		int id = XDataRegister.SERIALIZER_REGISTRY.getOrDefault(serializerType, -1);
		if (id == -1) {
			id = XDataRegister.SERIALIZER_ID_HOLDER.getAndIncrement();
			XDataRegister.SERIALIZER_REGISTRY.put(serializerType, id);
			XDataRegister.SERIALIZER_FACTORIES.put(id, factory);
		}
		return id;
	}

	public static <T extends Serializer<?>> void register(final Class<T> serializerType, final Supplier<T> factory, final ReferenceHandler handler, final int priority) {
		if (XDataRegister.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(ReferenceHandler.class.getName(), handler.getClass().getName()));
		}
		final int serializerId = XDataRegister.register(serializerType, factory);
		XDataRegister.HANDLERS_UNSORTED.put(handler, priority);
		XDataRegister.HANDLER_TO_SERIALIZER_MAPPING.put(handler, serializerId);
	}

	public static <T extends Serializer<?>> void register(final Class<T> serializerType, final Supplier<T> factory, final ReferenceHandler handler) {
		XDataRegister.register(serializerType, factory, handler, XDataRegister.DEFAULT_PRIORITY);
	}

	public static <T, S extends Serializer<T>> void register(final Class<S> serializerType, final Supplier<S> factory, final Class<T> valueType, final boolean shallowEqualityCheck, final int priority) {
		XDataRegister.register(serializerType, factory, new SimpleObjectHandler(valueType, shallowEqualityCheck, factory), priority);
	}

	public static <T, S extends Serializer<T>> void register(final Class<S> serializerType, final Supplier<S> factory, final Class<T> valueType, final boolean shallowEqualityCheck) {
		XDataRegister.register(serializerType, factory, valueType, shallowEqualityCheck, XDataRegister.DEFAULT_PRIORITY);
	}

	public static void register(final Copier<?> copier) {
		if (XDataRegister.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Copier.class.getName(), copier.getClass().getName()));
		}
		XDataRegister.COPIERS.add(copier);
	}

	public static void register(final Checker<?> checker) {
		if (XDataRegister.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Checker.class.getName(), checker.getClass().getName()));
		}
		XDataRegister.CHECKERS.add(checker);
	}

	static void freeze() {
		XDataRegister.HANDLERS_SORTED.addAll(XDataRegister.HANDLERS_UNSORTED.object2IntEntrySet().stream().sorted((o1, o2) -> Integer.compare(o2.getIntValue(), o1.getIntValue())).map(Map.Entry::getKey).toList());
		XDataRegister.INITIALIZED.set(true);
	}

	public static boolean canHandleType(final Type type) {
		return XDataRegister.getHandler(type) != null;
	}

	public static int getSerializerId(final Serializer<?> serializer) {
		return XDataRegister.SERIALIZER_REGISTRY.getInt(serializer.getClass());
	}

	public static Serializer<?> getSerializerById(final int id) {
		return XDataRegister.SERIALIZER_FACTORIES.get(id).get();
	}

	public static Serializer<?> getSerializerByHandler(final ReferenceHandler handler) {
		if (handler instanceof ArrayHandler || handler instanceof CollectionHandler) {
			return new ArraySerializer();
		}
		final int id = XDataRegister.HANDLER_TO_SERIALIZER_MAPPING.getInt(handler);
		return XDataRegister.getSerializerById(id);
	}

	@Nullable
	public static ReferenceHandler getHandler(final Type clazz) {
		if (clazz instanceof final GenericArrayType array) {
			final Type contentType = array.getGenericComponentType();
			final ReferenceHandler contentHandler = XDataRegister.getHandler(contentType);
			if (contentHandler == null) {
				return null;
			}
			final Class<?> rawType = XDataRegister.getRawType(contentType);
			return ArrayHandler.FACTORY.apply(contentHandler, rawType == null ? Object.class : rawType);
		}
		final Class<?> rawType = XDataRegister.getRawType(clazz);
		if (rawType != null) {
			if (rawType.isArray()) {
				final Class<?> contentType = rawType.getComponentType();
				final ReferenceHandler contentHandler = XDataRegister.getHandler(contentType);
				if (contentHandler == null) {
					return null;
				}
				return ArrayHandler.FACTORY.apply(contentHandler, contentType);
			}
			if (Collection.class.isAssignableFrom(rawType)) {
				final Type contentType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
				final ReferenceHandler contentHandler = XDataRegister.getHandler(contentType);
				if (contentHandler == null) {
					return null;
				}
				final Class<?> rawContentType = XDataRegister.getRawType(contentType);
				return CollectionHandler.FACTORY.apply(contentHandler, rawContentType == null ? Object.class : rawContentType);
			}
			return XDataRegister.getHandlerByClass(rawType);
		}
		return null;
	}

	@Nullable
	private static ReferenceHandler getHandlerByClass(final Class<?> clazz) {
		if (!XDataRegister.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot fetch %s during initialization!".formatted(ReferenceHandler.class.getName()));
		}
		return XDataRegister.HANDLER_TYPE_CACHE.computeIfAbsent(clazz, ignored -> XDataRegister.HANDLERS_SORTED.stream().filter(entry -> entry.canHandle(clazz)).findFirst().orElse(null));
	}

	public static Class<?> getRawType(final Type type) {
		return switch (type) {
			case final Class<?> aClass -> aClass;
			case final GenericArrayType genericArrayType -> XDataRegister.getRawType(genericArrayType.getGenericComponentType());
			case final ParameterizedType parameterizedType -> XDataRegister.getRawType(parameterizedType.getRawType());
			case null, default -> null;
		};
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Copier<T> getCopier(final Class<T> clazz) {
		return (Copier<T>) XDataRegister.COPIER_TYPE_CACHE.computeIfAbsent(clazz, ignored -> XDataRegister.COPIERS.stream().filter(copier -> copier.canHandle(clazz)).findFirst().orElse(null));
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Checker<T> getChecker(final Class<T> clazz) {
		return (Checker<T>) XDataRegister.CHECKER_TYPE_CACHE.computeIfAbsent(clazz, ignored -> XDataRegister.CHECKERS.stream().filter(checker -> checker.canHandle(clazz)).findFirst().orElse(null));
	}
}
