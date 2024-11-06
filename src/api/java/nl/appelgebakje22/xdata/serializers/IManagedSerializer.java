package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public final class IManagedSerializer extends Serializer<IManaged> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		final IManaged managed = (IManaged) ref.getValueHolder().get();
		return managed.getDataMap().serialize(Operation.FULL, adapters);
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final TableAdapter tableAdapter = this.testAdapter(adapter, TableAdapter.class);
		final IManaged managed = (IManaged) ref.getValueHolder().get();
		managed.getDataMap().deserialize(Operation.FULL, adapters, tableAdapter);
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkOutput output) {
		final IManaged managed = (IManaged) ref.getValueHolder().get();
		output.write(false); //TODO check partial or full
		managed.getDataMap().toNetwork(Operation.PARTIAL, output);
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkInput input) {
		final IManaged managed = (IManaged) ref.getValueHolder().get();
		managed.getDataMap().fromNetwork(input.readBoolean() ? Operation.FULL : Operation.PARTIAL, input);
	}
}
