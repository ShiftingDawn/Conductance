package conductance.api.plugin;

import java.util.function.Supplier;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;

public interface ManagedFieldValueHandlerRegister {

	<T extends DataSerializer<?>> void register(Class<T> clazz, Supplier<T> constructor, ManagedFieldValueHandler handler, int priority);

	default <T extends DataSerializer<?>> void register(final Class<T> clazz, final Supplier<T> constructor, final ManagedFieldValueHandler handler) {
		this.register(clazz, constructor, handler, 100);
	}
}
