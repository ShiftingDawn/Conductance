package conductance.lib.pack.server;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import com.google.gson.JsonElement;
import conductance.ModConfig;
import conductance.lib.pack.AbstractRuntimePack;

final class RuntimeDataPack extends AbstractRuntimePack {

	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();

	RuntimeDataPack(final PackLocationInfo info) {
		super(info, PackType.SERVER_DATA);
	}

	static void reset() {
		RuntimeDataPack.DATA.clear();
	}

	@Override
	protected Map<ResourceLocation, byte[]> getAllData() {
		return RuntimeDataPack.DATA;
	}

	static void addRecipe(final ResourceLocation recipeId, final JsonElement recipe) {
		final ResourceLocation recipeLocation = RuntimeDataPack.getRecipeLocation(recipeId);
		RuntimeDataPack.writeJson(recipeLocation, recipe);
		RuntimeDataPack.DATA.put(recipeLocation, recipe.toString().getBytes(StandardCharsets.UTF_8));
	}

	private static ResourceLocation getRecipeLocation(final ResourceLocation recipeId) {
		return ResourceLocation.fromNamespaceAndPath(recipeId.getNamespace(), "recipe/%s.json".formatted(recipeId.getPath()));
	}

	private static ResourceLocation getAdvancementLocation(final ResourceLocation advancementId) {
		return ResourceLocation.fromNamespaceAndPath(advancementId.getNamespace(), "advancement/%s.json".formatted(advancementId.getPath()));
	}

	private static boolean shouldDumpAssets() {
		try {
			return ModConfig.debug_dumpRuntimeDataPack.getAsBoolean();
		} catch (final IllegalStateException ignored) {
			//This happens when the mod throws an exception on startup before neoforge has loaded the configs.
			//We are likely in a dev env right now, so default to true
			return true;
		}
	}

	private static void writeJson(final ResourceLocation id, final JsonElement json) {
		if (RuntimeDataPack.shouldDumpAssets()) {
			AbstractRuntimePack.dump("data", id, null, json);
		}
	}
}
