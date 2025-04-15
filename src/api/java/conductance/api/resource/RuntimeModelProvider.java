package conductance.api.resource;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;

public interface RuntimeModelProvider {

	void createBlockState(ResourceLocation blockId, BlockStateBuilder builder, ResourceLocation defaultModelLocation, Consumer<JsonObject> prebuilt);

	void createBlockModel(ResourceLocation blockId, BlockModelBuilder<?> builder, Consumer<JsonObject> prebuilt);

	default boolean createItemModel(final ResourceLocation blockId, final ItemModelBuilder<?> builder) {
		return false; //Return false to use the block model
	}
}
