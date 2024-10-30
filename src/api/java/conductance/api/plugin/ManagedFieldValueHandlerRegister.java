package conductance.api.plugin;

import conductance.api.machine.data.ManagedFieldValueHandler;

public interface ManagedFieldValueHandlerRegister {

	void register(ManagedFieldValueHandler<?> handler);
}
