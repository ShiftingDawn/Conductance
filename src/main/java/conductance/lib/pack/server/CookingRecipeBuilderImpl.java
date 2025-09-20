package conductance.lib.pack.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import conductance.api.recipe.event.CookingRecipeBuilder;

final class CookingRecipeBuilderImpl extends AbstractRecipeBuilder<CookingRecipeBuilder> implements CookingRecipeBuilder {

	private int cookingTime = 200;
	private float experience = 0;
	private JsonElement ingredient;

	CookingRecipeBuilderImpl(final ResourceLocation recipeType, final ItemStack result) {
		super(recipeType, result);
		if (recipeType.getPath().equals("blasting") || recipeType.getPath().equals("smoking")) {
			this.cookingTime = 100;
		}
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
		this.ingredient = this.encode(ingredient);
		return this;
	}

	@Override
	public CookingRecipeBuilder ingredient(final ItemStack stack) {
		this.ingredient = this.encode(stack);
		return this;
	}

	@Override
	public CookingRecipeBuilder ingredient(final ItemLike item) {
		this.ingredient = this.encode(item);
		return this;
	}

	@Override
	public CookingRecipeBuilder ingredient(final TagKey<Item> tag) {
		this.ingredient = this.encode(tag);
		return this;
	}

	@Override
	public CookingRecipeBuilder ingredient(final ResourceLocation tag) {
		this.ingredient = this.encode(tag);
		return this;
	}

	@SuppressWarnings("ConstantValue")
	@Override
	protected void populateJson(final JsonObject json) {
		if (this.ingredient == null) {
			throw new IllegalStateException("No ingredient set");
		}
		json.addProperty("cookingtime", this.cookingTime);
		json.addProperty("experience", this.experience);
		json.add("ingredient", this.ingredient);
	}
}
