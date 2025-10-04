package conductance.api;

import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.Table;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredItemType;
import conductance.api.util.ExtruderShape;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCItems {

	public static Holder<Item> WRENCH;
	public static Holder<Item> HAMMER;
	public static Holder<Item> WIRE_CUTTERS;
	public static Table<TieredItemType, Tier, Holder<Item>> TIERED;
	public static Holder<Item> PROGRAM_CIRCUIT;
	public static Holder<Item> EMPTY_EXTRUDER_SHAPE;
	public static Map<ExtruderShape, Holder<Item>> EXTRUDER_SHAPES;
	public static Holder<Item> WOOD_CIRCUIT_BOARD;
	public static Holder<Item> WOOD_CIRCUIT_SUBSTRATE;
	public static Holder<Item> DIODE;
	public static Holder<Item> RESISTOR;
	public static Holder<Item> TRANSISTOR;

	public static ItemStack tiered(final TieredItemType tieredItemType, final Tier tier, final int count) {
		final Holder<Item> item = NCItems.TIERED.get(tieredItemType, tier);
		assert item != null;
		return new ItemStack(item, count);
	}

	private NCItems() {
	}
}
