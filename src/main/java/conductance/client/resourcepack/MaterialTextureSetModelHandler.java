package conductance.client.resourcepack;

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

public class MaterialTextureSetModelHandler {

	static void reload() {
		CAPI.REGS.materialTextureSets().forEach(set -> {
			CAPI.REGS.materialTaggedSets().values().stream().filter(TaggedSet::isItemGenerator).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				RuntimeResourcePack.addItemModel(ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/" + set + "/" + type.getRegistryKey().getPath()), createItemEntry(set, type));
			});
			CAPI.REGS.materialTaggedSets().values().stream().filter(TaggedSet::isBlockGenerator).map(TaggedMaterialSet::getTextureType).distinct().forEach(type -> {
				RuntimeResourcePack.addBlockModel(ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "material/" + set + "/" + type.getRegistryKey().getPath()), createBlockEntry(set, type));
			});
		});
	}

	private static JsonObject createItemEntry(final MaterialTextureSet set, final MaterialTextureType type) {
		return Util.make(new JsonObject(), json -> {
			json.addProperty("parent", "item/generated");
			json.add("textures", Util.make(new JsonObject(), textures -> {
				textures.addProperty("layer0", type.getItemTexture(set, null, null).getValue().toString());

				final ResourceLocation magneticOverlayTexture = ResourceLocation.fromNamespaceAndPath(type.getRegistryKey().getNamespace(), "item/material/" + set.getRegistryKey() + "/magnetic_overlay");
				if (CAPI.RESOURCE_FINDER.isTextureValid(magneticOverlayTexture)) {
					textures.addProperty("layer" + textures.size(), magneticOverlayTexture.toString());
				}

				for (int i = 0; i <= (textures.size() == 1 ? 4 : 3); ++i) {
					final SafeOptional<ResourceLocation> extraOverlay = type.getItemTexture(set, null, "overlay" + (i == 0 ? "" : i + 1));
					if (CAPI.RESOURCE_FINDER.isTextureValid(extraOverlay.getValue())) {
						textures.addProperty("layer" + textures.size(), extraOverlay.getValue().toString());
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
}
