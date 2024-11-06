package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.IManagedSerializer;

public final class IManagedHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return IManaged.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return new IManagedSerializer();
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		//noop
	}
}
