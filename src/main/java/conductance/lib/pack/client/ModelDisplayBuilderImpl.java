package conductance.lib.pack.client;

import net.minecraft.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import conductance.api.resource.ModelDisplayBuilder;

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
			json.add("rotation", Util.make(new JsonArray(), arr -> {
				arr.add(this.rotation[0]);
				arr.add(this.rotation[1]);
				arr.add(this.rotation[2]);
			}));
		}
		if (this.translation.length > 0) {
			json.add("translation", Util.make(new JsonArray(), arr -> {
				arr.add(this.translation[0]);
				arr.add(this.translation[1]);
				arr.add(this.translation[2]);
			}));
		}
		if (this.scale.length > 0) {
			json.add("scale", Util.make(new JsonArray(), arr -> {
				arr.add(this.scale[0]);
				arr.add(this.scale[1]);
				arr.add(this.scale[2]);
			}));
		}
	}
}
