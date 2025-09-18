package conductance.lib.pack.server;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.recipe.event.ShapelessCraftingRecipeBuilder;

final class ShapelessCraftingRecipeBuilderImpl extends AbstractRecipeBuilder<ShapelessCraftingRecipeBuilder> implements ShapelessCraftingRecipeBuilder {

	private final List<JsonElement> ingredients = new ArrayList<>();

	ShapelessCraftingRecipeBuilderImpl(final ItemStack result) {
		super(ResourceLocation.withDefaultNamespace("crafting_shapeless"), result);
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final Ingredient ingredient, final int amount) {
		final JsonElement json = this.encode(ingredient);
		for (int i = 0; i < amount; ++i) {
			this.ingredients.add(json);
		}
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final ItemStack stack, final int amount) {
		final JsonElement json = this.encode(stack);
		for (int i = 0; i < amount; ++i) {
			this.ingredients.add(json);
		}
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final ItemLike item, final int amount) {
		final JsonElement json = this.encode(item);
		for (int i = 0; i < amount; ++i) {
			this.ingredients.add(json);
		}
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final TagKey<Item> tag, final int amount) {
		final JsonElement json = this.encode(tag);
		for (int i = 0; i < amount; ++i) {
			this.ingredients.add(json);
		}
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final ResourceLocation tag, final int amount) {
		final JsonElement json = this.encode(tag);
		for (int i = 0; i < amount; ++i) {
			this.ingredients.add(json);
		}
		return this;
	}

	@Override
	public ShapelessCraftingRecipeBuilder add(final MaterialGenerationHandler handler, final Material material, final int amount) {
		return this.add(CAPI.materials().getItem(material, handler), amount);
	}

	@Override
	protected void populateJson(final JsonObject json) {
		if (this.ingredients.isEmpty()) {
			throw new IllegalArgumentException("No ingredients added");
		}
		json.add("ingredients", Util.make(new JsonArray(), arr -> this.ingredients.forEach(arr::add)));
	}
}
