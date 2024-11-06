package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.adapter.NumberAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class IntSerializer extends Serializer<Integer> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return adapters.ofInt(this.getData());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final NumberAdapter<?> numberAdapter = this.testAdapter(adapter, NumberAdapter.class);
		this.setData(numberAdapter.getInt());
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkOutput output) {
		output.write(this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkInput input) {
		this.setData(input.readInt());
	}

	public static IntSerializer of(final int data) {
		return XData.make(new IntSerializer(), serializer -> serializer.setData(data));
	}
}
