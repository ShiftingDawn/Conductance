package conductance.core.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

final class InstancedField<T> {

	private final ManagedFieldWrapper field;
	private final ManagedFieldValueHandler<T> valueHandler;
	private final Object instance;
	@Nullable
	private T lastValue;
	@Getter
	private boolean dirty = false;

	private InstancedField(final ManagedFieldWrapper field, final ManagedFieldValueHandler<T> valueHandler, final Object instance) {
		this.field = field;
		this.valueHandler = valueHandler;
		this.instance = instance;
	}

	public void init() {
		this.lastValue = this.get();
	}

	@Nullable
	public T get() {
		return this.valueHandler.getValue(this.field.getField(), this.instance);
	}

	public void set(@Nullable final T value) {
		this.valueHandler.setValue(this.field.getField(), this.instance, value);
		this.lastValue = this.get();
	}

	public @Nullable Tag serialize(final HolderLookup.Provider registries) {
		if (this.lastValue == null) {
			return null;
		}
		return this.valueHandler.serialize(this.lastValue, registries);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public T deserialize(final Tag tag, final HolderLookup.Provider registries) {
		return this.valueHandler.deserialize((Class<T>) this.field.getFieldType(), tag, registries);
	}

	public void markDirty() {
		this.dirty = true;
	}

	public void markNotDirty() {
		this.dirty = false;
	}

	public void tick() {
		final T currentValue = this.get();
		if (currentValue == null || this.lastValue == null) {
			if (currentValue == this.lastValue) {
				return;
			}
			this.lastValue = currentValue;
			this.markDirty();
		} else if (!this.valueHandler.equals(currentValue, this.lastValue)) {
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
