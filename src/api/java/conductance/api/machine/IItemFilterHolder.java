package conductance.api.machine;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

public interface IItemFilterHolder {

	Predicate<ItemStack> getItemFilter();
}
