package conductance.core.sync.handlers;

import net.minecraft.Util;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.serializers.PrimitiveCodecSerializer;

public class DoubleHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == double.class || clazz == Double.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return Util.make(new PrimitiveCodecSerializer.DoubleSerializer(), s -> s.setData((double) ref.getValueHolder().get()));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final PrimitiveCodecSerializer.DoubleSerializer s = this.testSerializer(serializer, PrimitiveCodecSerializer.DoubleSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
