package conductance.block;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.CAPI;
import conductance.client.LuxBlockRenderer;
import conductance.init.ConductanceCreativeTabs;

public class DecoLuxBlock extends ConductanceBlock implements IBlockRendererProvider {

	private final IRenderer renderer;

	public DecoLuxBlock(final BlockBehaviour.Properties properties, final DyeColor dyeColor) {
		super(properties);
		this.renderer = new LuxBlockRenderer(dyeColor);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IRenderer getRenderer(final BlockState state) {
		return this.renderer;
	}

	@Override
	public RegistryEntry<CreativeModeTab, CreativeModeTab> getCreativeTab() {
		return ConductanceCreativeTabs.DECORATION;
	}

	@Override
	public MutableComponent getName() {
		return CAPI.translations().makeLocalizedName(this);
	}
}
