package conductance.api.resource;

public interface ModelNeoforgeDataBuilder extends JsonResourceBuilder<ModelNeoforgeDataBuilder> {

	ModelNeoforgeDataBuilder color(int color);

	ModelNeoforgeDataBuilder blockLight(int blockLight);

	ModelNeoforgeDataBuilder skyLight(int skyLight);

	ModelNeoforgeDataBuilder ambientOcclusion(boolean ambientOcclusion);
}
