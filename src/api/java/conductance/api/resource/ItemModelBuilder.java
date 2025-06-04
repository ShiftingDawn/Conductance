package conductance.api.resource;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;

public interface ItemModelBuilder<BUILDER extends ItemModelBuilder<BUILDER>> extends ModelBuilder<BUILDER> {

	BUILDER guiLight(BlockModel.GuiLight guiLight);

	default BUILDER textureLayer(final int layer, final ResourceLocation texture) {
		return this.texture("layer" + layer, texture);
	}

	default BUILDER textureLayer(final int layer, final String textureOrReferenceKey) {
		return this.texture("layer" + layer, textureOrReferenceKey);
	}

	default BUILDER layer0(final ResourceLocation texture) {
		return this.textureLayer(0, texture);
	}

	default BUILDER layer0(final String textureOrReferenceKey) {
		return this.textureLayer(0, textureOrReferenceKey);
	}

	default BUILDER layer1(final ResourceLocation texture) {
		return this.textureLayer(1, texture);
	}

	default BUILDER layer1(final String textureOrReferenceKey) {
		return this.textureLayer(1, textureOrReferenceKey);
	}

	default BUILDER layer2(final ResourceLocation texture) {
		return this.textureLayer(2, texture);
	}

	default BUILDER layer2(final String textureOrReferenceKey) {
		return this.textureLayer(2, textureOrReferenceKey);
	}

	default BUILDER layer3(final ResourceLocation texture) {
		return this.textureLayer(3, texture);
	}

	default BUILDER layer3(final String textureOrReferenceKey) {
		return this.textureLayer(3, textureOrReferenceKey);
	}
}
