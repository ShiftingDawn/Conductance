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
import conductance.api.machine.data.ManagedFieldValueHandler;
import conductance.api.machine.data.ManagedFieldValueMutator;
import conductance.api.plugin.ManagedFieldValueHandlerRegister;

public final class ManagedFieldValueHandlerRegistry implements ManagedFieldValueHandlerRegister {

	public static final ManagedFieldValueHandlerRegistry INSTANCE = new ManagedFieldValueHandlerRegistry();
	private final Object2IntMap<ManagedFieldValueHandler<?>> unsortedHandlers = new Object2IntArrayMap<>();
	private final Object2IntMap<ManagedFieldValueMutator<?>> unsortedMutators = new Object2IntArrayMap<>();
	private final Map<Type, ManagedFieldValueHandler<?>> mapperCache = new IdentityHashMap<>();
	@Nullable
	private List<? extends ManagedFieldValueHandler<?>> sortedHandlers;
	@Nullable
	private List<? extends ManagedFieldValueMutator<?>> sortedMutators;

	private ManagedFieldValueHandlerRegistry() {
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

	@Override
	public void register(final ManagedFieldValueMutator<?> mutator, final int priority) {
		if (this.sortedMutators != null) {
			throw new IllegalStateException("Cannot register ManagedFieldValueMutator after registry has been frozen!");
		}
		if (this.unsortedMutators.containsKey(mutator)) {
			throw new IllegalStateException("ManagedFieldValueMutator %s has already been registered".formatted(mutator.getClass().getName()));
		}
		this.unsortedMutators.put(mutator, priority);
	}

	public void freeze() {
		this.sortedHandlers = this.unsortedHandlers.object2IntEntrySet().stream()
				.sorted((o1, o2) -> o2.getIntValue() - o1.getIntValue()) //Descending
				.map(Map.Entry::getKey)
				.toList();
		this.sortedMutators = this.unsortedMutators.object2IntEntrySet().stream()
				.sorted((o1, o2) -> o2.getIntValue() - o1.getIntValue()) //Descending
				.map(Map.Entry::getKey)
				.toList();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	<T> ManagedFieldValueHandler<T> getHandler(final Type clazz) {
		if (clazz instanceof final GenericArrayType array) {
			final Type contentsType = array.getGenericComponentType();
			final ManagedFieldValueHandler<Object> contentsHandler = this.getHandler(contentsType);
			final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(contentsType);
			return (ManagedFieldValueHandler<T>) ArrayFieldValueHandler.FACTORY.apply(contentsHandler, rawType != null ? rawType : Object.class);
		}
		final Class<?> rawType = ManagedFieldValueHandlerRegistry.getRawType(clazz);
		if (rawType == null) {
			throw new RuntimeException("Unknown type " + clazz);
		}
		if (rawType.isArray()) {
			final Class<?> contentsType = rawType.getComponentType();
			final ManagedFieldValueHandler<Object> contentsHandler = this.getHandler(contentsType);
			return (ManagedFieldValueHandler<T>) ArrayFieldValueHandler.FACTORY.apply(contentsHandler, contentsType);
		} else if (Collection.class.isAssignableFrom(rawType)) {
			final Type contentsType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
			final ManagedFieldValueHandler<Object> contentsHandler = this.getHandler(contentsType);
			final Class<?> rawContentsType = ManagedFieldValueHandlerRegistry.getRawType(contentsType);
			return (ManagedFieldValueHandler<T>) CollectionFieldValueHandler.FACTORY.apply(contentsHandler, rawContentsType != null ? rawContentsType : Object.class);
		}
		return this.getHandlerByClass(rawType);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	<T> ManagedFieldValueHandler<T> getHandlerByClass(final Class<?> clazz) {
		assert this.sortedHandlers != null;
		return (ManagedFieldValueHandler<T>) this.mapperCache.computeIfAbsent(clazz, $ -> this.sortedHandlers.stream().filter(handler -> handler.canHandle(clazz)).findFirst().orElse(null));
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
