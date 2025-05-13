package conductance.core.sync;

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
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Checker;
import conductance.api.machine.sync.Copier;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.api.plugin.SyncFieldSerializerRegister;
import conductance.core.sync.handlers.ArrayHandler;
import conductance.core.sync.handlers.CollectionHandler;
import conductance.core.sync.handlers.SimpleObjectHandler;
import conductance.core.sync.serializers.ArraySerializer;

public final class SyncFieldSerializerRegisterImpl implements SyncFieldSerializerRegister {

	public static final SyncFieldSerializerRegisterImpl INSTANCE = new SyncFieldSerializerRegisterImpl();
	private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

	private final Object2IntMap<Class<?>> serializerRegistry = new Object2IntArrayMap<>();
	private final Int2ObjectMap<Supplier<? extends Serializer<?>>> serializerFactories = new Int2ObjectArrayMap<>();
	private final AtomicInteger serializerIdHolder = new AtomicInteger(10); //Reserve a couple for the future

	private final Object2IntMap<ReferenceHandler> handlersUnsorted = new Object2IntArrayMap<>();
	private final List<ReferenceHandler> handlersSorted = new ArrayList<>();
	private final Object2IntMap<ReferenceHandler> handlerToSerializerMapping = new Object2IntArrayMap<>();
	private final Object2ObjectMap<Class<?>, ReferenceHandler> handlerTypeCache = new Object2ObjectArrayMap<>();

	private final List<Copier<?>> copiers = new ArrayList<>();
	private final Object2ObjectMap<Class<?>, Copier<?>> copierTypeCache = new Object2ObjectArrayMap<>();
	private final List<Checker<?>> checkers = new ArrayList<>();
	private final Object2ObjectMap<Class<?>, Checker<?>> checkerTypeCache = new Object2ObjectArrayMap<>();

	private SyncFieldSerializerRegisterImpl() {
	}

	@Override
	public <T extends Serializer<?>> int register(final Class<T> serializerType, final Supplier<T> factory) {
		if (SyncFieldSerializerRegisterImpl.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Serializer.class.getName(), serializerType.getName()));
		}
		int id = this.serializerRegistry.getOrDefault(serializerType, -1);
		if (id == -1) {
			id = this.serializerIdHolder.getAndIncrement();
			this.serializerRegistry.put(serializerType, id);
			this.serializerFactories.put(id, factory);
		}
		return id;
	}

	@Override
	public <T extends Serializer<?>> void register(final Class<T> serializerType, final Supplier<T> factory, final ReferenceHandler handler, final int priority) {
		if (SyncFieldSerializerRegisterImpl.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(ReferenceHandler.class.getName(), handler.getClass().getName()));
		}
		final int serializerId = this.register(serializerType, factory);
		this.handlersUnsorted.put(handler, priority);
		this.handlerToSerializerMapping.put(handler, serializerId);
	}

	@Override
	public <T, S extends Serializer<T>> void register(final Class<S> serializerType, final Supplier<S> factory, final Class<T> valueType, final boolean shallowEqualityCheck, final int priority) {
		this.register(serializerType, factory, new SimpleObjectHandler(valueType, shallowEqualityCheck, factory), priority);
	}

	@Override
	public void register(final Copier<?> copier) {
		if (SyncFieldSerializerRegisterImpl.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Copier.class.getName(), copier.getClass().getName()));
		}
		this.copiers.add(copier);
	}

	@Override
	public void register(final Checker<?> checker) {
		if (SyncFieldSerializerRegisterImpl.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Checker.class.getName(), checker.getClass().getName()));
		}
		this.checkers.add(checker);
	}

	public void freeze() {
		this.handlersSorted.addAll(this.handlersUnsorted.object2IntEntrySet().stream().sorted((o1, o2) -> Integer.compare(o2.getIntValue(), o1.getIntValue())).map(Map.Entry::getKey).toList());
		SyncFieldSerializerRegisterImpl.INITIALIZED.set(true);
	}

	public boolean canHandleType(final Type type) {
		return this.getHandler(type) != null;
	}

	public int getSerializerId(final Serializer<?> serializer) {
		return this.serializerRegistry.getInt(serializer.getClass());
	}

	public Serializer<?> getSerializerById(final int id) {
		return this.serializerFactories.get(id).get();
	}

	public Serializer<?> getSerializerByHandler(final ReferenceHandler handler) {
		if (handler instanceof ArrayHandler || handler instanceof CollectionHandler) {
			return new ArraySerializer();
		}
		final int id = this.handlerToSerializerMapping.getInt(handler);
		return this.getSerializerById(id);
	}

	@Nullable
	public ReferenceHandler getHandler(final Type clazz) {
		if (clazz instanceof final GenericArrayType array) {
			final Type contentType = array.getGenericComponentType();
			final ReferenceHandler contentHandler = this.getHandler(contentType);
			final Class<?> rawType = this.getRawType(contentType);
			return ArrayHandler.FACTORY.apply(contentHandler, rawType == null ? Object.class : rawType);
		}
		final Class<?> rawType = this.getRawType(clazz);
		if (rawType != null) {
			if (rawType.isArray()) {
				final Class<?> contentType = rawType.getComponentType();
				final ReferenceHandler contentHandler = this.getHandler(contentType);
				return ArrayHandler.FACTORY.apply(contentHandler, contentType);
			}
			if (Collection.class.isAssignableFrom(rawType)) {
				final Type contentType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
				final ReferenceHandler contentHandler = this.getHandler(contentType);
				final Class<?> rawContentType = this.getRawType(contentType);
				return CollectionHandler.FACTORY.apply(contentHandler, rawContentType == null ? Object.class : rawContentType);
			}
			return this.getHandlerByClass(rawType);
		}
		return null;
	}

	@Nullable
	private ReferenceHandler getHandlerByClass(final Class<?> clazz) {
		if (!SyncFieldSerializerRegisterImpl.INITIALIZED.get()) {
			throw new IllegalStateException("Cannot fetch %s during initialization!".formatted(ReferenceHandler.class.getName()));
		}
		return this.handlerTypeCache.computeIfAbsent(clazz, ignored -> this.handlersSorted.stream().filter(entry -> entry.canHandle(clazz)).findFirst().orElse(null));
	}

	public Class<?> getRawType(final Type type) {
		return switch (type) {
			case final Class<?> aClass -> aClass;
			case final GenericArrayType genericArrayType -> this.getRawType(genericArrayType.getGenericComponentType());
			case final ParameterizedType parameterizedType -> this.getRawType(parameterizedType.getRawType());
			case null, default -> null;
		};
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T> Copier<T> getCopier(final Class<T> clazz) {
		return (Copier<T>) this.copierTypeCache.computeIfAbsent(clazz, ignored -> this.copiers.stream().filter(copier -> copier.canHandle(clazz)).findFirst().orElse(null));
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T> Checker<T> getChecker(final Class<T> clazz) {
		return (Checker<T>) this.checkerTypeCache.computeIfAbsent(clazz, ignored -> this.checkers.stream().filter(checker -> checker.canHandle(clazz)).findFirst().orElse(null));
	}
}
