package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.ByteSerializer;

public class ByteHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == byte.class || clazz == Byte.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return ByteSerializer.of((byte) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final ByteSerializer s = this.testSerializer(serializer, ByteSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
