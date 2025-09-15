package conductance.lib.pack.client;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import conductance.Conductance;
import conductance.ModConfig;
import conductance.lib.pack.AbstractRuntimePack;

final class RuntimeResourcePack extends AbstractRuntimePack {

	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();
	private static final Map<String, String> TRANSLATIONS = new ConcurrentHashMap<>();

	RuntimeResourcePack(final PackLocationInfo info) {
		super(info, PackType.CLIENT_RESOURCES);
	}

	@Override
	protected Map<ResourceLocation, byte[]> getAllData() {
		return RuntimeResourcePack.DATA;
	}

	static void resetStatesAndModels() {
		RuntimeResourcePack.DATA.keySet().forEach(key -> {
			if (key.getPath().startsWith("blockstates") || key.getPath().startsWith("items") || key.getPath().startsWith("models")) {
				RuntimeResourcePack.DATA.remove(key);
			}
		});
	}

	static void resetTranslations() {
		RuntimeResourcePack.TRANSLATIONS.clear();
		RuntimeResourcePack.DATA.remove(Conductance.id("lang/en_us.json"));
	}

	static void freezeTranslations() {
		RuntimeResourcePack.DATA.put(Conductance.id("lang/en_us.json"), Util.make(new JsonObject(), json -> {
			RuntimeResourcePack.TRANSLATIONS.forEach(json::addProperty);
			RuntimeResourcePack.writeJson(Conductance.id("lang/en_us.json"), json);
		}).toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addBlockState(final ResourceLocation location, final JsonElement blockState) {
		final ResourceLocation realLocation = RuntimeResourcePack.getBlockStateLocation(location);
		RuntimeResourcePack.writeJson(realLocation, blockState);
		RuntimeResourcePack.DATA.put(realLocation, blockState.toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addBlockModel(final ResourceLocation location, final JsonElement blockModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getBlockModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, blockModel);
		RuntimeResourcePack.DATA.put(realLocation, blockModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addItemsModel(final ResourceLocation location, final JsonElement itemsModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getItemsModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, itemsModel);
		RuntimeResourcePack.DATA.put(realLocation, itemsModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addItemModel(final ResourceLocation location, final JsonElement itemModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getItemModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, itemModel);
		RuntimeResourcePack.DATA.put(realLocation, itemModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addTranslation(final String key, final String translation) {
		RuntimeResourcePack.TRANSLATIONS.put(key, translation);
	}

	private static ResourceLocation getBlockStateLocation(final ResourceLocation blockId) {
		return ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), String.join("", "blockstates/", blockId.getPath(), ".json"));
	}

	private static ResourceLocation getBlockModelLocation(final ResourceLocation blockId) {
		return ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), String.join("", "models/block/", blockId.getPath(), ".json"));
	}

	private static ResourceLocation getItemsModelLocation(final ResourceLocation itemId) {
		return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), String.join("", "items/", itemId.getPath(), ".json"));
	}

	private static ResourceLocation getItemModelLocation(final ResourceLocation itemId) {
		return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), String.join("", "models/item/", itemId.getPath(), ".json"));
	}

	private static boolean shouldDumpAssets() {
		try {
			return ModConfig.debug_dumpRuntimeResourcePack.getAsBoolean();
		} catch (final IllegalStateException ignored) {
			//This happens when the mod throws an exception on startup before neoforge has loaded the configs.
			//We are likely in a dev env right now, so default to true
			return true;
		}
	}

	private static void writeJson(final ResourceLocation id, final JsonElement json) {
		if (RuntimeResourcePack.shouldDumpAssets()) {
			AbstractRuntimePack.dump("assets", id, null, json);
		}
	}
}
