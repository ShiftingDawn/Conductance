package conductance.core.sync.handlers;

import net.minecraft.Util;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.serializers.PrimitiveCodecSerializer;

public class LongHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == long.class || clazz == Long.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return Util.make(new PrimitiveCodecSerializer.LongSerializer(), s -> s.setData((long) ref.getValueHolder().get()));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final PrimitiveCodecSerializer.LongSerializer s = this.testSerializer(serializer, PrimitiveCodecSerializer.LongSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
