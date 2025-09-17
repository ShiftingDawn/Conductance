package conductance.lib.pack.client;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import conductance.api.resource.ItemsModelBuilder;
import conductance.api.resource.ItemsModelCompositeBuilder;
import conductance.api.resource.ItemsModelConditionBuilder;
import conductance.api.resource.ItemsModelCustomModelBuilder;
import conductance.api.resource.ItemsModelModelBuilder;
import conductance.api.resource.ItemsModelRangeDispatchBuilder;
import conductance.api.resource.ItemsModelSelectBuilder;

final class ItemsModelBuilderImpl implements ItemsModelBuilder {

	private @Nullable Boolean handAnimationOnSwap;
	private @Nullable Boolean oversizedInGui;
	private @Nullable JsonResourceBuilderImpl<?> model;

	@Override
	public ItemsModelBuilder handAnimationOnSwap(final boolean playAnimation) {
		this.handAnimationOnSwap = playAnimation;
		return this;
	}

	@Override
	public ItemsModelBuilder oversizedInGui(final boolean oversized) {
		this.oversizedInGui = oversized;
		return this;
	}

	@Override
	public void model(final ResourceLocation modelLocation, final Consumer<ItemsModelModelBuilder> builder) {
		this.model = Util.make(new ItemsModelModelBuilderImpl(modelLocation), builder);
	}

	@Override
	public void composite(final Consumer<ItemsModelCompositeBuilder> builder) {
		this.model = Util.make(new ItemsModelCompositeBuilderImpl(), builder);
	}

	@Override
	public void condition(final ResourceLocation property, final Consumer<ItemsModelConditionBuilder> builder) {
		this.model = Util.make(new ItemsModelConditionBuilderImpl(property), builder);
	}

	@Override
	public void select(final ResourceLocation property, final Consumer<ItemsModelSelectBuilder> builder) {
		this.model = Util.make(new ItemsModelSelectBuilderImpl(property), builder);
	}

	@Override
	public void rangeDispatch(final ResourceLocation property, final Consumer<ItemsModelRangeDispatchBuilder> builder) {
		this.model = Util.make(new ItemsModelRangeDispatchBuilderImpl(property), builder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void empty() {
		this.model = new JsonResourceBuilderImpl() {
			@Override
			protected void populateJson(final JsonObject json) {
				json.addProperty("type", ResourceLocation.withDefaultNamespace("empty").toString());
			}
		};
	}

	@Override
	public void custom(final ResourceLocation type, final Consumer<ItemsModelCustomModelBuilder> builder) {
		this.model = Util.make(new ItemsModelCustomModelBuilderImpl(type), builder);
	}

	public JsonObject build() {
		return Util.make(new JsonObject(), json -> {
			if (this.handAnimationOnSwap != null) {
				json.addProperty("hand_animation_on_swap", this.handAnimationOnSwap);
			}
			if (this.oversizedInGui != null) {
				json.addProperty("oversized_in_gui", this.oversizedInGui);
			}
			if (this.model != null) {
				json.add("model", this.model.build());
			}
		});
	}
}
