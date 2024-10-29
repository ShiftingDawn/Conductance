package conductance.api.resource;

import net.minecraft.core.Direction;

public interface ModelElementBuilder<BUILDER extends ModelBuilder<BUILDER>> {

	ModelElementBuilder<BUILDER> from(int x, int y, int z);

	ModelElementBuilder<BUILDER> to(int x, int y, int z);

	ModelElementBuilder<BUILDER> rotation(int originX, int originY, int originZ, Direction.Axis axis, float angle, boolean rescale);

	default ModelElementBuilder<BUILDER> rotation(final int originX, final int originY, final int originZ, final Direction.Axis axis, final float angle) {
		return this.rotation(originX, originY, originZ, axis, angle, false);
	}

	ModelElementBuilder<BUILDER> shade(boolean shade);

	ModelElementBuilder<BUILDER> lightEmission(int lightEmission);

	ModelElementFaceBuilder<BUILDER> face(Direction face);

	BUILDER build();
}
