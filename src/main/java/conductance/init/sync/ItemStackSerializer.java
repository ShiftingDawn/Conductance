package conductance.init.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import conductance.api.sync.Operation;
import conductance.api.sync.Reference;
import conductance.api.sync.Serializer;

public class ItemStackSerializer extends Serializer<ItemStack> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.serialize(data -> data.save(registries));
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, @Nullable final Tag tag, final HolderLookup.Provider registries) {
		this.setData(tag == null ? ItemStack.EMPTY : ItemStack.parse(registries, tag).orElse(ItemStack.EMPTY));
	}
}
