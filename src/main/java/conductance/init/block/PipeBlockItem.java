package conductance.init.block;

import net.minecraft.world.item.BlockItem;

public class PipeBlockItem extends BlockItem {

	public PipeBlockItem(final PipeBlock<?, ?, ?> block, final Properties properties) {
		super(block, properties);
	}

	@Override
	public PipeBlock<?, ?, ?> getBlock() {
		return (PipeBlock<?, ?, ?>) super.getBlock();
	}
}
