package conductance.api.machine.data.serializer;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public final class ItemStackSerializer extends ObjectDataSerializer<ItemStack> {

	@Override
	public Tag serialize(final HolderLookup.Provider registries) {
		return this.getData().saveOptional(registries);
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		this.setData(ItemStack.OPTIONAL_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag).result().orElse(ItemStack.EMPTY));
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf) {
		ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, this.getData());
	}

	@Override
	public void fromNetwork(final RegistryFriendlyByteBuf buf) {
		this.setData(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
	}
}
