package conductance.init.block;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

public class WireBlockItem extends PipeBlockItem implements IItemRendererProvider {

	public WireBlockItem(final WireBlock block, final Properties properties) {
		super(block, properties);
	}

	@Override
	public WireBlock getBlock() {
		return (WireBlock) super.getBlock();
	}

	@Override
	public IRenderer getRenderer(final ItemStack stack) {
		return this.getBlock().getRenderer(this.getBlock().defaultBlockState());
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
