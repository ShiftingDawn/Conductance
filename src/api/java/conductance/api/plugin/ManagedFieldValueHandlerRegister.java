package conductance.api.plugin;

import conductance.api.machine.data.ManagedFieldValueHandler;
import conductance.api.machine.data.ManagedFieldValueMutator;

public interface ManagedFieldValueHandlerRegister {

	void register(ManagedFieldValueHandler<?> handler, int priority);

	default void register(final ManagedFieldValueHandler<?> handler) {
		this.register(handler, 100);
	}

	void register(ManagedFieldValueMutator<?> mutator, int priority);

	default void register(final ManagedFieldValueMutator<?> mutator) {
		this.register(mutator, 100);
	}
}
