package conductance.api.resource;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;

public interface ItemModelBuilder<BUILDER extends ItemModelBuilder<BUILDER>> extends ModelBuilder<BUILDER> {

	BUILDER guiLight(BlockModel.GuiLight guiLight);

	default BUILDER textureLayer(final int layer, final ResourceLocation texture) {
		return this.texture("layer" + layer, texture);
	}

	default BUILDER textureLayer(final int layer, final String textureReference) {
		return this.texture("layer" + layer, textureReference);
	}

	default BUILDER layer0(final ResourceLocation texture) {
		return this.textureLayer(0, texture);
	}

	default BUILDER layer0(final String textureReference) {
		return this.textureLayer(0, textureReference);
	}

	default BUILDER layer1(final ResourceLocation texture) {
		return this.textureLayer(1, texture);
	}

	default BUILDER layer1(final String textureReference) {
		return this.textureLayer(1, textureReference);
	}

	default BUILDER layer2(final ResourceLocation texture) {
		return this.textureLayer(2, texture);
	}

	default BUILDER layer2(final String textureReference) {
		return this.textureLayer(2, textureReference);
	}

	default BUILDER layer3(final ResourceLocation texture) {
		return this.textureLayer(3, texture);
	}

	default BUILDER layer3(final String textureReference) {
		return this.textureLayer(3, textureReference);
	}
}
