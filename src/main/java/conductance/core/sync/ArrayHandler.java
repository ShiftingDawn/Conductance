package conductance.core.sync;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class ArrayHandler implements ReferenceHandler {

	public static final BiFunction<ReferenceHandler, Class<?>, ArrayHandler> FACTORY = Util.memoize(ArrayHandler::new);

	private final ReferenceHandler contentHandler;
	private final Class<?> contentType;

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz.isArray();
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		final Object currentData = ref.getValueHolder().get();
		if (currentData == null || !currentData.getClass().isArray()) {
			throw new IllegalStateException("Field %s is not an array".formatted(ref.getKey().getRawField()));
		}
		final int length = Array.getLength(currentData);
		final Serializer<?>[] arr = new Serializer[length];
		for (int i = 0; i < arr.length; ++i) {
			final Reference wrapped = ReferenceHelper.forArray(ref.getKey(), currentData, i, this.contentType);
			arr[i] = this.contentHandler.readFromReference(operation, wrapped, registries);
		}
		return ArraySerializer.of(arr);
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer, final HolderLookup.Provider registries) {
		Object currentData = ref.getValueHolder().get();
		if (currentData != null && !currentData.getClass().isArray()) {
			throw new IllegalStateException("Field is not an array");
		}
		final ArraySerializer serializer = this.testSerializer(rawSerializer, ArraySerializer.class);
		if (currentData == null || Array.getLength(currentData) != serializer.getData().length) {
			currentData = Array.newInstance(this.contentType, serializer.getData().length);
			ref.getValueHolder().set(currentData);
		}
		for (int i = 0; i < Array.getLength(currentData); ++i) {
			final Reference itemRef = ReferenceHelper.forArray(ref.getKey(), currentData, i, this.contentType);
			this.contentHandler.writeToReference(operation, itemRef, serializer.getData()[i], registries);
		}
	}
}
