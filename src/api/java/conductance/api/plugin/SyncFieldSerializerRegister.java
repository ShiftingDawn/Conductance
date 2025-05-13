package conductance.api.plugin;

import java.util.function.Supplier;
import conductance.api.machine.sync.Checker;
import conductance.api.machine.sync.Copier;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;

public interface SyncFieldSerializerRegister {

	int DEFAULT_PRIORITY = 1000;

	<T extends Serializer<?>> int register(Class<T> serializerType, Supplier<T> factory);

	<T extends Serializer<?>> void register(Class<T> serializerType, Supplier<T> factory, ReferenceHandler handler, int priority);

	default <T extends Serializer<?>> void register(final Class<T> serializerType, final Supplier<T> factory, final ReferenceHandler handler) {
		this.register(serializerType, factory, handler, SyncFieldSerializerRegister.DEFAULT_PRIORITY);
	}

	<T, S extends Serializer<T>> void register(Class<S> serializerType, Supplier<S> factory, Class<T> valueType, boolean shallowEqualityCheck, int priority);

	default <T, S extends Serializer<T>> void register(final Class<S> serializerType, final Supplier<S> factory, final Class<T> valueType, final boolean shallowEqualityCheck) {
		this.register(serializerType, factory, valueType, shallowEqualityCheck, SyncFieldSerializerRegister.DEFAULT_PRIORITY);
	}

	void register(Copier<?> copier);

	void register(Checker<?> checker);
}
