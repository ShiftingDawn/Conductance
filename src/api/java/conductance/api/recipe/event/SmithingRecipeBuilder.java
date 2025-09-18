package conductance.api.recipe.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface SmithingRecipeBuilder extends RecipeBuilder<SmithingRecipeBuilder> {

	SmithingRecipeBuilder base(Ingredient ingredient);

	SmithingRecipeBuilder base(ItemStack stack);

	SmithingRecipeBuilder base(ItemLike item);

	SmithingRecipeBuilder base(TagKey<Item> tag);

	SmithingRecipeBuilder base(ResourceLocation tag);

	SmithingRecipeBuilder template(Ingredient ingredient);

	SmithingRecipeBuilder template(ItemStack stack);

	SmithingRecipeBuilder template(ItemLike item);

	SmithingRecipeBuilder template(TagKey<Item> tag);

	SmithingRecipeBuilder template(ResourceLocation tag);

	SmithingRecipeBuilder addition(Ingredient ingredient);

	SmithingRecipeBuilder addition(ItemStack stack);

	SmithingRecipeBuilder addition(ItemLike item);

	SmithingRecipeBuilder addition(TagKey<Item> tag);

	SmithingRecipeBuilder addition(ResourceLocation tag);
}
