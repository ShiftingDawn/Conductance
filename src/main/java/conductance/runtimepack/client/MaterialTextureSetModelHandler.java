package conductance.runtimepack.client;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.TaggedSet;
import conductance.api.util.SafeOptional;
import conductance.Conductance;
import conductance.core.material.MaterialTextureSetLoader;

public final class MaterialTextureSetModelHandler {

	static void reload() {
		MaterialTextureSetLoader.reload();
		MaterialTextureSetLoader.getTextureSets().forEach(set -> {
			Conductance.LOGGER.debug("Creating models for material texture set {}", set);
			CAPI.regs().materialTaggedSets().values().stream().filter(TaggedSet::hasItems).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/%s/%s/%s".formatted(set.getNamespace(), set.getPath(),
						type.getRegistryKey().getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				RuntimeResourcePack.addItemModel(path, MaterialTextureSetModelHandler.createItemEntry(set, type));
			});
			CAPI.regs().materialTaggedSets().values().stream().filter(TaggedSet::hasBlocks).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				final ResourceLocation path = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/%s/%s/%s".formatted(set.getNamespace(), set.getPath(),
						type.getRegistryKey().getPath()));
				Conductance.LOGGER.trace("\t{}", path);
				RuntimeResourcePack.addBlockModel(path, MaterialTextureSetModelHandler.createBlockEntry(set, type));
			});
		});
	}

	private static JsonObject createItemEntry(final ResourceLocation set, final MaterialTextureType type) {
		final Queue<String> layerQueue = new ArrayDeque<>(Arrays.asList("layer1", "layer2", "layer3", "layer4"));
		return Util.make(new JsonObject(), json -> {
			json.addProperty("parent", "item/generated");
			json.add("textures", Util.make(new JsonObject(), textures -> {
				textures.addProperty("layer0", type.getTexture(set, null, null).getValue().toString());

				final ResourceLocation magneticOverlayTexture = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(),
						"item/material/%s/%s/magnetic_overlay".formatted(set.getNamespace(), set.getPath()));
				if (CAPI.resourceFinder().isTextureValid(magneticOverlayTexture)) {
					assert layerQueue.size() > 1;
					textures.addProperty(layerQueue.poll(), magneticOverlayTexture.toString());
				}

				int i = 1;
				while (!layerQueue.isEmpty()) {
					final int overlay = i++;
					final SafeOptional<ResourceLocation> extraOverlay = type.getTexture(set, null, "_overlay%s".formatted(overlay == 1 ? "" : overlay));
					if (CAPI.resourceFinder().isTextureValid(extraOverlay.getValue())) {
						assert layerQueue.size() > 1;
						textures.addProperty(layerQueue.poll(), extraOverlay.getValue().toString());
					} else {
						break;
					}
				}
			}));
		});
	}

	private static JsonObject createBlockEntry(final ResourceLocation set, final MaterialTextureType type) {
		return Util.make(new JsonObject(), json -> {
			json.addProperty("parent", Conductance.id("block/cube_all_tinted0").toString());
			json.add("textures", Util.make(new JsonObject(), textures -> {
				textures.addProperty("all", type.getTexture(set, null, null).getValue().toString());
			}));
		});
	}

	private MaterialTextureSetModelHandler() {
	}
}
