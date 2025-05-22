package conductance.init.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import org.jetbrains.annotations.Nullable;

public class RenderedBlockItem extends BlockItem implements IItemRendererProvider {

	public RenderedBlockItem(final Block block, final Properties properties) {
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

	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public IRenderer getRenderer(final ItemStack stack) {
		if (this.getBlock() instanceof final IBlockRendererProvider provider) {
			return provider.getRenderer(this.getBlock().defaultBlockState());
		}
		return null;
	}
}
