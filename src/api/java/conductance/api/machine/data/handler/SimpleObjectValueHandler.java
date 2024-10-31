package conductance.api.machine.data.handler;

import conductance.api.machine.data.serializer.DataSerializer;

public abstract class SimpleObjectValueHandler extends ManagedObjectValueHandler {

	protected SimpleObjectValueHandler(final Class<?> typeClass, final boolean shallowHandlerCheck) {
		super(typeClass, shallowHandlerCheck);
	}

	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value) {
		try {
			field.getField().set(field.getInstance(), value.getData());
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
