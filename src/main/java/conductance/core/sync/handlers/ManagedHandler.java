package conductance.core.sync.handlers;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.serializers.ManagedSerializer;

public class ManagedHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return IManaged.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		final IManaged managed = (IManaged) ref.getValueHolder().get();
		assert managed != null;
		return Util.make(new ManagedSerializer(), serializer -> serializer.setData(managed));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer, final HolderLookup.Provider registries) {
		//NOOP
	}
}
