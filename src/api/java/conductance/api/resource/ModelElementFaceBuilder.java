package conductance.api.resource;

import java.util.function.Consumer;
import net.minecraft.core.Direction;
import com.mojang.math.Quadrant;

public interface ModelElementFaceBuilder extends JsonResourceBuilder<ModelElementFaceBuilder> {

	ModelElementFaceBuilder uv(int x1, int y1, int x2, int y2);

	ModelElementFaceBuilder texture(String textureKey);

	default ModelElementFaceBuilder particle() {
		return this.texture("particle");
	}

	ModelElementFaceBuilder cullFace(Direction face);

	ModelElementFaceBuilder rotation(Quadrant rotation);

	ModelElementFaceBuilder tintIndex(int tintIndex);

	ModelElementFaceBuilder neoforgeData(Consumer<ModelNeoforgeDataBuilder> builder);
}
