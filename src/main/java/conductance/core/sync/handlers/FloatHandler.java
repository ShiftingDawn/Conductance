package conductance.core.sync.handlers;

import net.minecraft.Util;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.serializers.PrimitiveCodecSerializer;

public class FloatHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == float.class || clazz == Float.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		return Util.make(new PrimitiveCodecSerializer.FloatSerializer(), s -> s.setData((float) ref.getValueHolder().get()));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer) {
		final PrimitiveCodecSerializer.FloatSerializer s = this.testSerializer(serializer, PrimitiveCodecSerializer.FloatSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
