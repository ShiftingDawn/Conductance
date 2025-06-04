package conductance.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.ItemEntry;
import conductance.api.tier.TieredItemType;
import conductance.api.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCItems {

	public static Table<TieredItemType, Tier, ItemEntry<? extends Item>> TIERED;

	public static ItemStack tiered(final TieredItemType tieredItemType, final Tier tier, final int count) {
		return new ItemStack(NCItems.TIERED.get(tieredItemType, tier).get(), count);
	}

	private NCItems() {
	}
}
