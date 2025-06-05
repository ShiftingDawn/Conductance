package conductance.core.runtimepack.client;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.VariantProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelElementFaceBuilder;
import conductance.api.util.JsonUtils;

final class ModelElementFaceBuilderImpl implements ModelElementFaceBuilder {

	private int[] uv = new int[0];
	@Nullable
	private String texture;
	@Nullable
	private Direction cullFace;
	@Nullable
	private VariantProperties.Rotation rotation;
	private int tintIndex = -1;

	@Override
	public ModelElementFaceBuilder uv(final int x1, final int y1, final int x2, final int y2) {
		this.uv = new int[] {x1, y1, x2, y2};
		return this;
	}

	@Override
	public ModelElementFaceBuilder texture(final String textureKey) {
		this.texture = '#' + textureKey;
		return this;
	}

	@Override
	public ModelElementFaceBuilder cullFace(final Direction face) {
		this.cullFace = face;
		return this;
	}

	@Override
	public ModelElementFaceBuilder rotation(final VariantProperties.Rotation newRotation) {
		this.rotation = newRotation;
		return this;
	}

	@Override
	public ModelElementFaceBuilder tintIndex(final int newTintIndex) {
		this.tintIndex = newTintIndex;
		return this;
	}

	JsonElement serialize() {
		return Util.make(new JsonObject(), json -> {
			if (this.uv.length > 0) {
				json.add("uv", JsonUtils.toJsonArray(this.uv));
			}
			if (this.texture != null) {
				json.addProperty("texture", this.texture);
			}
			if (this.cullFace != null) {
				json.addProperty("cullface", this.cullFace.getSerializedName());
			}
			if (this.rotation != null) {
				json.addProperty("rotation", switch (this.rotation) {
					case R0 -> 0;
					case R90 -> 90;
					case R180 -> 180;
					case R270 -> 270;
				});
			}
			if (this.tintIndex != -1) {
				json.addProperty("tintindex", this.tintIndex);
			}
		});
	}
}
