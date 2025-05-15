package conductance.core.sync;

import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;

final class SimpleObjectHandler implements ReferenceHandler {

	private final Class<?> typeClass;
	private final boolean shallowEqualityCheck;
	private final Supplier<? extends Serializer<?>> serializerFactory;

	SimpleObjectHandler(final Class<?> typeClass, final boolean shallowEqualityCheck, final Supplier<? extends Serializer<?>> serializerFactory) {
		this.typeClass = typeClass;
		this.shallowEqualityCheck = shallowEqualityCheck;
		this.serializerFactory = serializerFactory;
	}

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return this.shallowEqualityCheck ? clazz.equals(this.typeClass) : this.typeClass.isAssignableFrom(clazz);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return Util.make(this.serializerFactory.get(), serializer -> ((Serializer) serializer).setData(ref.getValueHolder().get()));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer, final HolderLookup.Provider registries) {
		ref.getValueHolder().set(serializer.getData());
	}
}
