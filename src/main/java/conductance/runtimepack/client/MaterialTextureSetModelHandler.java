package conductance.runtimepack.client;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.registry.TaggedSet;
import conductance.api.resource.BlockModelBuilder;
import conductance.api.resource.ItemModelBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.util.SafeOptional;
import conductance.Conductance;
import conductance.core.material.MaterialTextureSetLoader;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialTextureSetModelHandler {

	@EventListener(priority = -101)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		MaterialTextureSetLoader.reload();
		MaterialTextureSetLoader.getTextureSets().forEach(set -> {
			Conductance.LOGGER.debug("Creating models for material texture set {}", set);
			CAPI.regs().materialTaggedSets().values().stream().filter(TaggedSet::hasItems).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/%s/%s/%s".formatted(set.getNamespace(), set.getPath(),
						type.getRegistryKey().getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				event.addItemModel(path, builder -> MaterialTextureSetModelHandler.createItemEntry(set, type, builder));
			});
			CAPI.regs().materialTaggedSets().values().stream().filter(TaggedSet::hasBlocks).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/%s/%s/%s".formatted(set.getNamespace(), set.getPath(),
						type.getRegistryKey().getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				event.addBlockModel(path, builder -> MaterialTextureSetModelHandler.createBlockEntry(set, type, builder));
			});
		});
	}

	private static void createItemEntry(final ResourceLocation set, final MaterialTextureType type, final ItemModelBuilder<?> builder) {
		builder.layer0(type.getTexture(set, null, null).getValue());
		final ResourceLocation magneticOverlayTexture = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/%s/%s/magnetic_overlay".formatted(set.getNamespace(), set.getPath()));
		int currentLayer = 1;
		if (CAPI.resourceFinder().isTextureValid(magneticOverlayTexture)) {
			builder.textureLayer(currentLayer++, magneticOverlayTexture);
		}
		int i = 1;
		while (currentLayer < 5) {
			final int overlay = i++;
			final SafeOptional<ResourceLocation> extraOverlay = type.getTexture(set, null, "_overlay%s".formatted(overlay == 1 ? "" : overlay));
			if (CAPI.resourceFinder().isTextureValid(extraOverlay.getValue())) {
				builder.textureLayer(currentLayer++, extraOverlay.getValue());
			} else {
				break;
			}
		}
	}

	private static void createBlockEntry(final ResourceLocation set, final MaterialTextureType type, final BlockModelBuilder<?> builder) {
		builder
				.parent(Conductance.id("block/cube_all_tinted0"))
				.texture("all", type.getTexture(set, null, null).getValue());
	}

	private MaterialTextureSetModelHandler() {
	}
}
