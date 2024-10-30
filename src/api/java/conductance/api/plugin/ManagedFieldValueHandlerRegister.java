package conductance.api.plugin;

import conductance.api.machine.data.ManagedFieldValueHandler;

public interface ManagedFieldValueHandlerRegister {

	void register(ManagedFieldValueHandler<?> handler, int priority);

	default void register(final ManagedFieldValueHandler<?> handler) {
		this.register(handler, 100);
	}
}
