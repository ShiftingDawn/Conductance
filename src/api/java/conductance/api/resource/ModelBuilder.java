package conductance.api.resource;

import java.util.function.Consumer;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public interface ModelBuilder extends JsonResourceBuilder<ModelBuilder> {

	ModelBuilder composite(Consumer<CompositeModelBuilder> builder);

	ModelBuilder parent(ResourceLocation parent);

	default ModelBuilder parent(final String parent) {
		return this.parent(ResourceLocation.withDefaultNamespace(parent));
	}

	ModelBuilder loader(ResourceLocation loader);

	ModelBuilder renderType(ResourceLocation renderType);

	default ModelBuilder renderType(final String renderType) {
		return this.renderType(ResourceLocation.withDefaultNamespace(renderType));
	}

	ModelBuilder display(ItemDisplayContext context, Consumer<ModelDisplayBuilder> builder);

	ModelBuilder texture(String referenceKey, String textureOrReferenceKey);

	default ModelBuilder texture(final String referenceKey, final ResourceLocation texture) {
		return this.texture(referenceKey, texture.toString());
	}

	default ModelBuilder particle(final ResourceLocation texture) {
		return this.texture("particle", texture);
	}

	default ModelBuilder particle(final String textureOrReferenceKey) {
		return this.texture("particle", textureOrReferenceKey);
	}

	ModelBuilder element(Consumer<ModelElementBuilder> builder, boolean ignoreWhenEmpty);

	default ModelBuilder element(final Consumer<ModelElementBuilder> builder) {
		return this.element(builder, false);
	}

	ModelBuilder ambientOcclusion(boolean ambientOcclusion);

	ModelBuilder guiLight(BlockModel.GuiLight guiLight);

	default ModelBuilder textureLayer(final int layer, final ResourceLocation texture) {
		return this.texture("layer" + layer, texture);
	}

	default ModelBuilder textureLayer(final int layer, final String textureOrReferenceKey) {
		return this.texture("layer" + layer, textureOrReferenceKey);
	}

	default ModelBuilder layer0(final ResourceLocation texture) {
		return this.textureLayer(0, texture);
	}

	default ModelBuilder layer0(final String textureOrReferenceKey) {
		return this.textureLayer(0, textureOrReferenceKey);
	}

	default ModelBuilder layer1(final ResourceLocation texture) {
		return this.textureLayer(1, texture);
	}

	default ModelBuilder layer1(final String textureOrReferenceKey) {
		return this.textureLayer(1, textureOrReferenceKey);
	}

	default ModelBuilder layer2(final ResourceLocation texture) {
		return this.textureLayer(2, texture);
	}

	default ModelBuilder layer2(final String textureOrReferenceKey) {
		return this.textureLayer(2, textureOrReferenceKey);
	}

	default ModelBuilder layer3(final ResourceLocation texture) {
		return this.textureLayer(3, texture);
	}

	default ModelBuilder layer3(final String textureOrReferenceKey) {
		return this.textureLayer(3, textureOrReferenceKey);
	}
}
