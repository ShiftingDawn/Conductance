package conductance.core.sync.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import conductance.api.machine.sync.Holder;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.ref.CollectionReference;
import conductance.core.sync.ref.ReferenceImpl;
import conductance.core.sync.ref.SimpleHolder;
import conductance.core.sync.serializers.ArraySerializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionHandler implements ReferenceHandler {

	public static final BiFunction<ReferenceHandler, Class<?>, CollectionHandler> FACTORY = Util.memoize(CollectionHandler::new);

	private final ReferenceHandler contentHandler;
	private final Class<?> contentType;

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return Collections.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		final Object currentData = ref.getValueHolder().get();
		if (!(currentData instanceof final Collection<?> collection)) {
			throw new IllegalStateException("Field is not a collection");
		}
		final Serializer<?>[] arr = new Serializer[collection.size()];
		for (int i = 0; i < arr.length; ++i) {
			final ReferenceImpl wrapped = CollectionReference.of(ref.getKey(), collection, i);
			arr[i] = this.contentHandler.readFromReference(operation, wrapped, registries);
		}
		return Util.make(new ArraySerializer(), serializer -> serializer.setData(arr));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer, final HolderLookup.Provider registries) {
		final Object currentData = ref.getValueHolder().get();
		if (!(currentData instanceof final Collection collection)) {
			throw new IllegalStateException("Field is not a collection");
		}
		final ArraySerializer serializer = this.testSerializer(rawSerializer, ArraySerializer.class);
		collection.clear();
		for (final Serializer<?> item : serializer.getData()) {
			final Holder itemHolder = new SimpleHolder();
			this.contentHandler.writeToReference(operation, ReferenceImpl.of(ref.getKey(), itemHolder), item, registries);
			collection.add(itemHolder.get());
		}
	}
}
