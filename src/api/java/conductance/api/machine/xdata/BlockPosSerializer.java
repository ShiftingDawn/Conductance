package conductance.api.machine.xdata;

import net.minecraft.core.BlockPos;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public final class BlockPosSerializer extends Serializer<BlockPos> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return XData.make(adapters.array(), array -> {
			array.add(this.getData().getX());
			array.add(this.getData().getY());
			array.add(this.getData().getZ());
		});
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final ArrayAdapter array = this.testAdapter(adapter, ArrayAdapter.class);
		this.setData(new BlockPos(array.getInt(0), array.getInt(1), array.getInt(2)));
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkOutput output) {
		output.write(this.getData().getX());
		output.write(this.getData().getY());
		output.write(this.getData().getZ());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkInput input) {
		this.setData(new BlockPos(input.readInt(), input.readInt(), input.readInt()));
	}
}
