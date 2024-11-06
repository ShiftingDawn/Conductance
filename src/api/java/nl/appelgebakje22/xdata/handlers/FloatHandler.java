package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.FloatSerializer;

public class FloatHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == float.class || clazz == Float.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return FloatSerializer.of((float) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final FloatSerializer s = this.testSerializer(serializer, FloatSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
