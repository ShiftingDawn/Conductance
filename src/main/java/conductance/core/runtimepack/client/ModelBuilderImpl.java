package conductance.core.runtimepack.client;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.CompositeModelBuilder;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.ModelDisplayBuilder;
import conductance.api.resource.ModelElementBuilder;

final class ModelBuilderImpl implements ModelBuilder {

	private final Map<String, String> textures = new HashMap<>();
	private final EnumMap<ItemDisplayContext, ModelDisplayBuilderImpl> displays = new EnumMap<>(ItemDisplayContext.class);
	private final List<ModelElementBuilderImpl> elements = new ArrayList<>();
	private final Map<String, JsonElement> customProps = new HashMap<>();
	private ResourceLocation parent;
	@Nullable
	private ResourceLocation loader;
	@Nullable
	private CompositeModelBuilderImpl compositeBuilder;
	@Nullable
	private ResourceLocation renderType;
	@Nullable
	private Boolean ambientOcclusion;
	@Nullable
	private BlockModel.GuiLight guiLight;

	ModelBuilderImpl(final ResourceLocation defaultParent) {
		this.parent = defaultParent;
	}

	@Override
	public ModelBuilder composite(final Consumer<CompositeModelBuilder> builder) {
		if (this.compositeBuilder == null) {
			this.compositeBuilder = new CompositeModelBuilderImpl();
			this.loader(ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "composite"));
		}
		builder.accept(this.compositeBuilder);
		return this;
	}

	@Override
	public ModelBuilder parent(final ResourceLocation newParent) {
		this.parent = newParent;
		return this;
	}

	@Override
	public ModelBuilder loader(final ResourceLocation newLoader) {
		this.loader = newLoader;
		return this;
	}

	@Override
	public ModelBuilder renderType(final ResourceLocation type) {
		this.renderType = type;
		return this;
	}

	@Override
	public ModelBuilder display(final ItemDisplayContext context, final Consumer<ModelDisplayBuilder> builder) {
		Util.make(this.displays.computeIfAbsent(context, k -> new ModelDisplayBuilderImpl()), builder);
		return this;
	}

	@Override
	public ModelBuilder texture(final String textureKey, final String textureOrReferenceKey) {
		this.textures.put(textureKey, textureOrReferenceKey);
		return this;
	}

	@Override
	public ModelBuilder element(final Consumer<ModelElementBuilder> builder, final boolean ignoreWhenEmpty) {
		Util.make(new ModelElementBuilderImpl(), b -> {
			builder.accept(b);
			if (!(b.isEmpty() && ignoreWhenEmpty)) {
				this.elements.add(b);
			}
		});
		return this;
	}

	@Override
	public ModelBuilder ambientOcclusion(final boolean newAmbientOcclusion) {
		this.ambientOcclusion = newAmbientOcclusion;
		return this;
	}

	@Override
	public ModelBuilder guiLight(final BlockModel.GuiLight newGuiLight) {
		this.guiLight = newGuiLight;
		return this;
	}

	@Override
	public ModelBuilder addProperty(final String propertyKey, final String propertyValue) {
		this.customProps.put(propertyKey, new JsonPrimitive(propertyValue));
		return this;
	}

	@Override
	public ModelBuilder addProperty(final String propertyKey, final boolean propertyValue) {
		this.customProps.put(propertyKey, new JsonPrimitive(propertyValue));
		return this;
	}

	@Override
	public ModelBuilder addProperty(final String propertyKey, final Number propertyValue) {
		this.customProps.put(propertyKey, new JsonPrimitive(propertyValue));
		return this;
	}

	@Override
	public ModelBuilder addProperty(final String propertyKey, final char propertyValue) {
		this.customProps.put(propertyKey, new JsonPrimitive(propertyValue));
		return this;
	}

	public JsonObject build() {
		return Util.make(new JsonObject(), json -> {
			json.addProperty("parent", this.parent.toString());
			if (this.loader != null) {
				json.addProperty("loader", this.loader.toString());
			}
			if (this.compositeBuilder != null) {
				this.compositeBuilder.addToJson(json);
			}
			if (this.renderType != null) {
				json.addProperty("render_type", this.renderType.toString());
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
			if (this.ambientOcclusion != null) {
				json.addProperty("ambientocclusion", this.ambientOcclusion);
			}
			if (this.guiLight != null) {
				json.addProperty("gui_light", this.guiLight.getSerializedName());
			}
			this.customProps.forEach(json::add);
		});
	}
}
