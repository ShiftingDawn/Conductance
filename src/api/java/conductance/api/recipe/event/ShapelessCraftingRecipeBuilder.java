package conductance.api.recipe.event;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public interface ShapelessCraftingRecipeBuilder extends RecipeBuilder<ShapelessCraftingRecipeBuilder> {

	ShapelessCraftingRecipeBuilder add(Ingredient ingredient, int amount);

	ShapelessCraftingRecipeBuilder add(ItemStack stack, int amount);

	ShapelessCraftingRecipeBuilder add(ItemLike item, int amount);

	default ShapelessCraftingRecipeBuilder add(final Holder<? extends ItemLike> itemHolder, final int amount) {
		return this.add(itemHolder.value(), amount);
	}

	default ShapelessCraftingRecipeBuilder add(final Supplier<? extends ItemLike> itemSupplier, final int amount) {
		return this.add(itemSupplier.get(), amount);
	}

	ShapelessCraftingRecipeBuilder add(TagKey<Item> tag, int amount);

	ShapelessCraftingRecipeBuilder add(ResourceLocation tag, int amount);

	ShapelessCraftingRecipeBuilder add(MaterialGenerationHandler handler, Material material, int amount);

	default ShapelessCraftingRecipeBuilder add(final Ingredient ingredient) {
		return this.add(ingredient, 1);
	}

	default ShapelessCraftingRecipeBuilder add(final ItemStack stack) {
		return this.add(stack, stack.getCount());
	}

	default ShapelessCraftingRecipeBuilder add(final ItemLike item) {
		return this.add(item, 1);
	}

	default ShapelessCraftingRecipeBuilder add(final Holder<? extends ItemLike> itemHolder) {
		return this.add(itemHolder, 1);
	}

	default ShapelessCraftingRecipeBuilder add(final Supplier<? extends ItemLike> itemSupplier) {
		return this.add(itemSupplier, 1);
	}

	default ShapelessCraftingRecipeBuilder add(final TagKey<Item> tag) {
		return this.add(tag, 1);
	}

	default ShapelessCraftingRecipeBuilder add(final ResourceLocation tag) {
		return this.add(tag, 1);
	}

	default ShapelessCraftingRecipeBuilder add(final MaterialGenerationHandler handler, final Material material) {
		return this.add(handler, material, 1);
	}
}
