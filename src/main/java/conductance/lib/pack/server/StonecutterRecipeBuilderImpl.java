package conductance.lib.pack.server;

import java.util.Objects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.event.StonecutterRecipeBuilder;

final class StonecutterRecipeBuilderImpl extends AbstractRecipeBuilder<StonecutterRecipeBuilder> implements StonecutterRecipeBuilder {

	private final ItemStack result;
	private @Nullable Ingredient ingredient;

	StonecutterRecipeBuilderImpl(final HolderLookup.Provider registries, final ItemStack result) {
		super(registries);
		this.result = result;
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final ItemStack stack) {
		return this.ingredient(Ingredient.of(stack.getItem()));
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final ItemLike item) {
		return this.ingredient(Ingredient.of(item));
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final TagKey<Item> tag) {
		return this.ingredient(Ingredient.of(this.getHolderGetter().getOrThrow(tag)));
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final ResourceLocation tag) {
		return this.ingredient(TagKey.create(Registries.ITEM, tag));
	}

	@Override
	protected void build(final ResourceKey<Recipe<?>> recipeId, final RecipeOutput output) {
		Objects.requireNonNull(this.ingredient, "Ingredient has not been set.");
		SingleItemRecipeBuilder.stonecutting(this.ingredient, RecipeCategory.MISC, this.result.getItem(), this.result.getCount()).unlockedBy(AbstractRecipeBuilder.ADV_NAME, AbstractRecipeBuilder.ADV)
			.save(output, recipeId);
	}
}
