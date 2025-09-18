package conductance.lib.pack.server;

import java.util.Objects;
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
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import org.apache.http.util.TextUtils;
import conductance.api.CAPI;
import conductance.api.recipe.event.ShapedCraftingRecipeBuilder;

final class ShapedCraftingRecipeBuilderImpl extends AbstractRecipeBuilder<ShapedCraftingRecipeBuilder> implements ShapedCraftingRecipeBuilder {

	private final String[] pattern = new String[3];
	private final Char2ObjectArrayMap<JsonElement> keys = new Char2ObjectArrayMap<>();

	ShapedCraftingRecipeBuilderImpl(final ItemStack result) {
		super(ResourceLocation.withDefaultNamespace("crafting_shaped"), result);
	}

	@SuppressWarnings("DataFlowIssue")
	@Override
	public ShapedCraftingRecipeBuilder pattern(final String row1, final String row2, final String row3) {
		this.pattern[0] = row1;
		this.pattern[1] = row2;
		this.pattern[2] = row3;
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
		this.keys.put(c, this.encode(ingredient));
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final ItemStack stack) {
		this.keys.put(c, this.encode(stack));
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final ItemLike item) {
		this.keys.put(c, this.encode(item));
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final TagKey<Item> tag) {
		this.keys.put(c, this.encode(tag));
		return this;
	}

	@Override
	public ShapedCraftingRecipeBuilder key(final char c, final ResourceLocation tag) {
		this.keys.put(c, this.encode(tag));
		return this;
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.add("pattern", Util.make(new JsonArray(), arr -> {
			if (TextUtils.isBlank(this.pattern[0])) {
				throw new IllegalArgumentException("No pattern set");
			}
			arr.add(this.pattern[0]);
			if (!TextUtils.isBlank(this.pattern[1])) {
				arr.add(this.pattern[1]);
			}
			if (!TextUtils.isBlank(this.pattern[2])) {
				arr.add(this.pattern[2]);
			}
		}));
		json.add("key", Util.make(new JsonObject(), keys -> {
			this.keys.forEach((c, val) -> keys.add(c.toString(), val));
		}));
	}
}
