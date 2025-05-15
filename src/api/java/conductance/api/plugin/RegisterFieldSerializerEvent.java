package conductance.api.plugin;

import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.machine.sync.Checker;
import conductance.api.machine.sync.Copier;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterFieldSerializerEvent implements IConductancePluginEvent {

	public static final int DEFAULT_PRIORITY = 1000;

	public interface SyncFieldSerializerRegister {

		<T extends Serializer<?>> int register(Class<T> serializerType, Supplier<T> factory);

		<T extends Serializer<?>> void register(Class<T> serializerType, Supplier<T> factory, ReferenceHandler handler, int priority);

		<T, S extends Serializer<T>> void register(Class<S> serializerType, Supplier<S> factory, Class<T> valueType, boolean shallowEqualityCheck, int priority);

		void register(Copier<?> copier);

		void register(Checker<?> checker);
	}

	private final SyncFieldSerializerRegister delegate;

	public <T extends Serializer<?>> int register(final Class<T> serializerType, final Supplier<T> factory) {
		return this.delegate.register(serializerType, factory);
	}

	public <T extends Serializer<?>> void register(final Class<T> serializerType, final Supplier<T> factory, final ReferenceHandler handler, final int priority) {
		this.delegate.register(serializerType, factory, handler, priority);
	}

	public <T extends Serializer<?>> void register(final Class<T> serializerType, final Supplier<T> factory, final ReferenceHandler handler) {
		this.register(serializerType, factory, handler, RegisterFieldSerializerEvent.DEFAULT_PRIORITY);
	}

	public <T, S extends Serializer<T>> void register(final Class<S> serializerType, final Supplier<S> factory, final Class<T> valueType, final boolean shallowEqualityCheck, final int priority) {
		this.delegate.register(serializerType, factory, valueType, shallowEqualityCheck, priority);
	}

	public <T, S extends Serializer<T>> void register(final Class<S> serializerType, final Supplier<S> factory, final Class<T> valueType, final boolean shallowEqualityCheck) {
		this.register(serializerType, factory, valueType, shallowEqualityCheck, RegisterFieldSerializerEvent.DEFAULT_PRIORITY);
	}

	public void register(final Copier<?> copier) {
		this.delegate.register(copier);
	}

	public void register(final Checker<?> checker) {
		this.delegate.register(checker);
	}
}
