package conductance.client.resourcepack;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;

@RequiredArgsConstructor
public final class MaterialModelHandler {

	private static final Set<MaterialModelHandler> MODELS = new HashSet<>();

	private final Item item;
	private final Material material;
	private final MaterialTextureSet set;
	private final MaterialTextureType type;

	public static void add(Item item, Material material, MaterialTextureSet set, MaterialTextureType type) {
		MaterialModelHandler.MODELS.add(new MaterialModelHandler(item, material, set, type));
	}

	static void reload() {
		MaterialModelHandler.MODELS.forEach(model -> {
			ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(model.item);
			ResourceLocation custom = getCustomTexture(model);
			if (custom == null) {
				RuntimeResourcePack.addItemModel(itemId, new DelegatedModel(model.type.getItemModel(model.set).getValue()));
			} else {
				RuntimeResourcePack.addItemModel(itemId, () -> Util.make(new JsonObject(), json -> {
					json.addProperty("parent", "item/generated");
					json.add("textures", Util.make(new JsonObject(), json2 -> json2.addProperty("layer0", custom.toString())));
				}));
			}
		});
	}

	@Nullable
	private static ResourceLocation getCustomTexture(MaterialModelHandler handler) {
		final ResourceLocation checkTexture = handler.material.getRegistryKey().withPath("textures/item/material_custom/%s/%s.png".formatted(handler.material.getRegistryKey().getPath(), handler.type.getRegistryKey().getPath()));
		if (CAPI.RESOURCE_FINDER.isResourceValid(checkTexture)) {
			return handler.material.getRegistryKey().withPath("item/material_custom/%s/%s".formatted(handler.material.getRegistryKey().getPath(), handler.type.getRegistryKey().getPath()));
		}
		return null;
	}
}
