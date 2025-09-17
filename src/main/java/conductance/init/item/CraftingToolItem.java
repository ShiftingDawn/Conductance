package conductance.init.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CraftingToolItem extends Item {

	public CraftingToolItem(final Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getCraftingRemainder(final ItemStack itemStack) {
		return itemStack;
	}
}
