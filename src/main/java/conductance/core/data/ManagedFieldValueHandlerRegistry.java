package conductance.core.data;

import java.util.IdentityHashMap;
import java.util.Map;
import com.google.common.primitives.Primitives;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

public final class ManagedFieldValueHandlerRegistry {

	public static final ManagedFieldValueHandlerRegistry INSTANCE = new ManagedFieldValueHandlerRegistry();
	private final Map<Class<?>, ManagedFieldValueHandler<?>> registry = new IdentityHashMap<>();

	private ManagedFieldValueHandlerRegistry() {
	}

	public void register(final ManagedFieldValueHandler<?> handler) {
		if (this.registry.containsKey(handler.getFieldType())) {
			throw new IllegalStateException("Duplicate ManagedFieldValueHandler registration for type " + handler.getFieldType().getName());
		}
		this.registry.put(handler.getFieldType(), handler);
		if (handler.getFieldType().isPrimitive()) {
			this.registry.put(Primitives.wrap(handler.getFieldType()), handler);
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T> ManagedFieldValueHandler<T> getHandler(final Class<T> clazz) {
		return (ManagedFieldValueHandler<T>) this.registry.get(clazz);
	}
}
