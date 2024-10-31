package conductance.api.registry;

import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.IManaged;
import conductance.api.machine.data.ManagedDataMap;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.serializer.DataSerializer;

public interface ManagedDataRegistry {

	ManagedDataMap requestDataMap(IManaged managed);

	int getSerializerId(DataSerializer<?> serializer);

	@Nullable
	DataSerializer<?> makeSerializer(int serializerId);

	<T> InstancedField getInstancedField(T data);
}
