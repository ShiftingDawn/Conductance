package conductance.api.machine.data.handler;

import net.minecraft.core.HolderLookup;
import conductance.api.machine.data.serializer.DataSerializer;

public interface ManagedFieldValueHandler {

	boolean canHandle(Class<?> type);

	DataSerializer<?> readFromField(InstancedField field, HolderLookup.Provider registries);

	void writeToField(InstancedField field, DataSerializer<?> value, HolderLookup.Provider registries);

	@SuppressWarnings("unchecked")
	default <T extends DataSerializer<?>> T testSerializer(final DataSerializer<?> serializer, final Class<T> expectedType) {
		if (!expectedType.isAssignableFrom(serializer.getClass())) {
			throw new IllegalArgumentException("Serializer %s is not %s".formatted(serializer.getClass().getName(), expectedType.getName()));
		}
		return (T) serializer;
	}
}
