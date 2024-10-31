package conductance.api.machine.data.serializer;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.fluids.FluidStack;

public final class FluidStackSerializer extends ObjectDataSerializer<FluidStack> {

	@Override
	public Tag serialize(final HolderLookup.Provider registries) {
		return this.getData().saveOptional(registries);
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		this.setData(FluidStack.OPTIONAL_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag).result().orElse(FluidStack.EMPTY));
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf) {
		FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, this.getData());
	}

	@Override
	public void fromNetwork(final RegistryFriendlyByteBuf buf) {
		this.setData(FluidStack.OPTIONAL_STREAM_CODEC.decode(buf));
	}
}
