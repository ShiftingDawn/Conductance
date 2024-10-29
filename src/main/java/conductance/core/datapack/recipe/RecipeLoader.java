package conductance.core.datapack.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import conductance.Conductance;

final class RecipeLoader {

	public static void init(final RecipeOutput output) {
		MaterialRecipes.add(output);
	}

	@SuppressWarnings("unchecked")
	public static void shaped(final RecipeOutput output, final String name, final ItemStack result, final Object... recipe) {
		final VanillaRecipeBuilders.CraftingShaped builder = new VanillaRecipeBuilders.CraftingShaped(Conductance.id("crafting/" + name), result);
		for (int i = 0; i < recipe.length; ++i) {
			if (recipe[i] instanceof final String str) {
				builder.row(str);
			} else if (recipe[i++] instanceof final Character character) {
				if (recipe.length <= i) {
					throw new IllegalStateException("Unexpected end of recipe inputs");
				}
				//TODO add crafting tool lookup
				switch (recipe[i]) {
					case final Ingredient ingredient -> builder.define(character, ingredient);
					case final ItemStack ingredient -> builder.define(character, ingredient);
					case final TagKey<?> itemTag -> builder.define(character, (TagKey<Item>) itemTag);
					case final ItemLike itemLike -> builder.define(character, itemLike);
					case null, default -> throw new IllegalArgumentException("Invalid recipe input " + (recipe[i] == null ? "null" : recipe[i].getClass().getName()) + " at position " + i);
				}
			}
		}
		builder.save(output);
	}

	@SuppressWarnings("unchecked")
	public static void shapeless(final RecipeOutput output, final String name, final ItemStack result, final Object... recipe) {
		final VanillaRecipeBuilders.CraftingShapeless builder = new VanillaRecipeBuilders.CraftingShapeless(Conductance.id("crafting/" + name), result);
		for (int i = 0; i < recipe.length; ++i) {
			if (recipe[i] instanceof final Ingredient ingredient) {
				builder.add(ingredient);
			} else if (recipe[i] instanceof final ItemStack itemStack) {
				for (int j = 0; j < itemStack.getCount(); ++j) {
					builder.add(itemStack.copyWithCount(1));
				}
			} else if (recipe[i] instanceof final TagKey<?> itemTag) {
				final int inputAmount = i + 1 < recipe.length && recipe[i + 1] instanceof Integer ? (int) recipe[++i] : 1;
				for (int j = 0; j < inputAmount; ++j) {
					builder.add((TagKey<Item>) itemTag);
				}
			} else if (recipe[i] instanceof final ItemLike itemLike) {
				final int inputAmount = i + 1 < recipe.length && recipe[i + 1] instanceof Integer ? (int) recipe[++i] : 1;
				for (int j = 0; j < inputAmount; ++j) {
					builder.add(itemLike);
				}
			} else {
				throw new IllegalArgumentException("Invalid recipe input " + (recipe[i] == null ? "null" : recipe[i].getClass().getName()) + " at position " + i);
			}
		}
		builder.save(output);
	}

	private RecipeLoader() {
	}
}
