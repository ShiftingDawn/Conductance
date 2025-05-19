package conductance.init.block;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.CAPI;
import conductance.Conductance;
import conductance.client.EmissiveOverlayRenderer;
import conductance.init.ConductanceCreativeTabs;

public class DecoEmissiveBlock extends ConductanceBlock implements IBlockRendererProvider {

	private final IRenderer renderer;

	public DecoEmissiveBlock(final Properties properties, final String texture) {
		super(properties);
		this.renderer = new EmissiveOverlayRenderer(
				Conductance.id("block/decoration/emissive/%s_base".formatted(texture)),
				Conductance.id("block/decoration/emissive/%s_overlay".formatted(texture))
		);
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
