package conductance.lib.pack.client;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.resource.ItemsModelModelBuilder;
import conductance.api.resource.ItemsModelTintsBuilder;

@RequiredArgsConstructor
final class ItemsModelModelBuilderImpl extends JsonResourceBuilderImpl<ItemsModelModelBuilderImpl> implements ItemsModelModelBuilder {

	private final ResourceLocation modelLocation;
	private @Nullable ItemsModelTintsBuilderImpl tints;

	@Override
	public ItemsModelModelBuilder tints(final Consumer<ItemsModelTintsBuilder> builder) {
		if (this.tints == null) {
			this.tints = new ItemsModelTintsBuilderImpl();
		}
		builder.accept(this.tints);
		return this;
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.addProperty("type", ResourceLocation.withDefaultNamespace("model").toString());
		json.addProperty("model", this.modelLocation.toString());
		if (this.tints != null) {
			json.add("tints", this.tints.build());
		}
	}
}
