package conductance.core.data;

import lombok.Getter;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.core.datahandlers.PrimitiveValueHandler;

class BaseInstancedField implements InstancedField {

	@Getter
	private final ManagedFieldWrapper field;
	@Getter
	private final ManagedFieldValueHandler handler;
	@Getter
	private final Object instance;

	BaseInstancedField(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
		this.field = field;
		this.handler = handler;
		this.instance = instance;
	}

	@Override
	public Object get() {
		try {
			return this.field.getField().get(this.getInstance());
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void set(final Object data) {
		try {
			this.field.getField().set(this.getInstance(), data);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	static BaseInstancedField of(final ManagedFieldWrapper wrapper, final Object instance) {
		final ManagedFieldValueHandler valueHandler = ManagedFieldValueHandlerRegistry.INSTANCE.getHandler(wrapper.getFieldType());
		return switch (valueHandler) {
			case null -> throw new IllegalStateException("No %s registered for type %s".formatted(ManagedFieldValueHandler.class.getName(), wrapper.getFieldType().getName()));
			case final PrimitiveValueHandler.BooleanHandler ignored -> new PrimitiveInstancedFields.InstancedBoolean(wrapper, valueHandler, instance);
			case final PrimitiveValueHandler.ByteHandler ignored -> new PrimitiveInstancedFields.InstancedByte(wrapper, valueHandler, instance);
			case final PrimitiveValueHandler.ShortHandler ignored -> new PrimitiveInstancedFields.InstancedShort(wrapper, valueHandler, instance);
			case final PrimitiveValueHandler.IntegerHandler ignored -> new PrimitiveInstancedFields.InstancedInteger(wrapper, valueHandler, instance);
			case final PrimitiveValueHandler.LongHandler ignored -> new PrimitiveInstancedFields.InstancedLong(wrapper, valueHandler, instance);
			case final PrimitiveValueHandler.FloatHandler ignored -> new PrimitiveInstancedFields.InstancedFloat(wrapper, valueHandler, instance);
			case final PrimitiveValueHandler.DoubleHandler ignored -> new PrimitiveInstancedFields.InstancedDouble(wrapper, valueHandler, instance);
			case final PrimitiveValueHandler.CharacterHandler ignored -> new PrimitiveInstancedFields.InstancedCharacter(wrapper, valueHandler, instance);
			default -> new BaseInstancedField(wrapper, valueHandler, instance);
		};
	}
}
