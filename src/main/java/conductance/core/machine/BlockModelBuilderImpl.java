package conductance.core.machine;

import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.BlockModelBuilder;

public final class BlockModelBuilderImpl extends ModelBuilderImpl<BlockModelBuilderImpl> implements BlockModelBuilder<BlockModelBuilderImpl> {

	@Nullable
	private Boolean ambientOcclusion;

	public BlockModelBuilderImpl() {
		super(ResourceLocation.withDefaultNamespace("block/block"));
	}

	@Override
	public BlockModelBuilderImpl ambientOcclusion(final boolean newAmbientOcclusion) {
		this.ambientOcclusion = newAmbientOcclusion;
		return this;
	}

	@Override
	protected void addJsonProperties(final JsonObject json) {
		if (this.ambientOcclusion != null) {
			json.addProperty("ambientocclusion", this.ambientOcclusion);
		}
	}
}
