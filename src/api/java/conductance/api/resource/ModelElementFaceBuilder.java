package conductance.api.resource;

import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.VariantProperties;

public interface ModelElementFaceBuilder {

	ModelElementFaceBuilder uv(int x1, int y1, int x2, int y2);

	ModelElementFaceBuilder texture(String textureKey);

	default ModelElementFaceBuilder particle() {
		return this.texture("particle");
	}

	ModelElementFaceBuilder cullFace(Direction face);

	ModelElementFaceBuilder rotation(VariantProperties.Rotation rotation);

	ModelElementFaceBuilder tintIndex(int tintIndex);
}
