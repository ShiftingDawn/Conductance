package conductance.client.resourcepack;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.Conductance;
import conductance.Config;
import conductance.core.AbstractRuntimePack;

public final class RuntimeResourcePack extends AbstractRuntimePack {

	private static final Set<String> KNOWN_NAMESPACES = new ObjectOpenHashSet<>(Sets.newHashSet(CAPI.MOD_ID, ResourceLocation.DEFAULT_NAMESPACE, "c", "neoforge"));
	private static final List<Runnable> RELOAD_LISTENERS = new ArrayList<>();
	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();

	public RuntimeResourcePack(final PackLocationInfo location) {
		super(location, PackType.CLIENT_RESOURCES);
	}

	@Override
	protected Map<ResourceLocation, byte[]> getData() {
		return RuntimeResourcePack.DATA;
	}

	@Override
	public Set<String> getKnownNamespaces() {
		return RuntimeResourcePack.KNOWN_NAMESPACES;
	}

	public static void addReloadListener(final Runnable reloadListener) {
		RuntimeResourcePack.RELOAD_LISTENERS.add(reloadListener);
	}

	private static boolean shouldDumpAssets() {
		return Config.debug_dumpRuntimeResourcePack.getAsBoolean();
	}

	static void reset() {
		RuntimeResourcePack.DATA.clear();
		RuntimeResourcePack.RELOAD_LISTENERS.clear();

		// Register built-in listeners
		addReloadListener(MaterialTextureSetModelHandler::reload);
		addReloadListener(MaterialModelHandler::reload);

		// TODO reset translationregistry
	}

	public static void load() {
		final long sysTime = System.currentTimeMillis();
		RuntimeResourcePack.RELOAD_LISTENERS.forEach(Runnable::run);
		Conductance.LOGGER.info("Conductance reloaded RuntimeResourcePack in {}ms", System.currentTimeMillis() - sysTime);
	}

	public static void addItemModel(ResourceLocation location, Supplier<JsonElement> itemModelGenerator) {
		RuntimeResourcePack.addItemModel(location, itemModelGenerator.get());
	}

	public static void addItemModel(final ResourceLocation location, final JsonElement itemModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getItemModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, itemModel);
		RuntimeResourcePack.DATA.put(realLocation, itemModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	public static void addBlockModel(ResourceLocation location, Supplier<JsonElement> blockModelGenerator) {
		RuntimeResourcePack.addBlockModel(location, blockModelGenerator.get());
	}

	public static void addBlockModel(final ResourceLocation location, final JsonElement blockModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getBlockModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, blockModel);
		RuntimeResourcePack.DATA.put(realLocation, blockModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	private static ResourceLocation getItemModelLocation(final ResourceLocation itemId) {
		return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), String.join("", "models/item/", itemId.getPath(), ".json"));
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
