package conductance.core.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import conductance.api.CAPI;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.ArraySerializer;
import conductance.api.machine.data.serializer.DataSerializer;

@SuppressWarnings({"rawtypes", "unchecked"})
final class CollectionValueHandler implements ManagedFieldValueHandler {

	static final Function<ManagedFieldValueHandler, CollectionValueHandler> FACTORY = Util.memoize(CollectionValueHandler::new);

	private final ManagedFieldValueHandler contentHandler;

	private CollectionValueHandler(final ManagedFieldValueHandler contentHandler) {
		this.contentHandler = contentHandler;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return Collection.class.isAssignableFrom(type);
	}

	@Override
	public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
		final Object currentData = field.get();
		if (!(currentData instanceof final Collection<?> collection)) {
			throw new IllegalStateException("Field is not a collection");
		}
		final Iterator<?> iterator = collection.iterator();
		final DataSerializer<?>[] arr = new ArraySerializer[collection.size()];
		for (int i = 0; i < arr.length; ++i) {
			final Object value = iterator.next();
			final InstancedField wrapped = CAPI.managedDataRegistry().getInstancedField(value);
			arr[i] = this.contentHandler.readFromField(wrapped, registries);
		}
		return Util.make(new ArraySerializer(), serializer -> serializer.setData(arr));
	}

	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
		final Object currentData = field.get();
		if (!(currentData instanceof final Collection collection)) {
			throw new IllegalStateException("Field is not a collection");
		}
		if (!(value instanceof final ArraySerializer arraySerializer)) {
			throw new IllegalArgumentException("Serializer %s is not %s".formatted(value.getClass().getName(), ArraySerializer.class.getName()));
		}

		collection.clear();
		for (int i = 0; i < arraySerializer.getData().length; ++i) {
			final DataSerializer<?> item = arraySerializer.getData()[i];
			final InstancedField wrapped = new DummyInstancedField(new Object());
			this.contentHandler.writeToField(wrapped, item, registries);
			collection.add(wrapped.get());
		}
	}
}
