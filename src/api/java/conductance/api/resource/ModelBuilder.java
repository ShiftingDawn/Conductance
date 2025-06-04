package conductance.api.resource;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public interface ModelBuilder<BUILDER extends ModelBuilder<BUILDER>> {

	BUILDER parent(ResourceLocation parent);

	default BUILDER parent(final String parent) {
		return this.parent(ResourceLocation.withDefaultNamespace(parent));
	}

	BUILDER loader(ResourceLocation loader);

	BUILDER renderType(ResourceLocation renderType);

	default BUILDER renderType(final String renderType) {
		return this.renderType(ResourceLocation.withDefaultNamespace(renderType));
	}

	ModelDisplayBuilder<BUILDER> display(ItemDisplayContext context);

	BUILDER texture(String textureKey, ResourceLocation texture);

	BUILDER texture(String textureKey, String referenceTextureKey);

	default BUILDER particle(final ResourceLocation texture) {
		return this.texture("particle", texture);
	}

	default BUILDER particle(final String referenceTextureKey) {
		return this.texture("particle", referenceTextureKey);
	}

	BUILDER addProperty(String propertyKey, String propertyValue);

	BUILDER addProperty(String propertyKey, boolean propertyValue);

	BUILDER addProperty(String propertyKey, Number propertyValue);

	BUILDER addProperty(String propertyKey, char propertyValue);

	default BUILDER addProperty(final String propertyKey, final ResourceLocation propertyValue) {
		return this.addProperty(propertyKey, propertyValue.toString());
	}

	ModelElementBuilder<BUILDER> element();
}
