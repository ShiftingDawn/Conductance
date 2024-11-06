package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.ShortSerializer;

public class ShortHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == short.class || clazz == Short.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return ShortSerializer.of((short) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final ShortSerializer s = this.testSerializer(serializer, ShortSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
