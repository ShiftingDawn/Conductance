package conductance.api.resource;

import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.VariantProperties;

public interface ModelElementFaceBuilder<BUILDER extends ModelBuilder<BUILDER>> {

	ModelElementFaceBuilder<BUILDER> uv(int x1, int y1, int x2, int y2);

	ModelElementFaceBuilder<BUILDER> texture(String textureKey);

	default ModelElementFaceBuilder<BUILDER> particle() {
		return this.texture("particle");
	}

	ModelElementFaceBuilder<BUILDER> cullFace(Direction face);

	ModelElementFaceBuilder<BUILDER> rotation(VariantProperties.Rotation rotation);

	ModelElementFaceBuilder<BUILDER> tintIndex(int tintIndex);

	ModelElementBuilder<BUILDER> build();
}
