package conductance.core.runtimepack.client;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.Conductance;
import conductance.Config;
import conductance.core.apiimpl.TranslationRegistryImpl;
import conductance.core.runtimepack.AbstractRuntimePack;

final class RuntimeResourcePack extends AbstractRuntimePack {

	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();
	private static final Map<String, String> TRANSLATIONS = new ConcurrentHashMap<>();

	RuntimeResourcePack(final PackLocationInfo location) {
		super(location, PackType.CLIENT_RESOURCES);
	}

	@Override
	protected Map<ResourceLocation, byte[]> getAllData() {
		return RuntimeResourcePack.DATA;
	}

	private static boolean shouldDumpAssets() {
		try {
			return Config.debug_dumpRuntimeResourcePack.getAsBoolean();
		} catch (final IllegalStateException ignored) {
			//This happens when crashing on startup before configs are loaded.
			//We are likely in a dev env right now, so default to true
			return true;
		}
	}

	static void resetStatesAndModels() {
		RuntimeResourcePack.DATA.keySet().forEach(key -> {
			if (key.getPath().startsWith("blockstates") || key.getPath().startsWith("models")) {
				RuntimeResourcePack.DATA.remove(key);
			}
		});
	}

	static void resetTranslations() {
		RuntimeResourcePack.TRANSLATIONS.clear();
		RuntimeResourcePack.DATA.remove(Conductance.id("lang/en_us.json"));
		TranslationRegistryImpl.INSTANCE.reset();
	}

	static void freezeTranslations() {
		RuntimeResourcePack.DATA.put(Conductance.id("lang/en_us.json"), Util.make(new JsonObject(), json -> {
			RuntimeResourcePack.TRANSLATIONS.forEach(json::addProperty);
			RuntimeResourcePack.writeJson(Conductance.id("lang/en_us.json"), null, json);
		}).toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addBlockState(final ResourceLocation location, final JsonElement blockState) {
		final ResourceLocation realLocation = RuntimeResourcePack.getBlockStateLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, blockState);
		RuntimeResourcePack.DATA.put(realLocation, blockState.toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addBlockModel(final ResourceLocation location, final JsonElement blockModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getBlockModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, blockModel);
		RuntimeResourcePack.DATA.put(realLocation, blockModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addItemModel(final ResourceLocation location, final JsonElement itemModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getItemModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, itemModel);
		RuntimeResourcePack.DATA.put(realLocation, itemModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	static void addTranslation(final String key, final String translation) {
		RuntimeResourcePack.TRANSLATIONS.put(key, translation);
	}

	private static ResourceLocation getItemModelLocation(final ResourceLocation itemId) {
		return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), String.join("", "models/item/", itemId.getPath(), ".json"));
	}

	private static ResourceLocation getBlockStateLocation(final ResourceLocation blockId) {
		return ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), String.join("", "blockstates/", blockId.getPath(), ".json"));
	}

	private static ResourceLocation getBlockModelLocation(final ResourceLocation blockId) {
		return ResourceLocation.fromNamespaceAndPath(blockId.getNamespace(), String.join("", "models/block/", blockId.getPath(), ".json"));
	}

	private static void writeJson(final ResourceLocation id, @Nullable final String subDirectory, final JsonElement json) {
		if (RuntimeResourcePack.shouldDumpAssets()) {
			AbstractRuntimePack.dump("assets", id, subDirectory, json);
		}
	}
}
