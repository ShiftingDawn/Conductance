package conductance.api.machine.data.serializer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public final class BlockPosSerializer extends ObjectDataSerializer<BlockPos> {

	@Override
	public Tag serialize(final HolderLookup.Provider registries) {
		return NbtUtils.writeBlockPos(this.getData());
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		final IntArrayTag ints = (IntArrayTag) tag;
		this.setData(new BlockPos(ints.get(0).getAsInt(), ints.get(1).getAsInt(), ints.get(2).getAsInt()));
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(this.getData());
	}

	@Override
	public void fromNetwork(final RegistryFriendlyByteBuf buf) {
		this.setData(buf.readBlockPos());
	}
}
