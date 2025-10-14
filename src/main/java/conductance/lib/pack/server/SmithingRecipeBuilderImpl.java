package conductance.lib.pack.server;

import java.util.Objects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.event.SmithingRecipeBuilder;

final class SmithingRecipeBuilderImpl extends AbstractRecipeBuilder<SmithingRecipeBuilder> implements SmithingRecipeBuilder {

	private final ItemStack result;
	private @Nullable Ingredient base;
	private @Nullable Ingredient template;
	private @Nullable Ingredient addition;

	SmithingRecipeBuilderImpl(final HolderLookup.Provider registries, final ItemStack result) {
		super(registries);
		this.result = result;
	}

	@Override
	public SmithingRecipeBuilder base(final Ingredient ingredient) {
		this.base = ingredient;
		return this;
	}

	@Override
	public SmithingRecipeBuilder base(final ItemStack stack) {
		return this.base(Ingredient.of(stack.getItem()));
	}

	@Override
	public SmithingRecipeBuilder base(final ItemLike item) {
		return this.base(Ingredient.of(item));
	}

	@Override
	public SmithingRecipeBuilder base(final TagKey<Item> tag) {
		return this.base(Ingredient.of(this.getHolderGetter().getOrThrow(tag)));
	}

	@Override
	public SmithingRecipeBuilder base(final ResourceLocation tag) {
		return this.base(TagKey.create(Registries.ITEM, tag));
	}

	@Override
	public SmithingRecipeBuilder template(final Ingredient ingredient) {
		this.template = ingredient;
		return this;
	}

	@Override
	public SmithingRecipeBuilder template(final ItemStack stack) {
		return this.template(Ingredient.of(stack.getItem()));
	}

	@Override
	public SmithingRecipeBuilder template(final ItemLike item) {
		return this.template(Ingredient.of(item));
	}

	@Override
	public SmithingRecipeBuilder template(final TagKey<Item> tag) {
		return this.template(Ingredient.of(this.getHolderGetter().getOrThrow(tag)));
	}

	@Override
	public SmithingRecipeBuilder template(final ResourceLocation tag) {
		return this.template(TagKey.create(Registries.ITEM, tag));
	}

	@Override
	public SmithingRecipeBuilder addition(final Ingredient ingredient) {
		this.addition = ingredient;
		return this;
	}

	@Override
	public SmithingRecipeBuilder addition(final ItemStack stack) {
		return this.addition(Ingredient.of(stack.getItem()));
	}

	@Override
	public SmithingRecipeBuilder addition(final ItemLike item) {
		return this.addition(Ingredient.of(item));
	}

	@Override
	public SmithingRecipeBuilder addition(final TagKey<Item> tag) {
		return this.addition(Ingredient.of(this.getHolderGetter().getOrThrow(tag)));
	}

	@Override
	public SmithingRecipeBuilder addition(final ResourceLocation tag) {
		return this.addition(TagKey.create(Registries.ITEM, tag));
	}

	@Override
	protected void build(final ResourceKey<Recipe<?>> recipeId, final RecipeOutput output) {
		Objects.requireNonNull(this.base, "Base has not been set.");
		Objects.requireNonNull(this.template, "Template has not been set.");
		Objects.requireNonNull(this.addition, "Addition has not been set.");
		SmithingTransformRecipeBuilder.smithing(this.template, this.base, this.addition, RecipeCategory.MISC, this.result.getItem()).unlocks(AbstractRecipeBuilder.ADV_NAME, AbstractRecipeBuilder.ADV)
			.save(output, recipeId);
	}
}
