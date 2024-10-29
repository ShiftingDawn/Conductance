package conductance.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CraftingToolItem extends ConductanceItem {

	public CraftingToolItem(final Item.Properties props) {
		super(props.stacksTo(1));
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}

	@Override
	public boolean hasCraftingRemainingItem(final ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(final ItemStack itemStack) {
		return itemStack;
	}
}
