package conductance.lib.pack.client;

import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.resource.ItemsModelTintsBuilder;

@RequiredArgsConstructor
final class ItemsModelTintsBuilderImpl implements ItemsModelTintsBuilder {

	private final ArrayList<JsonObject> tints = new ArrayList<>();

	private ItemsModelTintsBuilder add(final ResourceLocation type, final Consumer<JsonObject> provider) {
		this.tints.add(Util.make(new JsonObject(), json -> {
			json.addProperty("type", type.toString());
			provider.accept(json);
		}));
		return this;
	}

	private ItemsModelTintsBuilder add(final ResourceLocation type, final String propName, final int rgb) {
		return this.add(type, json -> json.addProperty(propName, rgb));
	}

	private ItemsModelTintsBuilder add(final ResourceLocation type, final String propName, final float r, final float g, final float b) {
		return this.add(type, json -> json.add(propName, Util.make(new JsonArray(), arr -> {
			arr.add(Mth.clamp(r, 0, 1));
			arr.add(Mth.clamp(g, 0, 1));
			arr.add(Mth.clamp(b, 0, 1));
		})));
	}

	@Override
	public ItemsModelTintsBuilder constant(final int color) {
		return this.add(ResourceLocation.withDefaultNamespace("constant"), "value", color);
	}

	@Override
	public ItemsModelTintsBuilder constant(final float r, final float g, final float b) {
		return this.add(ResourceLocation.withDefaultNamespace("constant"), "value", r, g, b);
	}

	@Override
	public ItemsModelTintsBuilder dye(final int color) {
		return this.add(ResourceLocation.withDefaultNamespace("dyed_color"), "default", color);
	}

	@Override
	public ItemsModelTintsBuilder dye(final float r, final float g, final float b) {
		return this.add(ResourceLocation.withDefaultNamespace("dyed_color"), "default", r, g, b);
	}

	@Override
	public ItemsModelTintsBuilder firework(final int color) {
		return this.add(ResourceLocation.withDefaultNamespace("firework_explosion"), "default", color);
	}

	@Override
	public ItemsModelTintsBuilder firework(final float r, final float g, final float b) {
		return this.add(ResourceLocation.withDefaultNamespace("firework_explosion"), "default", r, g, b);
	}

	@Override
	public ItemsModelTintsBuilder grass(final float temperature, final float downfall) {
		return this.add(ResourceLocation.withDefaultNamespace("grass"), json -> {
			json.addProperty("temperature", Mth.clamp(temperature, 0, 1));
			json.addProperty("downfall", Mth.clamp(downfall, 0, 1));
		});
	}

	@Override
	public ItemsModelTintsBuilder mapColor(final int color) {
		return this.add(ResourceLocation.withDefaultNamespace("map_color"), "default", color);
	}

	@Override
	public ItemsModelTintsBuilder mapColor(final float r, final float g, final float b) {
		return this.add(ResourceLocation.withDefaultNamespace("map_color"), "default", r, g, b);
	}

	@Override
	public ItemsModelTintsBuilder potion(final int color) {
		return this.add(ResourceLocation.withDefaultNamespace("potion_contents"), "default", color);
	}

	@Override
	public ItemsModelTintsBuilder potion(final float r, final float g, final float b) {
		return this.add(ResourceLocation.withDefaultNamespace("potion_contents"), "default", r, g, b);
	}

	@Override
	public ItemsModelTintsBuilder team(final int color) {
		return this.add(ResourceLocation.withDefaultNamespace("team"), "default", color);
	}

	@Override
	public ItemsModelTintsBuilder team(final float r, final float g, final float b) {
		return this.add(ResourceLocation.withDefaultNamespace("team"), "default", r, g, b);
	}

	@Override
	public ItemsModelTintsBuilder customModelData(final int index, final int color) {
		return this.add(ResourceLocation.withDefaultNamespace("custom_model_data"), json -> {
			if (index != 0) {
				json.addProperty("index", index);
			}
			json.addProperty("default", color);
		});
	}

	@Override
	public ItemsModelTintsBuilder customModelData(final int index, final float r, final float g, final float b) {
		return this.add(ResourceLocation.withDefaultNamespace("custom_model_data"), json -> {
			if (index != 0) {
				json.addProperty("index", index);
			}
			json.add("default", Util.make(new JsonArray(), arr -> {
				arr.add(Mth.clamp(r, 0, 1));
				arr.add(Mth.clamp(g, 0, 1));
				arr.add(Mth.clamp(b, 0, 1));
			}));
		});
	}

	public JsonArray build() {
		return Util.make(new JsonArray(), arr -> this.tints.forEach(arr::add));
	}
}
