package conductance.core.sync;

import java.util.function.Supplier;
import conductance.api.sync.Checker;
import conductance.api.sync.Copier;
import conductance.api.sync.ReferenceHandler;
import conductance.api.sync.Serializer;

interface SyncFieldSerializerRegister {

	<T extends Serializer<?>> int register(Class<T> serializerType, Supplier<T> factory);

	<T extends Serializer<?>> void register(Class<T> serializerType, Supplier<T> factory, ReferenceHandler handler, int priority);

	<T, S extends Serializer<T>> void register(Class<S> serializerType, Supplier<S> factory, Class<T> valueType, boolean shallowEqualityCheck, int priority);

	void register(Copier<?> copier);

	void register(Checker<?> checker);
}
