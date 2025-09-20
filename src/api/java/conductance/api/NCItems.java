package conductance.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.Table;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredItemType;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCItems {

	public static Table<TieredItemType, Tier, Item> TIERED;

	public static ItemStack tiered(final TieredItemType tieredItemType, final Tier tier, final int count) {
		final Item item = NCItems.TIERED.get(tieredItemType, tier);
		assert item != null;
		return new ItemStack(item, count);
	}

	private NCItems() {
	}
}
