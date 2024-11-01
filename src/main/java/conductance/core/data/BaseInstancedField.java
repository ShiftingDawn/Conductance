package conductance.core.data;

import java.util.Objects;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedFieldValueHandler;
import conductance.core.datahandlers.PrimitiveValueHandler;

public class BaseInstancedField implements InstancedField {

	@Getter
	private final ManagedFieldWrapper field;
	@Getter
	private final ManagedFieldValueHandler handler;
	@Getter
	private final Object instance;
	@Getter
	@Setter
	@Nullable
	private Object lastValue;
	@Getter
	private boolean dirty = false;
	@Setter
	@Nullable
	private BooleanConsumer syncDirtyListener;

	BaseInstancedField(final ManagedFieldWrapper field, final ManagedFieldValueHandler handler, final Object instance) {
		this.field = field;
		this.handler = handler;
		this.instance = instance;
	}

	public final void markDirty() {
		this.dirty = true;
		if (this.syncDirtyListener != null) {
			this.syncDirtyListener.accept(true);
		}
	}

	public final void markNotDirty() {
		this.dirty = false;
		if (this.syncDirtyListener != null) {
			this.syncDirtyListener.accept(false);
		}
	}

	void init() {
		this.lastValue = this.get();
	}

	void tick() {
		final Object newValue = this.get();
		if (!Objects.equals(newValue, this.lastValue)) {
			this.lastValue = newValue;
			this.markDirty();
		}
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
			case null -> throw new IllegalStateException("No %s registered for type %s".formatted(ManagedFieldValueHandler.class.getName(), wrapper.getFieldType().getTypeName()));
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
