package conductance.lib.pack.server;

import java.util.Objects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
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
import conductance.api.recipe.event.ShapedCraftingRecipeBuilder;

final class ShapedCraftingRecipeBuilderImpl extends AbstractRecipeBuilder<ShapedCraftingRecipeBuilder> implements ShapedCraftingRecipeBuilder {

	private final ShapedRecipeBuilder builder;

	ShapedCraftingRecipeBuilderImpl(final HolderLookup.Provider registries, final ItemStack result) {
		super(registries);
		this.builder = ShapedRecipeBuilder.shaped(this.getHolderGetter(), RecipeCategory.MISC, result).showNotification(false).unlockedBy(AbstractRecipeBuilder.ADV_NAME, AbstractRecipeBuilder.ADV);
	}

	@SuppressWarnings("DataFlowIssue")
	@Override
	public ShapedCraftingRecipeBuilder pattern(final String row1, final String row2, final String row3) {
		this.builder.pattern(row1);
		if (!row2.isEmpty()) {
			this.builder.pattern(row2);
		}
		if (!row3.isEmpty()) {
			this.builder.pattern(row3);
		}
		if (Objects.requireNonNullElse(row1, "").contains("W") || Objects.requireNonNullElse(row2, "").contains("W") || Objects.requireNonNullElse(row3, "").contains("W")) {
			this.key('W', CAPI.TAG_WRENCHES);
		}
		if (Objects.requireNonNullElse(row1, "").contains("H") || Objects.requireNonNullElse(row2, "").contains("H") || Objects.requireNonNullElse(row3, "").contains("H")) {
			this.key('H', CAPI.TAG_HAMMERS);
		}
		if (Objects.requireNonNullElse(row1, "").contains("X") || Objects.requireNonNullElse(row2, "").contains("X") || Objects.requireNonNullElse(row3, "").contains("X")) {
			this.key('X', CAPI.TAG_WIRE_CUTTERS);
		}
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final Ingredient ingredient) {
		this.builder.define(c, ingredient);
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final ItemStack stack) {
		this.key(c, Ingredient.of(stack.getItem()));
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final ItemLike item) {
		this.key(c, Ingredient.of(item));
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final TagKey<Item> tag) {
		this.builder.define(c, tag);
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final ResourceLocation tag) {
		return this.key(c, TagKey.create(Registries.ITEM, tag));
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final MaterialGenerationHandler handler, final Material material) {
		return this.key(c, CAPI.materials().getItemTag(material, handler));
	}

	@Override
	protected void build(final ResourceKey<Recipe<?>> recipeId, final RecipeOutput output) {
		this.builder.save(output, recipeId);
	}
}
