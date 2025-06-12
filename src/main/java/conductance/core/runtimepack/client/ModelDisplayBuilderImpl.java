package conductance.core.runtimepack.client;

import net.minecraft.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelDisplayBuilder;
import conductance.api.util.JsonUtils;

final class ModelDisplayBuilderImpl extends JsonResourceBuilderImpl<ModelDisplayBuilder> implements ModelDisplayBuilder {

	private int[] rotation = new int[0];
	private int[] translation = new int[0];
	private float[] scale = new float[0];

	@Override
	public ModelDisplayBuilder rotation(final int x, final int y, final int z) {
		this.rotation = new int[] {x, y, z};
		return this;
	}

	@Override
	public ModelDisplayBuilder translation(final int x, final int y, final int z) {
		this.translation = new int[] {x, y, z};
		return this;
	}

	@Override
	public ModelDisplayBuilder scale(final float x, final float y, final float z) {
		this.scale = new float[] {x, y, z};
		return this;
	}

	@Override
	protected void populateJson(final JsonObject json) {
		if (this.rotation.length > 0) {
			json.add("rotation", JsonUtils.toJsonArray(this.rotation));
		}
		if (this.translation.length > 0) {
			json.add("translation", JsonUtils.toJsonArray(this.translation));
		}
		if (this.scale.length > 0) {
			json.add("scale", JsonUtils.toJsonArray(this.scale));
		}
	}
}
