package conductance.api.resource;

public interface ModelDisplayBuilder<BUILDER extends ModelBuilder<BUILDER>> {

	ModelDisplayBuilder<BUILDER> rotation(int x, int y, int z);

	ModelDisplayBuilder<BUILDER> translation(int x, int y, int z);

	ModelDisplayBuilder<BUILDER> scale(float x, float y, float z);

	BUILDER build();
}
