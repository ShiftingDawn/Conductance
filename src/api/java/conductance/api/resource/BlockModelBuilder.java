package conductance.api.resource;

public interface BlockModelBuilder<BUILDER extends BlockModelBuilder<BUILDER>> extends ModelBuilder<BUILDER> {

	BUILDER ambientOcclusion(boolean ambientOcclusion);
}
