package conductance.block;

import java.util.Map;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import conductance.api.CAPI;
import conductance.api.machine.render.TextureOverrideRenderer;
import conductance.Conductance;

public class SimpleDynamicBlock extends ConductanceBlock implements IBlockRendererProvider {

	private final IRenderer renderer;

	public SimpleDynamicBlock(final Properties properties, final String textureName) {
		super(properties);
		this.renderer = new TextureOverrideRenderer(ResourceLocation.withDefaultNamespace("block/cube_all"), Map.of("all", Conductance.id("block/" + textureName)));
	}

	@Override
	public MutableComponent getName() {
		return CAPI.translations().makeLocalizedName(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IRenderer getRenderer(final BlockState state) {
		return this.renderer;
	}
}
