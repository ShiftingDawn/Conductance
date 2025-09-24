package conductance.api.machine;

import net.minecraft.world.item.ItemStack;

public interface InventoryPredicate {

	boolean test(int slot, ItemStack stack);
}
