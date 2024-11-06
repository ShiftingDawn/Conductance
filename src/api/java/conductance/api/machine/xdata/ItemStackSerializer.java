package conductance.api.machine.xdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

public final class ItemStackSerializer extends Serializer<ItemStack> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return NbtAdapterFactory.fromTag(adapters, this.getData().saveOptional(CAPI.frozenRegistry()));
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		this.setData(ItemStack.parseOptional(CAPI.frozenRegistry(), (CompoundTag) NbtAdapterFactory.toTag(adapter)));
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkOutput output) {
		if (!(output instanceof final FriendlyNetworkOutput out)) {
			throw new IllegalArgumentException("Only %s can deserialize ItemStack".formatted(FriendlyNetworkOutput.class.getName()));
		}
		ItemStack.OPTIONAL_STREAM_CODEC.encode(out.getBuffer(), this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkInput input) {
		if (!(input instanceof final FriendlyNetworkInput in)) {
			throw new IllegalArgumentException("Only %s can deserialize ItemStack".formatted(FriendlyNetworkInput.class.getName()));
		}
		this.setData(ItemStack.OPTIONAL_STREAM_CODEC.decode(in.getBuffer()));
	}
}
