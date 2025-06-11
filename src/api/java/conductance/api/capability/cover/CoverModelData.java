package conductance.api.capability.cover;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelProperty;

@OnlyIn(Dist.CLIENT)
public final class CoverModelData {

	public static final ModelProperty<CoverManager> MODEL_PROPERTY = new ModelProperty<>();

	private CoverModelData() {
	}
}
