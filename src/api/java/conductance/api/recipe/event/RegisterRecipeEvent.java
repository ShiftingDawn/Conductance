package conductance.api.recipe.event;

import java.util.function.Consumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.recipe.MachineRecipeType;

public interface RegisterRecipeEvent extends IConductancePluginEvent {

	ResourceLocation id(String recipeType, String recipePath);

	ResourceLocation id(String recipePath);

	void create(ResourceLocation recipeId, MachineRecipeType type, Consumer<MachineRecipeBuilder> builder);

	default void create(final String recipeId, final MachineRecipeType type, final Consumer<MachineRecipeBuilder> builder) {
		this.create(this.id(type.getId().getPath(), recipeId), type, builder);
	}

	void shaped(ResourceLocation recipeId, ItemStack result, Consumer<ShapedCraftingRecipeBuilder> builder);

	void shaped(ResourceLocation recipeId, ItemLike result, Consumer<ShapedCraftingRecipeBuilder> builder);

	default void shaped(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void shaped(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void shaped(final String recipeId, final ItemStack result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(this.id("crafting_shaped", recipeId), result, builder);
	}

	default void shaped(final String recipeId, final ItemLike result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(this.id("crafting_shaped", recipeId), result, builder);
	}

	default void shaped(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void shaped(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void shaped(final ItemStack result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(), result, builder);
	}

	default void shaped(final ItemLike result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(BuiltInRegistries.ITEM.getKey(result.asItem()).getPath(), result, builder);
	}

	default void shaped(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void shaped(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	void shapeless(ResourceLocation recipeId, ItemStack result, Consumer<ShapelessCraftingRecipeBuilder> builder);

	void shapeless(ResourceLocation recipeId, ItemLike result, Consumer<ShapelessCraftingRecipeBuilder> builder);

	default void shapeless(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void shapeless(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void shapeless(final String recipeId, final ItemStack result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(this.id("crafting_shapeless", recipeId), result, builder);
	}

	default void shapeless(final String recipeId, final ItemLike result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(this.id("crafting_shapeless", recipeId), result, builder);
	}

	default void shapeless(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void shapeless(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void shapeless(final ItemStack result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(), result, builder);
	}

	default void shapeless(final ItemLike result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(BuiltInRegistries.ITEM.getKey(result.asItem()).getPath(), result, builder);
	}

	default void shapeless(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void shapeless(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	void transmute(ResourceLocation recipeId, ItemStack result, Consumer<TransmuteCraftingRecipeBuilder> builder);

	void transmute(ResourceLocation recipeId, ItemLike result, Consumer<TransmuteCraftingRecipeBuilder> builder);

	default void transmute(final String recipeId, final ItemStack result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		this.transmute(this.id("crafting_transmute", recipeId), result, builder);
	}

	default void transmute(final String recipeId, final ItemLike result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		this.transmute(this.id("crafting_transmute", recipeId), result, builder);
	}

	void smelting(ResourceLocation recipeId, ItemStack result, Consumer<CookingRecipeBuilder> builder);

	void smelting(ResourceLocation recipeId, ItemLike result, Consumer<CookingRecipeBuilder> builder);

	default void smelting(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void smelting(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void smelting(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(this.id("smelting", recipeId), result, builder);
	}

	default void smelting(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(this.id("smelting", recipeId), result, builder);
	}

	default void smelting(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(this.id("smelting", recipeId), resultHandler, resultMaterial, builder);
	}

	default void smelting(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(this.id("smelting", recipeId), resultHandler, resultMaterial, resultAmount, builder);
	}

	default void smelting(final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(), result, builder);
	}

	default void smelting(final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(BuiltInRegistries.ITEM.getKey(result.asItem()), result, builder);
	}

	default void smelting(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void smelting(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	void blasting(ResourceLocation recipeId, ItemStack result, Consumer<CookingRecipeBuilder> builder);

	void blasting(ResourceLocation recipeId, ItemLike result, Consumer<CookingRecipeBuilder> builder);

	default void blasting(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void blasting(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void blasting(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(this.id("blasting", recipeId), result, builder);
	}

	default void blasting(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(this.id("blasting", recipeId), result, builder);
	}

	default void blasting(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(this.id("blasting", recipeId), resultHandler, resultMaterial, builder);
	}

	default void blasting(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(this.id("blasting", recipeId), resultHandler, resultMaterial, resultAmount, builder);
	}

	default void blasting(final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(), result, builder);
	}

	default void blasting(final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(BuiltInRegistries.ITEM.getKey(result.asItem()), result, builder);
	}

	default void blasting(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void blasting(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	void smoking(ResourceLocation recipeId, ItemStack result, Consumer<CookingRecipeBuilder> builder);

	void smoking(ResourceLocation recipeId, ItemLike result, Consumer<CookingRecipeBuilder> builder);

	default void smoking(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void smoking(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void smoking(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(this.id("smoking", recipeId), result, builder);
	}

	default void smoking(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(this.id("smoking", recipeId), result, builder);
	}

	default void smoking(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(this.id("smoking", recipeId), resultHandler, resultMaterial, builder);
	}

	default void smoking(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(this.id("smoking", recipeId), resultHandler, resultMaterial, resultAmount, builder);
	}

	default void smoking(final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(), result, builder);
	}

	default void smoking(final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(BuiltInRegistries.ITEM.getKey(result.asItem()), result, builder);
	}

	default void smoking(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void smoking(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	void campfire(ResourceLocation recipeId, ItemStack result, Consumer<CookingRecipeBuilder> builder);

	void campfire(ResourceLocation recipeId, ItemLike result, Consumer<CookingRecipeBuilder> builder);

	default void campfire(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void campfire(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void campfire(final String recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(this.id("campfire_cooking", recipeId), result, builder);
	}

	default void campfire(final String recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(this.id("campfire_cooking", recipeId), result, builder);
	}

	default void campfire(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(this.id("campfire_cooking", recipeId), resultHandler, resultMaterial, builder);
	}

	default void campfire(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(this.id("campfire_cooking", recipeId), resultHandler, resultMaterial, resultAmount, builder);
	}

	default void campfire(final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(), result, builder);
	}

	default void campfire(final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(BuiltInRegistries.ITEM.getKey(result.asItem()), result, builder);
	}

	default void campfire(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void campfire(final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	void stonecutting(ResourceLocation recipeId, ItemStack result, Consumer<StonecutterRecipeBuilder> builder);

	void stonecutting(ResourceLocation recipeId, ItemLike result, Consumer<StonecutterRecipeBuilder> builder);

	default void stonecutting(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void stonecutting(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void stonecutting(final String recipeId, final ItemStack result, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(this.id("stonecutting", recipeId), result, builder);
	}

	default void stonecutting(final String recipeId, final ItemLike result, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(this.id("stonecutting", recipeId), result, builder);
	}

	default void stonecutting(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void stonecutting(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	void smithingTransform(ResourceLocation recipeId, ItemStack result, Consumer<SmithingRecipeBuilder> builder);

	void smithingTransform(ResourceLocation recipeId, ItemLike result, Consumer<SmithingRecipeBuilder> builder);

	default void smithingTransform(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void smithingTransform(final ResourceLocation recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}

	default void smithingTransform(final String recipeId, final ItemStack result, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(this.id("smithing_transform", recipeId), result, builder);
	}

	default void smithingTransform(final String recipeId, final ItemLike result, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(this.id("smithing_transform", recipeId), result, builder);
	}

	default void smithingTransform(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler), builder);
	}

	default void smithingTransform(final String recipeId, final MaterialGenerationHandler resultHandler, final Material resultMaterial, final int resultAmount, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(recipeId, CAPI.materials().getItem(resultMaterial, resultHandler, resultAmount), builder);
	}
}
