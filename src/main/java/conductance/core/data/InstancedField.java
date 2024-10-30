package conductance.core.data;

import net.minecraft.nbt.Tag;
import lombok.Getter;
import conductance.api.machine.data.ManagedFieldValueHandler;

public final class InstancedField<T> {

	private final ManagedFieldWrapper field;
	private final ManagedFieldValueHandler<T> valueHandler;
	private final Object instance;
	private T lastValue;
	@Getter
	private boolean dirty = false;

	private InstancedField(final ManagedFieldWrapper field, final ManagedFieldValueHandler<T> valueHandler, final Object instance) {
		this.field = field;
		this.valueHandler = valueHandler;
		this.instance = instance;
		this.lastValue = this.get();
	}

	public T get() {
		return this.valueHandler.getValue(this.field.getField(), this.instance);
	}

	public Tag serialize() {
		return this.valueHandler.serialize(this.lastValue);
	}

	public T deserialize(final Tag tag) {
		return this.valueHandler.deserialize(tag);
	}

	public void markDirty() {
		this.dirty = true;
	}

	public void markNotDirty() {
		this.dirty = false;
	}

	public void tick() {
		final T currentValue = this.get();
		if (!this.valueHandler.equals(currentValue, this.lastValue)) {
			this.lastValue = currentValue;
			this.markDirty();
		}
	}

	@SuppressWarnings("unchecked")
	static <T> InstancedField<T> of(final ManagedFieldWrapper wrapper, final Object instance) {
		final ManagedFieldValueHandler<T> valueHandler = ManagedFieldValueHandlerRegistry.INSTANCE.getHandler((Class<T>) wrapper.getFieldType());
		if (valueHandler == null) {
			throw new IllegalStateException("No %s registered for type %s".formatted(ManagedFieldValueHandler.class.getName(), wrapper.getFieldType().getName()));
		}
		return new InstancedField<>(wrapper, valueHandler, instance);
	}
}
