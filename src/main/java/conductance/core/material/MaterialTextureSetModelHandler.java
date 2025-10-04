package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.util.SafeOptional;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialTextureSetModelHandler {

	@EventListener(priority = -101)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		MaterialTextureSetLoader.reload();
		MaterialTextureSetLoader.getTextureSets().forEach((setId, setData) -> {
			Conductance.LOGGER.debug("Creating models for material texture set {}", setId);
			CAPI.regs().materialGenerationHandlers().stream().filter(MaterialGenerationHandler::hasItem).map(MaterialGenerationHandler::getTextureType).distinct().forEach(textureType -> {
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(textureType.getNamespace(), "material/%s/%s/%s".formatted(setId.getNamespace(), setId.getPath(), textureType.getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				event.addItemModel(path, builder -> MaterialTextureSetModelHandler.createItemEntry(setId, setData, textureType, builder));
			});
			CAPI.regs().materialGenerationHandlers().stream().filter(MaterialGenerationHandler::hasBlock).forEach(taggedSet -> {
				final ResourceLocation textureType = taggedSet.getTextureType();
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(textureType.getNamespace(), "material/%s/%s/%s".formatted(setId.getNamespace(), setId.getPath(), textureType.getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				event.addBlockModel(path, builder -> MaterialTextureSetModelHandler.createBlockEntry(taggedSet, setData, setId, textureType, builder));
			});
		});
	}

	private static void createItemEntry(final ResourceLocation textureSet, final MaterialTextureSet setData, final ResourceLocation textureType, final ModelBuilder builder) {
		builder.layer0(CAPI.resourceFinder().getMaterialTexture(textureSet, textureType, null, null).value());
		int currentLayer = 1;
		while (currentLayer < 5) {
			final SafeOptional<ResourceLocation> extraOverlay = CAPI.resourceFinder().getMaterialTexture(textureSet, textureType, null, "_overlay%s".formatted(currentLayer == 1 ? "" : currentLayer));
			if (CAPI.resourceFinder().isTextureValid(extraOverlay.value())) {
				builder.textureLayer(currentLayer, extraOverlay.value());
				++currentLayer;
			} else {
				break;
			}
		}
		if (setData.overlay().isPresent()) {
			builder.textureLayer(currentLayer, setData.overlay().get());
		}
	}

	private static void createBlockEntry(final MaterialGenerationHandler handler, final MaterialTextureSet setData, final ResourceLocation textureSet, final ResourceLocation textureType, final ModelBuilder builder) {
		final ResourceLocation texture = CAPI.resourceFinder().getMaterialTexture(textureSet, textureType, null, null).value();
		builder.parent(Conductance.id("block/material_block_base")).particle(texture);
		if (!handler.shouldOccludeBlocks()) {
			builder.renderType("cutout_mipped");
		}
	}

	private MaterialTextureSetModelHandler() {
	}
}
