package conductance.api.resource;

import net.minecraft.resources.ResourceLocation;

public interface RuntimeModelProvider {

	void createBlockState(ResourceLocation blockId, BlockStateBuilder builder, ResourceLocation defaultModelLocation);

	void createBlockModel(ResourceLocation blockId, BlockModelBuilder<?> builder);

	default boolean createItemModel(final ResourceLocation blockId, final ItemModelBuilder<?> builder) {
		return false; //Return false to use the block model
	}
}
