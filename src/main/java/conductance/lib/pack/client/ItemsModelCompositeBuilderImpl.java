package conductance.lib.pack.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import conductance.api.resource.ItemsModelBuilder;
import conductance.api.resource.ItemsModelCompositeBuilder;

final class ItemsModelCompositeBuilderImpl extends JsonResourceBuilderImpl<ItemsModelCompositeBuilderImpl> implements ItemsModelCompositeBuilder {

	private final List<ItemsModelBuilderImpl> models = new ArrayList<>();

	@Override
	public ItemsModelCompositeBuilder model(final Consumer<ItemsModelBuilder> builder) {
		this.models.add(Util.make(new ItemsModelBuilderImpl(), builder));
		return this;
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.addProperty("type", ResourceLocation.withDefaultNamespace("composite").toString());
		json.add("models", Util.make(new JsonArray(), arr -> this.models.forEach(model -> arr.add(model.build()))));
	}
}
