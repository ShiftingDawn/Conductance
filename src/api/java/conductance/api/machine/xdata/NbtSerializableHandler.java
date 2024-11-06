package conductance.api.machine.xdata;

import net.neoforged.neoforge.common.util.INBTSerializable;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import conductance.api.CAPI;

@SuppressWarnings({"rawtypes", "DataFlowIssue"})
public final class NbtSerializableHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return INBTSerializable.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		final INBTSerializable serializable = (INBTSerializable) ref.getValueHolder().get();
		return XData.make(new NbtSerializer(), serializer -> serializer.setData(serializable.serializeNBT(CAPI.frozenRegistry())));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer) {
		final NbtSerializer serializer = this.testSerializer(rawSerializer, NbtSerializer.class);
		final INBTSerializable serializable = (INBTSerializable) ref.getValueHolder().get();
		serializable.deserializeNBT(CAPI.frozenRegistry(), serializer.getData());
	}
}
