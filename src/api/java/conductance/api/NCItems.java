package conductance.api;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import com.google.common.collect.Table;
import conductance.api.util.TieredItemType;
import conductance.api.util.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCItems {

	public static Table<TieredItemType, Tier, Supplier<? extends Item>> TIERED;

	private NCItems() {
	}
}
