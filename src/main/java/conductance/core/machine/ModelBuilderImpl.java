package conductance.core.machine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.ModelDisplayBuilder;
import conductance.api.resource.ModelElementBuilder;

public abstract class ModelBuilderImpl<BUILDER extends ModelBuilderImpl<BUILDER>> implements ModelBuilder<BUILDER> {

	private final Map<String, String> textures = new HashMap<>();
	private final EnumMap<ItemDisplayContext, ModelDisplayBuilderImpl<BUILDER>> displays = new EnumMap<>(ItemDisplayContext.class);
	private final List<ModelElementBuilderImpl<BUILDER>> elements = new ArrayList<>();
	private ResourceLocation parent;
	@Nullable
	private ResourceLocation loader;

	public ModelBuilderImpl(final ResourceLocation defaultParent) {
		this.parent = defaultParent;
	}

	@SuppressWarnings("unchecked")
	private BUILDER self() {
		return (BUILDER) this;
	}

	@Override
	public BUILDER parent(final ResourceLocation newParent) {
		this.parent = newParent;
		return this.self();
	}

	@Override
	public BUILDER loader(final ResourceLocation newLoader) {
		this.loader = newLoader;
		return this.self();
	}

	@Override
	public ModelDisplayBuilder<BUILDER> display(final ItemDisplayContext context) {
		return this.displays.computeIfAbsent(context, k -> new ModelDisplayBuilderImpl<>(this.self()));
	}

	@Override
	public BUILDER texture(final String textureKey, final ResourceLocation texture) {
		this.textures.put(textureKey, texture.toString());
		return this.self();
	}

	@Override
	public BUILDER texture(final String textureKey, final String referenceTextureKey) {
		this.textures.put(textureKey, '#' + referenceTextureKey);
		return this.self();
	}

	@Override
	public ModelElementBuilder<BUILDER> element() {
		return Util.make(new ModelElementBuilderImpl<>(this.self()), this.elements::add);
	}

	protected abstract void addJsonProperties(JsonObject json);

	public final JsonObject build() {
		return Util.make(new JsonObject(), json -> {
			json.addProperty("parent", this.parent.toString());
			if (this.loader != null) {
				json.addProperty("loader", this.loader.toString());
			}
			if (!this.displays.isEmpty()) {
				json.add("display", Util.make(new JsonObject(), displayJson -> {
					this.displays.forEach((key, builder) -> displayJson.add(key.getSerializedName(), builder.serialize()));
				}));
			}
			if (!this.textures.isEmpty()) {
				json.add("textures", Util.make(new JsonObject(), texturesJson -> this.textures.forEach(texturesJson::addProperty)));
			}
			if (!this.elements.isEmpty()) {
				json.add("elements", Util.make(new JsonArray(), array -> this.elements.forEach(element -> array.add(element.serialize()))));
			}
			this.addJsonProperties(json);
		});
	}
}
