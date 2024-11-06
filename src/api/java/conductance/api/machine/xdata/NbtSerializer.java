package conductance.api.machine.xdata;

import net.minecraft.nbt.Tag;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public final class NbtSerializer extends Serializer<Tag> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return NbtAdapterFactory.fromTag(adapters, this.getData());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		this.setData(NbtAdapterFactory.toTag(adapter));
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkOutput output) {
		XDataHelper.writeNbt(output, this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkInput input) {
		this.setData(XDataHelper.readNbt(input));
	}
}
