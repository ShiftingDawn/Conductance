package conductance.lib.pack.server;

import java.util.Objects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.event.TransmuteCraftingRecipeBuilder;

final class TransmuteCraftingRecipeBuilderImpl extends AbstractRecipeBuilder<TransmuteCraftingRecipeBuilder> implements TransmuteCraftingRecipeBuilder {

	private final ItemStack result;
	private @Nullable Ingredient input;
	private @Nullable Ingredient material;

	TransmuteCraftingRecipeBuilderImpl(final HolderLookup.Provider registries, final ItemStack result) {
		super(registries);
		this.result = result;
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final Ingredient ingredient) {
		this.input = ingredient;
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final ItemStack stack) {
		return this.input(Ingredient.of(stack.getItem()));
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final ItemLike item) {
		return this.input(Ingredient.of(item));
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final TagKey<Item> tag) {
		return this.input(Ingredient.of(this.getHolderGetter().getOrThrow(tag)));
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final ResourceLocation tag) {
		return this.input(TagKey.create(Registries.ITEM, tag));
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final Ingredient ingredient) {
		this.material = ingredient;
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final ItemStack stack) {
		return this.material(Ingredient.of(stack.getItem()));
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final ItemLike item) {
		return this.material(Ingredient.of(item));
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final TagKey<Item> tag) {
		return this.material(Ingredient.of(this.getHolderGetter().getOrThrow(tag)));
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final ResourceLocation tag) {
		return this.material(TagKey.create(Registries.ITEM, tag));
	}

	@Override
	protected void build(final ResourceKey<Recipe<?>> recipeId, final RecipeOutput output) {
		Objects.requireNonNull(this.input, "Input has not been set.");
		Objects.requireNonNull(this.material, "'Material' has not been set.");
		TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, this.input, this.material, this.result.getItem()).unlockedBy(AbstractRecipeBuilder.ADV_NAME, AbstractRecipeBuilder.ADV).save(output, recipeId);
	}
}
