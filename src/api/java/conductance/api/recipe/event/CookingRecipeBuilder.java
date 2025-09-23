package conductance.api.recipe.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

	public interface CookingRecipeBuilder extends RecipeBuilder<CookingRecipeBuilder> {

	CookingRecipeBuilder time(int time);

	CookingRecipeBuilder experience(float exp);

	CookingRecipeBuilder ingredient(Ingredient ingredient);

	CookingRecipeBuilder ingredient(ItemStack stack);

	CookingRecipeBuilder ingredient(ItemLike item);

	CookingRecipeBuilder ingredient(TagKey<Item> tag);

	CookingRecipeBuilder ingredient(ResourceLocation tag);
}
