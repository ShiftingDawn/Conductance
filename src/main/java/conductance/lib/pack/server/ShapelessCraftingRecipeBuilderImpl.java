package conductance.lib.pack.server;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.recipe.event.ShapelessCraftingRecipeBuilder;

final class ShapelessCraftingRecipeBuilderImpl extends AbstractRecipeBuilder<ShapelessCraftingRecipeBuilder> implements ShapelessCraftingRecipeBuilder {

	private final ShapelessRecipeBuilder builder;

	ShapelessCraftingRecipeBuilderImpl(final HolderLookup.Provider registries, final ItemStack result) {
		super(registries);
		this.builder = ShapelessRecipeBuilder.shapeless(this.getHolderGetter(), RecipeCategory.MISC, result)
			//TODO implement this properly
			.unlockedBy("dummy", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()));
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final Ingredient ingredient, final int amount) {
		this.builder.requires(ingredient, amount);
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final ItemStack stack, final int amount) {
		this.builder.requires(stack.getItem(), amount);
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final ItemLike item, final int amount) {
		this.builder.requires(item, amount);
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final TagKey<Item> tag, final int amount) {
		for (int i = 0; i < amount; ++i) {
			this.builder.requires(tag);
		}
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final ResourceLocation tag, final int amount) {
		return this.add(TagKey.create(Registries.ITEM, tag), amount);
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final MaterialGenerationHandler handler, final Material material, final int amount) {
		return this.add(CAPI.materials().getItemTag(material, handler), amount);
	}

	@Override
	protected void build(final ResourceKey<Recipe<?>> recipeId, final RecipeOutput output) {
		this.builder.save(output, recipeId);
	}
}
