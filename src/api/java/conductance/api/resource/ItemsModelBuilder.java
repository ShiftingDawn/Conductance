package conductance.api.resource;

import java.util.function.Consumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface ItemsModelBuilder {

	ItemsModelBuilder handAnimationOnSwap(boolean playAnimation);

	ItemsModelBuilder oversizedInGui(boolean oversized);

	default void simple(final ResourceLocation modelLocation) {
		this.model(modelLocation, model -> {
		});
	}

	default void simple(final Item item) {
		this.model(item, model -> {
		});
	}

	void model(ResourceLocation modelLocation, Consumer<ItemsModelModelBuilder> builder);

	default void model(final Item item, final Consumer<ItemsModelModelBuilder> builder) {
		this.model(BuiltInRegistries.ITEM.getKey(item).withPrefix("item/"), builder);
	}

	void composite(Consumer<ItemsModelCompositeBuilder> builder);

	void condition(ResourceLocation property, Consumer<ItemsModelConditionBuilder> builder);

	default void condition(final String property, final Consumer<ItemsModelConditionBuilder> builder) {
		this.condition(ResourceLocation.parse(property), builder);
	}

	void select(ResourceLocation property, Consumer<ItemsModelSelectBuilder> builder);

	default void select(final String property, final Consumer<ItemsModelSelectBuilder> builder) {
		this.select(ResourceLocation.parse(property), builder);
	}

	void rangeDispatch(ResourceLocation property, Consumer<ItemsModelRangeDispatchBuilder> builder);

	default void rangeDispatch(final String property, final Consumer<ItemsModelRangeDispatchBuilder> builder) {
		this.rangeDispatch(ResourceLocation.parse(property), builder);
	}

	void empty();

	void custom(ResourceLocation type, Consumer<ItemsModelCustomModelBuilder> builder);
}
