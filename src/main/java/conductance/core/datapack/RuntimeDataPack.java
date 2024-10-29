package conductance.core.datapack;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.Tuple;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.Config;
import conductance.core.AbstractRuntimePack;

public final class RuntimeDataPack extends AbstractRuntimePack {

	private static final Set<String> KNOWN_NAMESPACES = new ObjectOpenHashSet<>(Sets.newHashSet(CAPI.MOD_ID, ResourceLocation.DEFAULT_NAMESPACE, "c", "neoforge"));
	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();

	public RuntimeDataPack(final PackLocationInfo location) {
		super(location, PackType.SERVER_DATA);
	}

	@Override
	protected Map<ResourceLocation, byte[]> getData() {
		return RuntimeDataPack.DATA;
	}

	@Override
	public Set<String> getKnownNamespaces() {
		return RuntimeDataPack.KNOWN_NAMESPACES;
	}

	private static boolean shouldDumpAssets() {
		return Config.debug_dumpRuntimeDataPack.getAsBoolean();
	}

	static void reset() {
		RuntimeDataPack.DATA.clear();
	}

	public static void addRecipe(final ResourceLocation recipeId, final Supplier<JsonElement> recipe, final Optional<Supplier<Tuple<JsonElement, ResourceLocation>>> advancement) {
		Util.make(recipe.get(), json -> {
			final ResourceLocation recipeLocation = RuntimeDataPack.getRecipeLocation(recipeId);
			RuntimeDataPack.writeJson(recipeLocation, null, json);
			RuntimeDataPack.DATA.put(recipeLocation, json.toString().getBytes(StandardCharsets.UTF_8));
		});
		advancement.ifPresent(adv -> Util.make(adv.get(), tuple -> {
			final ResourceLocation advancementLocation = RuntimeDataPack.getAdvancementLocation(tuple.getB());
			RuntimeDataPack.writeJson(advancementLocation, null, tuple.getA());
			RuntimeDataPack.DATA.put(advancementLocation, tuple.getA().toString().getBytes(StandardCharsets.UTF_8));
		}));
	}

	private static ResourceLocation getRecipeLocation(final ResourceLocation recipeId) {
		return ResourceLocation.fromNamespaceAndPath(recipeId.getNamespace(), "recipe/%s.json".formatted(recipeId.getPath()));
	}

	private static ResourceLocation getAdvancementLocation(final ResourceLocation advancementId) {
		return ResourceLocation.fromNamespaceAndPath(advancementId.getNamespace(), "advancement/%s.json".formatted(advancementId.getPath()));
	}

	private static void writeJson(final ResourceLocation id, @Nullable final String subDirectory, final JsonElement json) {
		if (RuntimeDataPack.shouldDumpAssets()) {
			AbstractRuntimePack.dump("data", id, subDirectory, json);
		}
	}
}
