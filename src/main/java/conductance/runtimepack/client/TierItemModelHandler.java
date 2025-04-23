package conductance.runtimepack.client;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.util.TieredItemType;
import conductance.Conductance;

@RequiredArgsConstructor
public final class TierItemModelHandler {

	private static final Set<TierItemModelHandler> MODELS = new HashSet<>();

	private final Item item;
	private final TieredItemType type;

	public static void add(final Item item, final TieredItemType type) {
		TierItemModelHandler.MODELS.add(new TierItemModelHandler(item, type));
	}

	static void reload() {
		TierItemModelHandler.MODELS.forEach(model -> {
			final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(model.item);
			final ResourceLocation custom = ResourceHelper.getCustomItemTexture(itemId);
			RuntimeResourcePack.addItemModel(itemId, () -> Util.make(new JsonObject(), json -> {
				json.addProperty("parent", "item/generated");
				json.add("textures", Util.make(new JsonObject(), json2 -> {
					if (custom == null) {
						json2.addProperty("layer0", Conductance.id("item/tier/%s/base".formatted(model.type)).toString());
						json2.addProperty("layer1", Conductance.id("item/tier/%s/overlay".formatted(model.type)).toString());
					} else {
						json2.addProperty("layer0", custom.toString());
					}
				}));
			}));
		});
	}
}
