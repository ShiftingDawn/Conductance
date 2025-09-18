package conductance.api.recipe.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface TransmuteCraftingRecipeBuilder extends RecipeBuilder<TransmuteCraftingRecipeBuilder> {

	TransmuteCraftingRecipeBuilder input(Ingredient ingredient);

	TransmuteCraftingRecipeBuilder input(ItemStack stack);

	TransmuteCraftingRecipeBuilder input(ItemLike item);

	TransmuteCraftingRecipeBuilder input(TagKey<Item> tag);

	TransmuteCraftingRecipeBuilder input(ResourceLocation tag);

	TransmuteCraftingRecipeBuilder material(Ingredient ingredient);

	TransmuteCraftingRecipeBuilder material(ItemStack stack);

	TransmuteCraftingRecipeBuilder material(ItemLike item);

	TransmuteCraftingRecipeBuilder material(TagKey<Item> tag);

	TransmuteCraftingRecipeBuilder material(ResourceLocation tag);
}
