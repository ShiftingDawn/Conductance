package conductance.core.runtimepack.client;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelNeoforgeDataBuilder;

final class ModelNeoforgeDataBuilderImpl extends JsonResourceBuilderImpl<ModelNeoforgeDataBuilder> implements ModelNeoforgeDataBuilder {

	@Nullable
	private Integer color;
	@Nullable
	private Integer blockLight;
	@Nullable
	private Integer skyLight;
	@Nullable
	private Boolean ambientOcclusion;

	@Override
	public ModelNeoforgeDataBuilder color(final int color) {
		this.color = color;
		return this;
	}

	@Override
	public ModelNeoforgeDataBuilder blockLight(final int blockLight) {
		this.blockLight = blockLight;
		return this;
	}

	@Override
	public ModelNeoforgeDataBuilder skyLight(final int skyLight) {
		this.skyLight = skyLight;
		return this;
	}

	@Override
	public ModelNeoforgeDataBuilder ambientOcclusion(final boolean ambientOcclusion) {
		this.ambientOcclusion = ambientOcclusion;
		return this;
	}

	@Override
	protected void populateJson(final JsonObject json) {
		if (this.color != null) {
			json.addProperty("color", this.color);
		}
		if (this.blockLight != null) {
			json.addProperty("block_light", this.blockLight);
		}
		if (this.skyLight != null) {
			json.addProperty("sky_light", this.skyLight);
		}
		if (this.ambientOcclusion != null) {
			json.addProperty("ambient_occlusion", this.ambientOcclusion);
		}
	}
}
