package conductance.core.runtimepack.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import conductance.api.resource.CompositeModelBuilder;
import conductance.api.resource.ModelBuilder;
import conductance.api.util.JsonUtils;

final class CompositeModelBuilderImpl implements CompositeModelBuilder {

	private final Map<String, ModelBuilderImpl> children = new ConcurrentHashMap<>();
	private String[] renderOrder = new String[0];

	@Override
	public CompositeModelBuilder child(final String name, final Consumer<ModelBuilder> childBuilder) {
		final ModelBuilderImpl b = new ModelBuilderImpl(ResourceLocation.withDefaultNamespace("block/block"));
		childBuilder.accept(b);
		this.children.put(name, b);
		return this;
	}

	@Override
	public CompositeModelBuilder itemRenderOrder(final String... newOrder) {
		this.renderOrder = newOrder;
		return this;
	}

	void addToJson(final JsonObject json) {
		json.add("children", Util.make(new JsonObject(), childrenJson -> this.children.forEach((name, child) -> {
			childrenJson.add(name, child.build());
		})));
		if (this.renderOrder.length > 0) {
			json.add("item_render_order", JsonUtils.toJsonArray(this.renderOrder));
		}
	}
}
