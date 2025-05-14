package conductance.core.sync.handlers;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.ReferenceHandler;
import conductance.api.machine.sync.Serializer;
import conductance.core.sync.serializers.PrimitiveCodecSerializer;

public class CharHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return clazz == char.class || clazz == Character.class;
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return Util.make(new PrimitiveCodecSerializer.CharSerializer(), s -> s.setData((char) ref.getValueHolder().get()));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> serializer, final HolderLookup.Provider registries) {
		final PrimitiveCodecSerializer.CharSerializer s = this.testSerializer(serializer, PrimitiveCodecSerializer.CharSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
