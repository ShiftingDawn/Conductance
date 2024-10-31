package conductance.api.machine.data.handler;

import java.lang.reflect.Field;

public interface InstancedField {

	Field getField();

	String getName();

	Object getInstance();

	ManagedFieldValueHandler getHandler();
}
