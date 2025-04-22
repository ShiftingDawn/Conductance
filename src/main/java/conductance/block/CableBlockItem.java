package conductance.block;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

public class CableBlockItem extends PipeBlockItem implements IItemRendererProvider {

	public CableBlockItem(final CableBlock block, final Properties properties) {
		super(block, properties);
	}

	@Override
	public String getDescriptionId() {
		return this.getBlock().getDescriptionId();
	}

	@Override
	public String getDescriptionId(final ItemStack stack) {
		return this.getDescriptionId();
	}

	@Override
	public Component getDescription() {
		return this.getBlock().getName();
	}

	@Override
	public Component getName(final ItemStack stack) {
		return this.getDescription();
	}

	@Override
	public CableBlock getBlock() {
		return (CableBlock) super.getBlock();
	}

	@Override
	public IRenderer getRenderer(final ItemStack stack) {
		return this.getBlock().getRenderer(this.getBlock().defaultBlockState());
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if (stack.getItem() instanceof final CableBlockItem materialBlockItem) {
				return materialBlockItem.getBlock().getColorTint(materialBlockItem.getBlock().defaultBlockState(), null, null, tintIndex);
			}
			return -1;
		};
	}
}
