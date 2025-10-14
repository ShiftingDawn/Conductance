package conductance.lib.pack.server;

import java.util.Objects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.event.CookingRecipeBuilder;

final class CookingRecipeBuilderImpl<T extends AbstractCookingRecipe> extends AbstractRecipeBuilder<CookingRecipeBuilder> implements CookingRecipeBuilder {

	private final ItemStack result;
	private final AbstractCookingRecipe.Factory<T> recipeFactory;
	private final RecipeSerializer<T> serializer;
	private @Nullable Ingredient ingredient;
	private int cookingTime = 200;
	private float experience = 0;

	CookingRecipeBuilderImpl(final HolderLookup.Provider registries, final ItemStack result, final AbstractCookingRecipe.Factory<T> recipeFactory, final RecipeSerializer<T> serializer) {
		super(registries);
		this.result = result;
		this.recipeFactory = recipeFactory;
		this.serializer = serializer;
	}

	@Override
	public CookingRecipeBuilder time(final int time) {
		this.cookingTime = time;
		return this;
	}

	@Override
	public CookingRecipeBuilder experience(final float exp) {
		this.experience = exp;
		return this;
	}

	@Override
	public CookingRecipeBuilder ingredient(final Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	@Override
	public CookingRecipeBuilder ingredient(final ItemStack stack) {
		return this.ingredient(Ingredient.of(stack.getItem()));
	}

	@Override
	public CookingRecipeBuilder ingredient(final ItemLike item) {
		return this.ingredient(Ingredient.of(item));
	}

	@Override
	public CookingRecipeBuilder ingredient(final TagKey<Item> tag) {
		return this.ingredient(Ingredient.of(this.getHolderGetter().getOrThrow(tag)));
	}

	@Override
	public CookingRecipeBuilder ingredient(final ResourceLocation tag) {
		return this.ingredient(TagKey.create(Registries.ITEM, tag));
	}

	@Override
	protected void build(final ResourceKey<Recipe<?>> recipeId, final RecipeOutput output) {
		Objects.requireNonNull(this.ingredient, "Ingredient has not been set");
		SimpleCookingRecipeBuilder.generic(this.ingredient, RecipeCategory.MISC, this.result, this.experience, this.cookingTime, this.serializer, this.recipeFactory).unlockedBy(AbstractRecipeBuilder.ADV_NAME,
			AbstractRecipeBuilder.ADV).save(output, recipeId);
	}
}
