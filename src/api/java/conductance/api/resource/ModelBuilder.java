package conductance.api.resource;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public interface ModelBuilder<BUILDER extends ModelBuilder<BUILDER>> {

	BUILDER parent(ResourceLocation parent);

	default BUILDER parent(final String parent) {
		return this.parent(ResourceLocation.withDefaultNamespace(parent));
	}

	BUILDER loader(ResourceLocation loader);

	ModelDisplayBuilder<BUILDER> display(ItemDisplayContext context);

	BUILDER texture(String textureKey, ResourceLocation texture);

	BUILDER texture(String textureKey, String referenceTextureKey);

	default BUILDER particle(final ResourceLocation texture) {
		return this.texture("particle", texture);
	}

	default BUILDER particle(final String referenceTextureKey) {
		return this.texture("particle", referenceTextureKey);
	}

	ModelElementBuilder<BUILDER> element();
}
