package conductance.client.resourcepack;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.TaggedSet;
import conductance.api.util.SafeOptional;
import conductance.Conductance;

public final class MaterialTextureSetModelHandler {

	static void reload() {
		CAPI.regs().materialTextureSets().forEach(set -> {
			CAPI.regs().materialTaggedSets().values().stream().filter(TaggedSet::isItemGenerator).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				RuntimeResourcePack.addItemModel(ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/" + set + "/" + type.getRegistryKey().getPath()),
						MaterialTextureSetModelHandler.createItemEntry(set, type));
			});
			CAPI.regs().materialTaggedSets().values().stream().filter(TaggedSet::isBlockGenerator).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				RuntimeResourcePack.addBlockModel(ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/" + set + "/" + type.getRegistryKey().getPath()),
						MaterialTextureSetModelHandler.createBlockEntry(set, type));
			});
		});
	}

	private static JsonObject createItemEntry(final MaterialTextureSet set, final MaterialTextureType type) {
		final Queue<String> layerQueue = new ArrayDeque<>(Arrays.asList("layer1", "layer2", "layer3", "layer4"));
		return Util.make(new JsonObject(), json -> {
			json.addProperty("parent", "item/generated");
			json.add("textures", Util.make(new JsonObject(), textures -> {
				textures.addProperty("layer0", type.getItemTexture(set, null, null).getValue().toString());

				final ResourceLocation magneticOverlayTexture = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "item/material/" + set.getRegistryKey() + "/magnetic_overlay");
				if (CAPI.resourceFinder().isTextureValid(magneticOverlayTexture)) {
					assert layerQueue.size() > 1;
					textures.addProperty(layerQueue.poll(), magneticOverlayTexture.toString());
				}

				int i = 1;
				while (!layerQueue.isEmpty()) {
					final int overlay = i++;
					final SafeOptional<ResourceLocation> extraOverlay = type.getItemTexture(set, null, "_overlay%s".formatted(overlay == 1 ? "" : overlay));
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

	private static JsonObject createBlockEntry(final MaterialTextureSet set, final MaterialTextureType type) {
		return Util.make(new JsonObject(), json -> {
			json.addProperty("parent", Conductance.id("block/cube_all_tinted0").toString());
			json.add("textures", Util.make(new JsonObject(), textures -> {
				textures.addProperty("all", type.getBlockTexture(set, null, null).getValue().toString());
			}));
		});
	}

	private MaterialTextureSetModelHandler() {
	}
}
