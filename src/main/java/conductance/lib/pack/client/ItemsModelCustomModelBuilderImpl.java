package conductance.lib.pack.client;

import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.resource.ItemsModelCustomModelBuilder;

@RequiredArgsConstructor
final class ItemsModelCustomModelBuilderImpl extends JsonResourceBuilderImpl<ItemsModelCustomModelBuilder> implements ItemsModelCustomModelBuilder {

	private final ResourceLocation type;

	@Override
	protected void populateJson(final JsonObject json) {
		json.addProperty("type", this.type.toString());
	}
}
