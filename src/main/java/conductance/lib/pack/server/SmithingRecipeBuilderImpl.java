package conductance.lib.pack.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import conductance.api.recipe.event.SmithingRecipeBuilder;

final class SmithingRecipeBuilderImpl extends AbstractRecipeBuilder<SmithingRecipeBuilder> implements SmithingRecipeBuilder {

	private JsonElement base;
	private JsonElement template;
	private JsonElement addition;

	SmithingRecipeBuilderImpl(final ItemStack result) {
		super(ResourceLocation.withDefaultNamespace("stonecutting"), result);
	}

	@Override
	public SmithingRecipeBuilder base(final Ingredient ingredient) {
		this.base = this.encode(ingredient);
		return this;
	}

	@Override
	public SmithingRecipeBuilder base(final ItemStack stack) {
		this.base = this.encode(stack);
		return this;
	}

	@Override
	public SmithingRecipeBuilder base(final ItemLike item) {
		this.base = this.encode(item);
		return this;
	}

	@Override
	public SmithingRecipeBuilder base(final TagKey<Item> tag) {
		this.base = this.encode(tag);
		return this;
	}

	@Override
	public SmithingRecipeBuilder base(final ResourceLocation tag) {
		this.base = this.encode(tag);
		return this;
	}

	@Override
	public SmithingRecipeBuilder template(final Ingredient ingredient) {
		this.template = this.encode(ingredient);
		return this;
	}

	@Override
	public SmithingRecipeBuilder template(final ItemStack stack) {
		this.template = this.encode(stack);
		return this;
	}

	@Override
	public SmithingRecipeBuilder template(final ItemLike item) {
		this.template = this.encode(item);
		return this;
	}

	@Override
	public SmithingRecipeBuilder template(final TagKey<Item> tag) {
		this.template = this.encode(tag);
		return this;
	}

	@Override
	public SmithingRecipeBuilder template(final ResourceLocation tag) {
		this.template = this.encode(tag);
		return this;
	}

	@Override
	public SmithingRecipeBuilder addition(final Ingredient ingredient) {
		this.addition = this.encode(ingredient);
		return this;
	}

	@Override
	public SmithingRecipeBuilder addition(final ItemStack stack) {
		this.addition = this.encode(stack);
		return this;
	}

	@Override
	public SmithingRecipeBuilder addition(final ItemLike item) {
		this.addition = this.encode(item);
		return this;
	}

	@Override
	public SmithingRecipeBuilder addition(final TagKey<Item> tag) {
		this.addition = this.encode(tag);
		return this;
	}

	@Override
	public SmithingRecipeBuilder addition(final ResourceLocation tag) {
		this.addition = this.encode(tag);
		return this;
	}

	@SuppressWarnings("ConstantValue")
	@Override
	protected void populateJson(final JsonObject json) {
		if (this.base == null) {
			throw new IllegalStateException("No base set");
		}
		if (this.template == null) {
			throw new IllegalStateException("No template set");
		}
		if (this.addition == null) {
			throw new IllegalStateException("No addition set");
		}
		json.add("base", this.base);
		json.add("template", this.template);
		json.add("addition", this.addition);
	}

}
