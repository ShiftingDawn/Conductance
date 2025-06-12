package conductance.api.resource;

public interface ModelDisplayBuilder extends JsonResourceBuilder<ModelDisplayBuilder> {

	ModelDisplayBuilder rotation(int x, int y, int z);

	ModelDisplayBuilder translation(int x, int y, int z);

	ModelDisplayBuilder scale(float x, float y, float z);
}
