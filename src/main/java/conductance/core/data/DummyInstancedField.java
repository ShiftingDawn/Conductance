package conductance.core.data;

import conductance.api.machine.data.handler.InstancedField;

final class DummyInstancedField implements InstancedField {

	private Object value;

	DummyInstancedField(final Object value) {
		this.value = value;
	}

	@Override
	public Object get() {
		return this.value;
	}

	@Override
	public void set(final Object newValue) {
		this.value = newValue;
	}
}
