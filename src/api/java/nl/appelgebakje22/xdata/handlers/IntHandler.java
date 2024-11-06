package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.IntSerializer;

public class IntHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == int.class || clazz == Integer.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return IntSerializer.of((int) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final IntSerializer s = this.testSerializer(serializer, IntSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
