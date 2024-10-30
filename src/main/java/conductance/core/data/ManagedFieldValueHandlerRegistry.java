package conductance.core.data;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.DataSerializer;
import conductance.api.machine.data.ManagedFieldValueHandler;
import conductance.api.plugin.ManagedFieldValueHandlerRegister;

public final class ManagedFieldValueHandlerRegistry implements ManagedFieldValueHandlerRegister {

	public static final ManagedFieldValueHandlerRegistry INSTANCE = new ManagedFieldValueHandlerRegistry();
	private final Object2IntMap<DataSerializer<?>> unsortedSerializers = new Object2IntArrayMap<>();
	private final Object2IntMap<ManagedFieldValueHandler<?>> unsortedHandlers = new Object2IntArrayMap<>();
	private final Map<Type, DataSerializer<?>> serializerCache = new IdentityHashMap<>();
	private final Map<Type, ManagedFieldValueHandler<?>> handlerCache = new IdentityHashMap<>();
	@Nullable
	private List<? extends DataSerializer<?>> sortedSerializers;
	@Nullable
	private List<? extends ManagedFieldValueHandler<?>> sortedHandlers;

	private ManagedFieldValueHandlerRegistry() {
	}

	@Override
	public void register(final DataSerializer<?> serializer, final int priority) {
		if (this.sortedSerializers != null) {
			throw new IllegalStateException("Cannot register DataSerializer after registry has been frozen!");
		}
		if (this.unsortedSerializers.containsKey(serializer)) {
			throw new IllegalStateException("DataSerializer %s has already been registered".formatted(serializer.getClass().getName()));
		}
		this.unsortedSerializers.put(serializer, priority);
	}

	@Override
	public void register(final ManagedFieldValueHandler<?> handler, final int priority) {
		if (this.sortedHandlers != null) {
			throw new IllegalStateException("Cannot register ManagedFieldValueHandler after registry has been frozen!");
		}
		if (this.unsortedHandlers.containsKey(handler)) {
			throw new IllegalStateException("ManagedFieldValueHandler %s has already been registered".formatted(handler.getClass().getName()));
		}
		this.unsortedHandlers.put(handler, priority);
	}

	public void freeze() {
		this.sortedHandlers = this.unsortedHandlers.object2IntEntrySet().stream()
				.sorted((o1, o2) -> o2.getIntValue() - o1.getIntValue()) //Descending
				.map(Map.Entry::getKey)
				.toList();
		this.sortedSerializers = this.unsortedSerializers.object2IntEntrySet().stream()
				.sorted((o1, o2) -> o2.getIntValue() - o1.getIntValue()) //Descending
				.map(Map.Entry::getKey)
				.toList();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	<T> DataSerializer<T> getSerializer(final Type clazz) {
		if (clazz instanceof final GenericArrayType array) {
			final Type contentType = array.getGenericComponentType();
			final DataSerializer<Object> contentSerializer = this.getSerializer(contentType);
			final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(contentType);
			return (DataSerializer<T>) ArrayDataSerializer.FACTORY.apply(contentSerializer, rawType != null ? rawType : Object.class);
		}
		final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(clazz);
		if (rawType == null) {
			throw new RuntimeException("Unknown type " + clazz);
		}
		if (rawType.isArray()) {
			final Class<?> contentsType = rawType.getComponentType();
			final DataSerializer<Object> contentSerializer = this.getSerializer(contentsType);
			return (DataSerializer<T>) ArrayDataSerializer.FACTORY.apply(contentSerializer, contentsType);
		} else if (Collection.class.isAssignableFrom(rawType)) {
			final Type contentType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
			final DataSerializer<Object> contentSerializer = this.getSerializer(contentType);
			final Class<?> rawContentType = ManagedFieldValueHandlerRegistry.getRawType(contentType);
			return (DataSerializer<T>) CollectionDataSerializer.FACTORY.apply(contentSerializer, rawContentType != null ? rawContentType : Object.class);
		}
		return this.getSerializerByClass(rawType);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	<T> DataSerializer<T> getSerializerByClass(final Class<?> clazz) {
		assert this.sortedSerializers != null;
		return (DataSerializer<T>) this.serializerCache.computeIfAbsent(clazz, $ -> this.sortedSerializers.stream().filter(serializer -> serializer.canHandle(clazz)).findFirst().orElse(null));
	}

	@SuppressWarnings("unchecked")
	@Nullable
	<T> ManagedFieldValueHandler<T> getHandler(final Type clazz, final ManagedFieldWrapper managedFieldWrapper) {
		if (clazz instanceof final GenericArrayType array) {
			final Type contentsType = array.getGenericComponentType();
			final ManagedFieldValueHandler<Object> contentHandler = this.getHandler(contentsType, managedFieldWrapper);
			final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(contentsType);
			return (ManagedFieldValueHandler<T>) ArrayFieldValueHandler.FACTORY.apply(contentHandler, rawType != null ? rawType : Object.class);
		}
		final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(clazz);
		if (rawType == null) {
			throw new RuntimeException("Unknown type " + clazz);
		}
		if (rawType.isArray()) {
			final Class<?> contentType = rawType.getComponentType();
			final ManagedFieldValueHandler<Object> contentHandler = this.getHandler(contentType, managedFieldWrapper);
			return (ManagedFieldValueHandler<T>) ArrayFieldValueHandler.FACTORY.apply(contentHandler, contentType);
		} else if (Collection.class.isAssignableFrom(rawType)) {
			final Type contentType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
			final ManagedFieldValueHandler<Object> contentHandler = this.getHandler(contentType, managedFieldWrapper);
			final Class<?> rawContentType = ManagedFieldValueHandlerRegistry.getRawType(contentType);
			return (ManagedFieldValueHandler<T>) CollectionFieldValueHandler.FACTORY.apply(contentHandler, rawContentType != null ? rawContentType : Object.class);
		}
		return this.getHandlerByClass(rawType);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	<T> ManagedFieldValueHandler<T> getHandlerByClass(final Class<?> clazz) {
		assert this.sortedHandlers != null;
		return (ManagedFieldValueHandler<T>) this.handlerCache.computeIfAbsent(clazz, $ -> this.sortedHandlers.stream().filter(handler -> handler.canHandle(clazz)).findFirst().orElse(null));
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
