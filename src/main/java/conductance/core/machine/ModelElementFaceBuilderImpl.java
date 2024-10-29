package conductance.core.machine;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.VariantProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelElementBuilder;
import conductance.api.resource.ModelElementFaceBuilder;
import conductance.api.util.SerializationHelper;

final class ModelElementFaceBuilderImpl<BUILDER extends ModelBuilderImpl<BUILDER>> implements ModelElementFaceBuilder<BUILDER> {

	private final ModelElementBuilder<BUILDER> builder;
	private int[] uv;
	@Nullable
	private String texture;
	@Nullable
	private Direction cullFace;
	@Nullable
	private VariantProperties.Rotation rotation;
	private int tintIndex = -1;

	ModelElementFaceBuilderImpl(final ModelElementBuilder<BUILDER> builder) {
		this.builder = builder;
	}

	@Override
	public ModelElementFaceBuilder<BUILDER> uv(final int x1, final int y1, final int x2, final int y2) {
		this.uv = new int[] {x1, y1, x2, y2};
		return this;
	}

	@Override
	public ModelElementFaceBuilder<BUILDER> texture(final String textureKey) {
		this.texture = '#' + textureKey;
		return this;
	}

	@Override
	public ModelElementFaceBuilder<BUILDER> cullFace(final Direction face) {
		this.cullFace = face;
		return this;
	}

	@Override
	public ModelElementFaceBuilder<BUILDER> rotation(final VariantProperties.Rotation newRotation) {
		this.rotation = newRotation;
		return this;
	}

	@Override
	public ModelElementFaceBuilder<BUILDER> tintIndex(final int newTintIndex) {
		this.tintIndex = newTintIndex;
		return this;
	}

	@Override
	public ModelElementBuilder<BUILDER> build() {
		return this.builder;
	}

	JsonElement serialize() {
		return Util.make(new JsonObject(), json -> {
			if (this.uv != null) {
				json.add("uv", SerializationHelper.toJsonArray(this.uv));
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
