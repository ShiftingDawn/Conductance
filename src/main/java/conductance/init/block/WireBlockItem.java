package conductance.init.block;

import net.minecraft.core.component.DataComponents;

public final class WireBlockItem extends PipeBlockItem {

	public WireBlockItem(final WireBlock block, final Properties properties) {
		super(block, properties.component(DataComponents.ITEM_NAME, block.getName()));
	}

	@Override
	public WireBlock getBlock() {
		return (WireBlock) super.getBlock();
	}
}
