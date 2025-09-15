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

	static void addTranslation(final String key, final String translation) {
		RuntimeResourcePack.TRANSLATIONS.put(key, translation);
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
