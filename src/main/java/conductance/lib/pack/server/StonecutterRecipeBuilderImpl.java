package conductance.lib.pack.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import conductance.api.recipe.event.StonecutterRecipeBuilder;

final class StonecutterRecipeBuilderImpl extends AbstractRecipeBuilder<StonecutterRecipeBuilder> implements StonecutterRecipeBuilder {

	private JsonElement ingredient;

	StonecutterRecipeBuilderImpl(final ItemStack result) {
		super(ResourceLocation.withDefaultNamespace("stonecutting"), result);
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final Ingredient ingredient) {
		this.ingredient = this.encode(ingredient);
		return this;
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final ItemStack stack) {
		this.ingredient = this.encode(stack);
		return this;
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final ItemLike item) {
		this.ingredient = this.encode(item);
		return this;
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final TagKey<Item> tag) {
		this.ingredient = this.encode(tag);
		return this;
	}

	@Override
	public StonecutterRecipeBuilder ingredient(final ResourceLocation tag) {
		this.ingredient = this.encode(tag);
		return this;
	}

	@SuppressWarnings("ConstantValue")
	@Override
	protected void populateJson(final JsonObject json) {
		if (this.ingredient == null) {
			throw new IllegalStateException("No ingredient set");
		}
		json.add("ingredient", this.ingredient);
	}
}
