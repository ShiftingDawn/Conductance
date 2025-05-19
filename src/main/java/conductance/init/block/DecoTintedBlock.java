package conductance.init.block;

import java.util.Map;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import conductance.api.CAPI;
import conductance.api.machine.render.TextureOverrideRenderer;
import conductance.Conductance;
import conductance.init.ConductanceCreativeTabs;

public class DecoTintedBlock extends ConductanceBlock implements IBlockRendererProvider {

	private final IRenderer renderer;

	public DecoTintedBlock(final Properties properties, final String textureName) {
		super(properties);
		this.renderer = new TextureOverrideRenderer(Conductance.id("block/cube_all_tinted1"), Map.of("all", Conductance.id("block/" + textureName)));
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
