package conductance.lib.pack.server;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import com.mojang.serialization.JsonOps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.event.RecipeBuilder;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
abstract class AbstractRecipeBuilder<BUILDER extends RecipeBuilder<BUILDER>> implements RecipeBuilder<BUILDER> {

	private final Map<String, JsonElement> customJsonData = new HashMap<>();
	private final ResourceLocation recipeType;
	private final @Nullable ItemStack result;

	protected abstract void populateJson(JsonObject json);

	protected JsonElement encode(final Ingredient ingredient) {
		return Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, ingredient).getOrThrow();
	}

	protected JsonElement encode(final ItemStack stack) {
		return this.encode(stack.getItem());
	}

	protected JsonElement encode(final ItemLike item) {
		return new JsonPrimitive(BuiltInRegistries.ITEM.getKey(item.asItem()).toString());
	}

	protected JsonElement encode(final TagKey<Item> tag) {
		return this.encode(tag.location());
	}

	protected JsonElement encode(final ResourceLocation tag) {
		return new JsonPrimitive('#' + tag.toString());
	}

	@Override
	public final BUILDER addProperty(final String propertyKey, final String propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public final BUILDER addProperty(final String propertyKey, final boolean propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public final BUILDER addProperty(final String propertyKey, final Number propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public final BUILDER addProperty(final String propertyKey, final char propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public BUILDER addProperty(final String propertyKey, final JsonElement propertyValue) {
		this.customJsonData.put(propertyKey, propertyValue);
		return (BUILDER) this;
	}

	protected final JsonObject build() {
		return Util.make(new JsonObject(), json -> {
			json.addProperty("type", this.recipeType.toString());
			this.populateJson(json);
			if (this.result != null) {
				json.add("result", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.result).getOrThrow());
			}
			this.customJsonData.forEach(json::add);
		});
	}
}
