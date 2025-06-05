package conductance.api.resource;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Direction;

public interface ModelElementBuilder {

	ModelElementBuilder from(int x, int y, int z);

	ModelElementBuilder to(int x, int y, int z);

	ModelElementBuilder rotation(int originX, int originY, int originZ, Direction.Axis axis, float angle, boolean rescale);

	default ModelElementBuilder rotation(final int originX, final int originY, final int originZ, final Direction.Axis axis, final float angle) {
		return this.rotation(originX, originY, originZ, axis, angle, false);
	}

	ModelElementBuilder shade(boolean shade);

	ModelElementBuilder lightEmission(int lightEmission);

	ModelElementBuilder face(Direction face, Consumer<ModelElementFaceBuilder> builder);

	ModelElementBuilder faces(BiConsumer<Direction, ModelElementFaceBuilder> faceBuilder, boolean cull, Direction... faces);
}
