package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.CharSerializer;

public class CharHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == char.class || clazz == Character.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return CharSerializer.of((char) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final CharSerializer s = this.testSerializer(serializer, CharSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
