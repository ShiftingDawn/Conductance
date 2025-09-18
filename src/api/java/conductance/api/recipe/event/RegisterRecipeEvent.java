package conductance.api.recipe.event;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterRecipeEvent extends IConductancePluginEvent {

	ResourceLocation id(String recipeType, String recipePath);

	ResourceLocation id(String recipePath);

	void shaped(ResourceLocation recipeId, ItemStack result, Consumer<ShapedCraftingRecipeBuilder> builder);

	void shaped(ResourceLocation recipeId, ItemLike result, Consumer<ShapedCraftingRecipeBuilder> builder);

	default void shaped(final String recipeId, final ItemStack result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(this.id("crafting_shaped", recipeId), result, builder);
	}

	default void shaped(final String recipeId, final ItemLike result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(this.id("crafting_shaped", recipeId), result, builder);
	}

	void shapeless(ResourceLocation recipeId, ItemStack result, Consumer<ShapelessCraftingRecipeBuilder> builder);

	void shapeless(ResourceLocation recipeId, ItemLike result, Consumer<ShapelessCraftingRecipeBuilder> builder);

	default void shapeless(final String recipeId, final ItemStack result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(this.id("crafting_shapeless", recipeId), result, builder);
	}

	default void shapeless(final String recipeId, final ItemLike result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(this.id("crafting_shapeless", recipeId), result, builder);
	}

	void transmute(ResourceLocation recipeId, ItemStack result, Consumer<TransmuteCraftingRecipeBuilder> builder);

	void transmute(ResourceLocation recipeId, ItemLike result, Consumer<TransmuteCraftingRecipeBuilder> builder);

	default void transmute(final String recipeId, final ItemStack result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		this.transmute(this.id("crafting_transmute", recipeId), result, builder);
	}

	default void transmute(final String recipeId, final ItemLike result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		this.transmute(this.id("crafting_transmute", recipeId), result, builder);
	}

	void cooking(ResourceLocation recipeType, ResourceLocation recipeId, ItemStack result, Consumer<CookingRecipeBuilder> builder);

	void cooking(ResourceLocation recipeType, ResourceLocation recipeId, ItemLike result, Consumer<CookingRecipeBuilder> builder);

	default void cooking(final ResourceLocation recipeType, final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(recipeType, this.id(recipeType.getPath(), recipeId), result, builder);
	}

	default void cooking(final ResourceLocation recipeType, final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(recipeType, this.id(recipeType.getPath(), recipeId), result, builder);
	}

	default void smelting(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smelting"), recipeId, result, builder);
	}

	default void smelting(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smelting"), recipeId, result, builder);
	}

	default void smelting(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smelting"), recipeId, result, builder);
	}

	default void smelting(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smelting"), recipeId, result, builder);
	}

	default void blasting(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("blasting"), recipeId, result, builder);
	}

	default void blasting(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("blasting"), recipeId, result, builder);
	}

	default void blasting(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("blasting"), recipeId, result, builder);
	}

	default void blasting(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("blasting"), recipeId, result, builder);
	}

	default void smoking(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smoking"), recipeId, result, builder);
	}

	default void smoking(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smoking"), recipeId, result, builder);
	}

	default void smoking(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smoking"), recipeId, result, builder);
	}

	default void smoking(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("smoking"), recipeId, result, builder);
	}

	default void campfire(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("campfire_cooking"), recipeId, result, builder);
	}

	default void campfire(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("campfire_cooking"), recipeId, result, builder);
	}


	default void campfire(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("campfire_cooking"), recipeId, result, builder);
	}

	default void campfire(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.cooking(ResourceLocation.withDefaultNamespace("campfire_cooking"), recipeId, result, builder);
	}

	void stonecutting(ResourceLocation recipeId, ItemStack result, Consumer<StonecutterRecipeBuilder> builder);

	void stonecutting(ResourceLocation recipeId, ItemLike result, Consumer<StonecutterRecipeBuilder> builder);

	default void stonecutting(final String recipeId, final ItemStack result, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(this.id("stonecutting", recipeId), result, builder);
	}

	default void stonecutting(final String recipeId, final ItemLike result, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(this.id("stonecutting", recipeId), result, builder);
	}

	void smithingTransform(ResourceLocation recipeId, ItemStack result, Consumer<SmithingRecipeBuilder> builder);

	void smithingTransform(ResourceLocation recipeId, ItemLike result, Consumer<SmithingRecipeBuilder> builder);

	default void smithingTransform(final String recipeId, final ItemStack result, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(this.id("smithing_transform", recipeId), result, builder);
	}

	default void smithingTransform(final String recipeId, final ItemLike result, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(this.id("smithing_transform", recipeId), result, builder);
	}
}
