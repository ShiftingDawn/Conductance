package conductance.api.machine.data.handler;

import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.ObjectDataSerializer;

public abstract class ManagedObjectValueHandler implements ManagedFieldValueHandler {

	private final Class<?> typeClass;
	private final boolean shallowHandlerCheck;

	protected ManagedObjectValueHandler(final Class<?> typeClass, final boolean shallowHandlerCheck) {
		this.typeClass = typeClass;
		this.shallowHandlerCheck = shallowHandlerCheck;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return this.shallowHandlerCheck ? type == this.typeClass : this.typeClass.isAssignableFrom(type);
	}

	public static <T> ManagedObjectValueHandler create(final Class<T> clazz, final boolean shallowHandlerCheck, final Supplier<? extends ObjectDataSerializer<T>> serializer) {
		return new ManagedObjectValueHandler(clazz, shallowHandlerCheck) {

			@Override
			public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
				return Util.make((ObjectDataSerializer) serializer.get(), serializer -> serializer.setData(field.get()));
			}

			@Override
			public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
				if (!(value instanceof ObjectDataSerializer<?>)) {
					throw new IllegalArgumentException("Serializer %s is not %s".formatted(value.getClass().getName(), ObjectDataSerializer.class.getName()));
				}
				field.set(value.getData());
			}
		};
	}
}
