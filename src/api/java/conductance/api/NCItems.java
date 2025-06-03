package conductance.api;

import net.minecraft.world.item.Item;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.ItemEntry;
import conductance.api.util.TieredItemType;
import conductance.api.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCItems {

	public static Table<TieredItemType, Tier, ItemEntry<? extends Item>> TIERED;

	private NCItems() {
	}
}
