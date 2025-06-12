package conductance.core.sync;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.sync.Holder;
import conductance.api.sync.Operation;
import conductance.api.sync.Reference;
import conductance.api.sync.ReferenceHandler;
import conductance.api.sync.Serializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class CollectionHandler implements ReferenceHandler {

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
			final Reference wrapped = ReferenceHelper.forCollection(ref.getKey(), collection, i);
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
		if (serializer.getData() == null) {
			ref.getValueHolder().set(null);
		}
		collection.clear();
		for (final Serializer<?> item : serializer.getData()) {
			final Holder itemHolder = new SimpleHolder();
			this.contentHandler.writeToReference(operation, ReferenceHelper.of(ref.getKey(), itemHolder), item, registries);
			collection.add(itemHolder.get());
		}
	}

	@RequiredArgsConstructor
	private static final class SimpleHolder implements Holder {

		@Nullable
		private Object data;

		@Override
		@Nullable
		public Object get() {
			return this.data;
		}

		@Override
		public void set(@Nullable final Object object) {
			this.data = object;
		}
	}
}
