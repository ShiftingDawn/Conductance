package conductance.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.init.ConductanceCreativeTabs;

public interface IConductanceItem extends ItemLike {

	default RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.GENERAL;
	}

	default void fillItemCategory(final CreativeModeTab tab, final NonNullList<ItemStack> list) {
		list.add(new ItemStack(this));
	}
}
