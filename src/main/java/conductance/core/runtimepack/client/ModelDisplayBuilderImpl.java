package conductance.core.runtimepack.client;

import net.minecraft.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelDisplayBuilder;
import conductance.api.util.JsonUtils;

@SuppressWarnings({"ConstantValue", "NotNullFieldNotInitialized"})
final class ModelDisplayBuilderImpl<BUILDER extends ModelBuilderImpl<BUILDER>> implements ModelDisplayBuilder<BUILDER> {

	private final BUILDER builder;
	private int[] rotation;
	private int[] translation;
	private float[] scale;

	ModelDisplayBuilderImpl(final BUILDER builder) {
		this.builder = builder;
	}

	@Override
	public ModelDisplayBuilder<BUILDER> rotation(final int x, final int y, final int z) {
		this.rotation = new int[] {x, y, z};
		return this;
	}

	@Override
	public ModelDisplayBuilder<BUILDER> translation(final int x, final int y, final int z) {
		this.translation = new int[] {x, y, z};
		return this;
	}

	@Override
	public ModelDisplayBuilder<BUILDER> scale(final float x, final float y, final float z) {
		this.scale = new float[] {x, y, z};
		return this;
	}

	@Override
	public BUILDER build() {
		return this.builder;
	}

	@Nullable
	JsonElement serialize() {
		final JsonObject result = Util.make(new JsonObject(), json -> {
			if (this.rotation != null) {
				json.add("rotation", JsonUtils.toJsonArray(this.rotation));
			}
			if (this.translation != null) {
				json.add("translation", JsonUtils.toJsonArray(this.translation));
			}
			if (this.scale != null) {
				json.add("scale", JsonUtils.toJsonArray(this.scale));
			}
		});
		return !result.isEmpty() ? result : null;
	}
}
