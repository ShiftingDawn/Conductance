package conductance.api.resource;

import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;

public interface RuntimeModelProvider {

	/**
	 * @param blockId              the blockId
	 * @param builderFactory       create a builder for the given ResourceLocation
	 * @param defaultModelLocation the default block model location for convenience
	 * @param prebuilt             consumer to manually insert json
	 */
	void createBlockState(ResourceLocation blockId, Function<ResourceLocation, BlockStateBuilder> builderFactory, ResourceLocation defaultModelLocation, BiConsumer<ResourceLocation, JsonObject> prebuilt);

	/**
	 * @param blockId        the blockId
	 * @param builderFactory create a builder for the given ResourceLocation
	 * @param prebuilt       consumer to manually insert json
	 */
	void createBlockModel(ResourceLocation blockId, Function<ResourceLocation, BlockModelBuilder<?>> builderFactory, BiConsumer<ResourceLocation, JsonObject> prebuilt);

	/**
	 * @param blockId the blockId
	 * @param builder the model builder
	 * @return <code>true</code> to use the builder, or <code>false</code> to use the block model
	 */
	default boolean createItemModel(final ResourceLocation blockId, final ItemModelBuilder<?> builder) {
		return false;
	}
}
