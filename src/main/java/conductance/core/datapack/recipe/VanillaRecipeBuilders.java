package conductance.core.datapack.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;

abstract class VanillaRecipeBuilders {

	private final ResourceLocation id;
	private final ItemLike resultItem;
	private final int resultItemCount;

	VanillaRecipeBuilders(final ResourceLocation id, final ItemLike resultItem, final int resultItemCount) {
		this.id = id;
		this.resultItem = resultItem;
		this.resultItemCount = resultItemCount;
	}

	protected abstract Recipe<?> build();

	protected final ItemStack getResult() {
		return new ItemStack(this.resultItem, this.resultItemCount);
	}

	public void save(final RecipeOutput output) {
		output.accept(this.id, this.build(), null);
	}

	public static class CraftingShaped extends VanillaRecipeBuilders {

		private final ArrayList<String> rows = new ArrayList<>();
		private final Map<Character, Ingredient> mapping = new HashMap<>();

		CraftingShaped(final ResourceLocation id, final ItemStack result) {
			super(id, result.getItem(), result.getCount());
		}

		public CraftingShaped row(final String row) {
			this.rows.add(row);
			return this;
		}

		public CraftingShaped define(final char character, final Ingredient ingredient) {
			this.mapping.put(character, ingredient);
			return this;
		}

		public CraftingShaped define(final char character, final TagKey<Item> ingredient) {
			this.mapping.put(character, Ingredient.of(ingredient));
			return this;
		}

		public CraftingShaped define(final char character, final ItemStack ingredient) {
			this.mapping.put(character, Ingredient.of(ingredient));
			return this;
		}

		public CraftingShaped define(final char character, final ItemLike ingredient) {
			this.mapping.put(character, Ingredient.of(ingredient));
			return this;
		}

		@Override
		protected Recipe<?> build() {
			return new ShapedRecipe("",
					RecipeBuilder.determineBookCategory(RecipeCategory.MISC),
					ShapedRecipePattern.of(this.mapping, this.rows),
					this.getResult(),
					false);
		}
	}

	public static class CraftingShapeless extends VanillaRecipeBuilders {

		private final NonNullList<Ingredient> inputs = NonNullList.create();

		CraftingShapeless(final ResourceLocation id, final ItemStack result) {
			super(id, result.getItem(), result.getCount());
		}

		public CraftingShapeless add(final Ingredient ingredient) {
			this.inputs.add(ingredient);
			return this;
		}

		public CraftingShapeless add(final TagKey<Item> ingredient) {
			return this.add(Ingredient.of(ingredient));
		}

		public CraftingShapeless add(final ItemStack ingredient) {
			return this.add(Ingredient.of(ingredient));
		}

		public CraftingShapeless add(final ItemLike ingredient) {
			return this.add(Ingredient.of(ingredient));
		}

		@Override
		protected Recipe<?> build() {
			return new ShapelessRecipe("",
					RecipeBuilder.determineBookCategory(RecipeCategory.MISC),
					this.getResult(),
					this.inputs
			);
		}
	}
}
