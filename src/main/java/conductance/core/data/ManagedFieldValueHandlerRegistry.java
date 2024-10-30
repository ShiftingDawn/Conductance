package conductance.core.data;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

public final class ManagedFieldValueHandlerRegistry {

	public static final ManagedFieldValueHandlerRegistry INSTANCE = new ManagedFieldValueHandlerRegistry();
	private final Object2IntMap<ManagedFieldValueHandler<?>> unsortedRegistry = new Object2IntArrayMap<>();
	private final Map<Class<?>, ManagedFieldValueHandler<?>> mapperCache = new IdentityHashMap<>();
	@Nullable
	private List<? extends ManagedFieldValueHandler<?>> sortedRegistry;

	private ManagedFieldValueHandlerRegistry() {
	}

	public void register(final ManagedFieldValueHandler<?> handler, final int priority) {
		if (this.sortedRegistry != null) {
			throw new IllegalStateException("Cannot register ManagedFieldValueHandler after registry has been frozen!");
		}
		if (this.unsortedRegistry.containsKey(handler)) {
			throw new IllegalStateException("ManagedFieldValueHandler %s has already been registered".formatted(handler.getClass().getName()));
		}
		this.unsortedRegistry.put(handler, priority);
	}

	public void freeze() {
		this.sortedRegistry = this.unsortedRegistry.object2IntEntrySet().stream()
				.sorted((o1, o2) -> o2.getIntValue() - o1.getIntValue()) //Descending
				.map(Map.Entry::getKey)
				.toList();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	<T> ManagedFieldValueHandler<T> getHandler(final Class<T> clazz) {
		assert this.sortedRegistry != null;
		return (ManagedFieldValueHandler<T>) this.mapperCache.computeIfAbsent(clazz, $ -> this.sortedRegistry.stream().filter(handler -> handler.canHandle(clazz)).findFirst().orElse(null));
	}
}
