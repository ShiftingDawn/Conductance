package conductance.init.block;

import net.minecraft.client.color.item.ItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class WireBlockItem extends PipeBlockItem {

	public WireBlockItem(final WireBlock block, final Properties properties) {
		super(block, properties);
	}

	@Override
	public WireBlock getBlock() {
		return (WireBlock) super.getBlock();
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if (stack.getItem() instanceof final WireBlockItem materialBlockItem) {
				return materialBlockItem.getBlock().getColorTint(materialBlockItem.getBlock().defaultBlockState(), null, null, tintIndex);
			}
			return -1;
		};
	}
}
