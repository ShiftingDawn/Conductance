package conductance.api.machine.render;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.impl.IModelRenderer;

public class RebakedModelRenderer extends IModelRenderer {

	public RebakedModelRenderer(final ResourceLocation modelLocation) {
		super(modelLocation);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean reBakeCustomQuads() {
		return true;
	}
}
