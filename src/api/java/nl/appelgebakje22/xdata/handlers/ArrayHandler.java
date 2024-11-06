package nl.appelgebakje22.xdata.handlers;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.ArrayReference;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.ArraySerializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayHandler implements ReferenceHandler {

	public static final BiFunction<ReferenceHandler, Class<?>, ArrayHandler> FACTORY = XData.memoize(ArrayHandler::new);

	private final ReferenceHandler contentHandler;
	private final Class<?> contentType;

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz.isArray();
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		final Object currentData = ref.getValueHolder().get();
		if (currentData == null || !currentData.getClass().isArray()) {
			throw new IllegalStateException("Field %s is not an array".formatted(ref.getKey().getRawField()));
		}
		final int length = Array.getLength(currentData);
		final Serializer<?>[] arr = new Serializer[length];
		for (int i = 0; i < arr.length; ++i) {
			final Reference wrapped = ArrayReference.of(ref.getKey(), currentData, i, this.contentType);
			arr[i] = this.contentHandler.readFromReference(operation, wrapped);
		}
		return ArraySerializer.of(arr);
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer) {
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
			final ArrayReference itemRef = ArrayReference.of(ref.getKey(), currentData, i, this.contentType);
			this.contentHandler.writeToReference(operation, itemRef, serializer.getData()[i]);
		}
	}
}
