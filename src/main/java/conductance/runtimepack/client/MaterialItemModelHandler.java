package conductance.runtimepack.client;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;

@RequiredArgsConstructor
public final class MaterialItemModelHandler {

	private static final Set<MaterialItemModelHandler> MODELS = new HashSet<>();

	private final Item item;
	private final Material material;
	private final MaterialTextureSet set;
	private final MaterialTextureType type;

	public static void add(final Item item, final Material material, final MaterialTextureSet set, final MaterialTextureType type) {
		MaterialItemModelHandler.MODELS.add(new MaterialItemModelHandler(item, material, set, type));
	}

	static void reload() {
		MaterialItemModelHandler.MODELS.forEach(model -> {
			final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(model.item);
			final ResourceLocation custom = ResourceHelper.getCustomItemTexture(model.material, model.type);
			if (custom == null) {
				RuntimeResourcePack.addItemModel(itemId, new DelegatedModel(model.type.getItemModel(model.set, null, null).getValue()));
			} else {
				RuntimeResourcePack.addItemModel(itemId, () -> Util.make(new JsonObject(), json -> {
					json.addProperty("parent", "item/generated");
					json.add("textures", Util.make(new JsonObject(), json2 -> json2.addProperty("layer0", custom.toString())));
				}));
			}
		});
	}
}
