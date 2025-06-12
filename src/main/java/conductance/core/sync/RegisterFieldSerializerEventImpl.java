package conductance.core.sync;

import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import conductance.api.sync.Checker;
import conductance.api.sync.Copier;
import conductance.api.sync.ReferenceHandler;
import conductance.api.sync.Serializer;
import conductance.api.sync.event.RegisterFieldSerializerEvent;

@AllArgsConstructor
final class RegisterFieldSerializerEventImpl implements RegisterFieldSerializerEvent {

	private final SyncFieldSerializerRegister delegate;

	@Override
	public <T extends Serializer<?>> int register(final Class<T> serializerType, final Supplier<T> factory) {
		return this.delegate.register(serializerType, factory);
	}

	@Override
	public <T extends Serializer<?>> void register(final Class<T> serializerType, final Supplier<T> factory, final ReferenceHandler handler, final int priority) {
		this.delegate.register(serializerType, factory, handler, priority);
	}

	@Override
	public <T, S extends Serializer<T>> void register(final Class<S> serializerType, final Supplier<S> factory, final Class<T> valueType, final boolean shallowEqualityCheck, final int priority) {
		this.delegate.register(serializerType, factory, valueType, shallowEqualityCheck, priority);
	}

	@Override
	public void register(final Copier<?> copier) {
		this.delegate.register(copier);
	}

	@Override
	public void register(final Checker<?> checker) {
		this.delegate.register(checker);
	}
}
