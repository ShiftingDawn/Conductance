package conductance.api.plugin;

import java.util.function.Supplier;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.handler.ManagedObjectValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.ObjectDataSerializer;

public interface ManagedFieldValueHandlerRegister {

	<T extends DataSerializer<?>> int registerSerializer(Class<T> clazz, Supplier<T> constructor);

	<T extends DataSerializer<?>> void register(Class<T> clazz, Supplier<T> constructor, ManagedFieldValueHandler handler, int priority);

	default <T extends DataSerializer<?>> void register(final Class<T> clazz, final Supplier<T> constructor, final ManagedFieldValueHandler handler) {
		this.register(clazz, constructor, handler, 100);
	}

	default <T, S extends ObjectDataSerializer<T>> void register(final Class<S> clazz, final Supplier<S> constructor, final Class<T> objectType, final boolean shallowHandlerCheck, final int priority) {
		this.register(clazz, constructor, ManagedObjectValueHandler.create(objectType, shallowHandlerCheck, constructor), priority);
	}

	default <T, S extends ObjectDataSerializer<T>> void register(final Class<S> clazz, final Supplier<S> constructor, final Class<T> objectType, final boolean shallowHandlerCheck) {
		this.register(clazz, constructor, objectType, shallowHandlerCheck, 100);
	}
}
