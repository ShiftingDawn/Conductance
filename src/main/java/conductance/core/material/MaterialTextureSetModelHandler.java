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
		MaterialTextureSetLoader.getTextureSets().forEach(set -> {
			Conductance.LOGGER.debug("Creating models for material texture set {}", set);
			CAPI.regs().materialGenerationHandlers().stream().filter(MaterialGenerationHandler::hasItem).map(MaterialGenerationHandler::getTextureType).distinct().forEach(textureType -> {
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(textureType.getNamespace(), "material/%s/%s/%s".formatted(set.getNamespace(), set.getPath(),
						textureType.getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				event.addItemModel(path, builder -> MaterialTextureSetModelHandler.createItemEntry(set, textureType, builder));
			});
			CAPI.regs().materialGenerationHandlers().stream().filter(MaterialGenerationHandler::hasBlock).forEach(taggedSet -> {
				final ResourceLocation textureType = taggedSet.getTextureType();
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(textureType.getNamespace(), "material/%s/%s/%s".formatted(set.getNamespace(), set.getPath(), textureType.getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				event.addBlockModel(path, builder -> MaterialTextureSetModelHandler.createBlockEntry(taggedSet, set, textureType, builder));
			});
		});
	}

	private static void createItemEntry(final ResourceLocation textureSet, final ResourceLocation textureType, final ModelBuilder builder) {
		builder.layer0(CAPI.resourceFinder().getMaterialTexture(textureSet, textureType, null, null).value());
		final ResourceLocation magneticOverlayTexture = ResourceLocation.fromNamespaceAndPath(textureType.getNamespace(), "material/%s/%s/magnetic_overlay".formatted(textureSet.getNamespace(), textureSet.getPath()));
		int currentLayer = 1;
		if (CAPI.resourceFinder().isTextureValid(magneticOverlayTexture)) {
			builder.textureLayer(currentLayer++, magneticOverlayTexture);
		}
		int i = 1;
		while (currentLayer < 5) {
			final int overlay = i++;
			final SafeOptional<ResourceLocation> extraOverlay = CAPI.resourceFinder().getMaterialTexture(textureSet, textureType, null, "_overlay%s".formatted(overlay == 1 ? "" : overlay));
			if (CAPI.resourceFinder().isTextureValid(extraOverlay.value())) {
				builder.textureLayer(currentLayer++, extraOverlay.value());
			} else {
				break;
			}
		}
	}

	private static void createBlockEntry(final MaterialGenerationHandler handler, final ResourceLocation textureSet, final ResourceLocation textureType, final ModelBuilder builder) {
		final ResourceLocation texture = CAPI.resourceFinder().getMaterialTexture(textureSet, textureType, null, null).value();
		builder.parent(Conductance.id("block/material_block_base")).particle(texture);
		if (!handler.shouldOccludeBlocks()) {
			builder.renderType("cutout_mipped");
		}
	}

	private MaterialTextureSetModelHandler() {
	}
}
