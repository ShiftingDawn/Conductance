package conductance.core.data;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.plugin.ManagedFieldValueHandlerRegister;

public final class ManagedFieldValueHandlerRegistry implements ManagedFieldValueHandlerRegister {

	public static final ManagedFieldValueHandlerRegistry INSTANCE = new ManagedFieldValueHandlerRegistry();
	//Serializer
	private final Object2IntMap<Class<?>> serializerIds = new Object2IntArrayMap<>();
	private final AtomicInteger nextSerializerID = new AtomicInteger(1); //Reserve zero for future use
	private final Int2ObjectMap<Supplier<? extends DataSerializer<?>>> serializerConstructors = new Int2ObjectArrayMap<>();
	//Handler
	private final Object2IntMap<ManagedFieldValueHandler> handlerToSerializerMapping = new Object2IntArrayMap<>();
	private final Object2IntMap<ManagedFieldValueHandler> unsortedHandlers = new Object2IntArrayMap<>();
	private final Map<Type, ManagedFieldValueHandler> handlerCache = new IdentityHashMap<>();
	@Nullable
	private List<? extends ManagedFieldValueHandler> sortedHandlers;

	private ManagedFieldValueHandlerRegistry() {
	}

	private <T extends DataSerializer<?>> int addSerializerIfMissing(final Class<T> clazz, final Supplier<T> constructor) {
		int id = this.serializerIds.getOrDefault(clazz, -1);
		if (id == -1) {
			id = this.nextSerializerID.getAndIncrement();
			this.serializerIds.put(clazz, id);
			this.serializerConstructors.put(id, constructor);
		}
		return id;
	}

	@Override
	public <T extends DataSerializer<?>> void register(final Class<T> clazz, final Supplier<T> constructor, final ManagedFieldValueHandler handler, final int priority) {
		if (this.sortedHandlers != null) {
			throw new IllegalStateException("Cannot register ManagedFieldValueHandler after registry has been frozen!");
		}
		if (this.unsortedHandlers.containsKey(handler)) {
			throw new IllegalStateException("ManagedFieldValueHandler %s has already been registered".formatted(handler.getClass().getName()));
		}
		final int id = this.addSerializerIfMissing(clazz, constructor);
		this.unsortedHandlers.put(handler, priority);
		this.handlerToSerializerMapping.put(handler, id);
	}

	public void freeze() {
		this.sortedHandlers = this.unsortedHandlers.object2IntEntrySet().stream()
				.sorted((o1, o2) -> o2.getIntValue() - o1.getIntValue()) //Descending
				.map(Map.Entry::getKey)
				.toList();
	}

	@Nullable
	ManagedFieldValueHandler getHandler(final Type clazz, final ManagedFieldWrapper managedFieldWrapper) {
//		if (clazz instanceof final GenericArrayType array) {
//			final Type contentsType = array.getGenericComponentType();
//			final ManagedFieldValueHandler contentHandler = this.getHandler(contentsType, managedFieldWrapper);
//			final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(contentsType);
//			return (ManagedFieldValueHandler) ArrayFieldValueHandler.FACTORY.apply(contentHandler, rawType != null ? rawType : Object.class);
//		}
		final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(clazz);
		if (rawType == null) {
			throw new RuntimeException("Unknown type " + clazz);
		}
//		if (rawType.isArray()) {
//			final Class<?> contentType = rawType.getComponentType();
//			final ManagedFieldValueHandler contentHandler = this.getHandler(contentType, managedFieldWrapper);
//			return (ManagedFieldValueHandler) ArrayFieldValueHandler.FACTORY.apply(contentHandler, contentType);
//		} else if (Collection.class.isAssignableFrom(rawType)) {
//			final Type contentType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
//			final ManagedFieldValueHandler contentHandler = this.getHandler(contentType, managedFieldWrapper);
//			final Class<?> rawContentType = ManagedFieldValueHandlerRegistry.getRawType(contentType);
//			return (ManagedFieldValueHandler) CollectionFieldValueHandler.FACTORY.apply(contentHandler, rawContentType != null ? rawContentType : Object.class);
//		}
		return this.getHandlerByClass(rawType);
	}

	@Nullable
	ManagedFieldValueHandler getHandlerByClass(final Class<?> clazz) {
		assert this.sortedHandlers != null;
		return this.handlerCache.computeIfAbsent(clazz, $ -> this.sortedHandlers.stream().filter(handler -> handler.canHandle(clazz)).findFirst().orElse(null));
	}

	int getSerializerForHandler(final ManagedFieldValueHandler handler) {
		return this.handlerToSerializerMapping.getOrDefault(handler, -1);
	}

	DataSerializer<?> createSerializer(final int serializerId) {
		final Supplier<? extends DataSerializer<?>> constructor = this.serializerConstructors.get(serializerId);
		if (constructor == null) {
			throw new IllegalArgumentException("No %s registered with id %s".formatted(DataSerializer.class.getName(), serializerId));
		}
		return constructor.get();
	}

	@Nullable
	private static Class<?> getRawType(final Type type) {
		return switch (type) {
			case final Class<?> aClass -> aClass;
			case final GenericArrayType genericArrayType -> ManagedFieldValueHandlerRegistry.getRawType(genericArrayType.getGenericComponentType());
			case final ParameterizedType parameterizedType -> ManagedFieldValueHandlerRegistry.getRawType(parameterizedType.getRawType());
			default -> null;
		};
	}
}
