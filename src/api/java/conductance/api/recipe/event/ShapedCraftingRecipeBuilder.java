package conductance.api.recipe.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface ShapedCraftingRecipeBuilder extends RecipeBuilder<ShapedCraftingRecipeBuilder> {

	ShapedCraftingRecipeBuilder pattern(String row1, String row2, String row3);

	default ShapedCraftingRecipeBuilder pattern(final String row1, final String row2) {
		return this.pattern(row1, row2, "");
	}

	default ShapedCraftingRecipeBuilder pattern(final String row1) {
		return this.pattern(row1, "");
	}

	ShapedCraftingRecipeBuilder key(char c, Ingredient ingredient);

	ShapedCraftingRecipeBuilder key(char c, ItemStack stack);

	ShapedCraftingRecipeBuilder key(char c, ItemLike item);

	ShapedCraftingRecipeBuilder key(char c, TagKey<Item> tag);

	ShapedCraftingRecipeBuilder key(char c, ResourceLocation tag);
}
