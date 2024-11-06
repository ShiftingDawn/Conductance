package conductance.api.machine.xdata;

import net.minecraft.resources.ResourceLocation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public final class ResourceLocationSerializer extends Serializer<ResourceLocation> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return adapters.ofString(this.getData().toString());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final StringAdapter stringAdapter = this.testAdapter(adapter, StringAdapter.class);
		this.setData(ResourceLocation.parse(stringAdapter.getString()));
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkOutput output) {
		output.write(this.getData().toString());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkInput input) {
		this.setData(ResourceLocation.parse(input.readString()));
	}

	public static ResourceLocationSerializer of(final ResourceLocation resourceLocation) {
		return XData.make(new ResourceLocationSerializer(), serializer -> serializer.setData(resourceLocation));
	}
}
