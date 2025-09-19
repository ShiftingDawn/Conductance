package conductance.init.block;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public final class MaterialOreBlockItem extends BlockItem {

	public MaterialOreBlockItem(final Block block, final Properties properties) {
		super(block, properties.component(DataComponents.ITEM_NAME, block.getName()));
	}
}
