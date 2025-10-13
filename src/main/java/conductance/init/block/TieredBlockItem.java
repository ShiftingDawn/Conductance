package conductance.init.block;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;

public final class TieredBlockItem extends BlockItem {

	public TieredBlockItem(final TieredBlock block, final Properties properties) {
		super(block, properties.component(DataComponents.ITEM_NAME, block.getName()));
	}
}
