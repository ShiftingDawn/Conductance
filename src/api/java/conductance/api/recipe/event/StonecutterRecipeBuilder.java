package conductance.api.recipe.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface StonecutterRecipeBuilder extends RecipeBuilder<StonecutterRecipeBuilder> {

	StonecutterRecipeBuilder ingredient(Ingredient ingredient);

	StonecutterRecipeBuilder ingredient(ItemStack stack);

	StonecutterRecipeBuilder ingredient(ItemLike item);

	StonecutterRecipeBuilder ingredient(TagKey<Item> tag);

	StonecutterRecipeBuilder ingredient(ResourceLocation tag);
}
