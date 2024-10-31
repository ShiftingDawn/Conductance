package conductance.core.data;

import java.lang.reflect.Field;
import lombok.Getter;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;

final class InstancedFieldImpl implements InstancedField {

	private final ManagedFieldWrapper field;
	@Getter
	private final ManagedFieldValueHandler handler;
	@Getter
	private final Object instance;

	private InstancedFieldImpl(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
		this.field = field;
		this.handler = handler;
		this.instance = instance;
	}

	@Override
	public Field getField() {
		return this.field.getField();
	}

	@Override
	public String getName() {
		return this.field.getName();
	}

	static InstancedFieldImpl of(final ManagedFieldWrapper wrapper, final Object instance) {
		final ManagedFieldValueHandler valueHandler = ManagedFieldValueHandlerRegistry.INSTANCE.getHandler(wrapper.getFieldType(), wrapper);
		if (valueHandler == null) {
			throw new IllegalStateException("No %s registered for type %s".formatted(ManagedFieldValueHandler.class.getName(), wrapper.getFieldType().getName()));
		}
		return new InstancedFieldImpl(wrapper, valueHandler, instance);
	}
}
