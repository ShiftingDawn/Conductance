package conductance.api.machine.data.handler;

import conductance.api.machine.data.serializer.DataSerializer;

public interface ManagedFieldValueHandler {

	boolean canHandle(Class<?> type);

	DataSerializer<?> readFromField(InstancedField field);

	void writeToField(InstancedField field, DataSerializer<?> value);
}
