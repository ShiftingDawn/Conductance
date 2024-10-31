package conductance.core.data;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import conductance.api.CAPI;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.api.machine.data.serializer.ArraySerializer;
import conductance.api.machine.data.serializer.DataSerializer;

final class ArrayValueHandler implements ManagedFieldValueHandler {

	static final BiFunction<ManagedFieldValueHandler, Class<?>, ArrayValueHandler> FACTORY = Util.memoize(ArrayValueHandler::new);

	private final ManagedFieldValueHandler contentHandler;
	private final Class<?> contentType;

	private ArrayValueHandler(final ManagedFieldValueHandler contentHandler, final Class<?> contentType) {
		this.contentHandler = contentHandler;
		this.contentType = contentType;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isArray();
	}

	@Override
	public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
		final Object currentData = field.get();
		if (currentData == null || !currentData.getClass().isArray()) {
			throw new IllegalStateException("Field is not an array");
		}
		final int length = Array.getLength(currentData);
		final DataSerializer<?>[] arr = new DataSerializer[length];
		for (int i = 0; i < arr.length; ++i) {
			final Object value = Array.get(currentData, i);
			final InstancedField wrapped = CAPI.managedDataRegistry().getInstancedField(value);
			arr[i] = this.contentHandler.readFromField(wrapped, registries);
		}
		return Util.make(new ArraySerializer(), serializer -> serializer.setData(arr));
	}

	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
		Object currentData = field.get();
		if (!currentData.getClass().isArray()) {
			throw new IllegalStateException("Field is not an array");
		}
		if (!(value instanceof final ArraySerializer arraySerializer)) {
			throw new IllegalArgumentException("Serializer %s is not %s".formatted(value.getClass().getName(), ArraySerializer.class.getName()));
		}
		if (currentData == null || Array.getLength(currentData) != arraySerializer.getData().length) {
			currentData = Array.newInstance(this.contentType, arraySerializer.getData().length);
			field.set(currentData);
		}
		for (int i = 0; i < Array.getLength(currentData); ++i) {
			final DataSerializer<?> item = arraySerializer.getData()[i];
			final InstancedField wrapped = CAPI.managedDataRegistry().getInstancedField(new Object());
			this.contentHandler.writeToField(wrapped, item, registries);
			Array.set(currentData, i, wrapped.get());
		}
	}
}
