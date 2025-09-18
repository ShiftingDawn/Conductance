package conductance.lib.pack.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import conductance.api.recipe.event.TransmuteCraftingRecipeBuilder;

final class TransmuteCraftingRecipeBuilderImpl extends AbstractRecipeBuilder<TransmuteCraftingRecipeBuilder> implements TransmuteCraftingRecipeBuilder {

	private JsonElement input;
	private JsonElement material;

	TransmuteCraftingRecipeBuilderImpl(final ItemStack result) {
		super(ResourceLocation.withDefaultNamespace("crafting_transmute"), result);
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final Ingredient ingredient) {
		this.input = this.encode(ingredient);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final ItemStack stack) {
		this.input = this.encode(stack);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final ItemLike item) {
		this.input = this.encode(item);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final TagKey<Item> tag) {
		this.input = this.encode(tag);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder input(final ResourceLocation tag) {
		this.input = this.encode(tag);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final Ingredient ingredient) {
		this.material = this.encode(ingredient);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final ItemStack stack) {
		this.material = this.encode(stack);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final ItemLike item) {
		this.material = this.encode(item);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final TagKey<Item> tag) {
		this.material = this.encode(tag);
		return this;
	}

	@Override
	public TransmuteCraftingRecipeBuilder material(final ResourceLocation tag) {
		this.material = this.encode(tag);
		return this;
	}

	@SuppressWarnings("ConstantValue")
	@Override
	protected void populateJson(final JsonObject json) {
		if (this.input == null) {
			throw new IllegalStateException("No input set");
		}
		if (this.material == null) {
			throw new IllegalStateException("No input set");
		}
		json.add("input", this.input);
		json.add("material", this.material);
	}
}
