package conductance.api.plugin;

import conductance.api.machine.data.DataSerializer;
import conductance.api.machine.data.ManagedFieldValueHandler;

public interface ManagedFieldValueHandlerRegister {

	void register(DataSerializer<?> serializer, int priority);

	default void register(final DataSerializer<?> serializer) {
		this.register(serializer, 100);
	}

	void register(ManagedFieldValueHandler<?> handler, int priority);

	default void register(final ManagedFieldValueHandler<?> handler) {
		this.register(handler, 100);
	}
}
