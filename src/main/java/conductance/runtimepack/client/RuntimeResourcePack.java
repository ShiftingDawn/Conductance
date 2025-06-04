package conductance.runtimepack.client;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.resource.event.AddBlockModelEvent;
import conductance.api.resource.event.AddBlockStateEvent;
import conductance.api.resource.event.AddItemModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.resource.event.ReloadingRuntimeResourcePackEvent;
import conductance.Conductance;
import conductance.Config;
import conductance.core.apiimpl.TranslationRegistryImpl;
import conductance.core.machine.BlockModelBuilderImpl;
import conductance.core.machine.BlockStateBuilderImpl;
import conductance.core.machine.ItemModelBuilderImpl;
import conductance.loader.PluginEventBus;
import conductance.runtimepack.AbstractRuntimePack;

public final class RuntimeResourcePack extends AbstractRuntimePack {

	private static final Set<String> KNOWN_NAMESPACES = new ObjectOpenHashSet<>(Sets.newHashSet(CAPI.MOD_ID, ResourceLocation.DEFAULT_NAMESPACE, "c", "neoforge"));
	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();
	private static final Map<String, String> TRANSLATIONS = new ConcurrentHashMap<>();

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

	private static boolean shouldDumpAssets() {
		try {
			return Config.debug_dumpRuntimeResourcePack.getAsBoolean();
		} catch (final IllegalStateException ignored) {
			//This happens when crashing on startup before configs are loaded.
			//We are likely in a dev env right now, so default to true
			return true;
		}
	}

	//TODO this should probably happen on F3+R too?
	static void reset() {
		RuntimeResourcePack.DATA.clear();
		RuntimeResourcePack.TRANSLATIONS.clear();
		TranslationRegistryImpl.INSTANCE.reset();
	}

	public static void load() {
		final long sysTime = System.currentTimeMillis();
		//Dispatch events
		PluginEventBus.postAll(AddBlockStateEvent.class, new AddBlockStateEventImpl((location, builder) -> {
			final JsonObject data = Util.make(new BlockStateBuilderImpl(), builder).build();
			RuntimeResourcePack.addBlockState(location, data);
		}));
		PluginEventBus.postAll(AddBlockModelEvent.class, new AddBlockModelEventImpl((location, builder) -> {
			final JsonObject data = Util.make(new BlockModelBuilderImpl(), builder).build();
			RuntimeResourcePack.addBlockModel(location, data);
		}));
		PluginEventBus.postAll(AddItemModelEvent.class, new AddItemModelEventImpl((location, builder) -> {
			final JsonObject data = Util.make(new ItemModelBuilderImpl(), builder).build();
			RuntimeResourcePack.addItemModel(location, data);
		}));
		PluginEventBus.postAll(AddTranslationEvent.class, new AddTranslationEventImpl(RuntimeResourcePack::addTranslation));
		PluginEventBus.postAll(ReloadingRuntimeResourcePackEvent.class, new ReloadingRuntimeResourcePackEventImpl(
				RuntimeResourcePack::addBlockState, RuntimeResourcePack::addBlockModel, RuntimeResourcePack::addItemModel
		));
		//Build translations
		RuntimeResourcePack.DATA.put(Conductance.id("lang/en_us.json"), Util.make(new JsonObject(), json -> {
			RuntimeResourcePack.TRANSLATIONS.forEach(json::addProperty);
			RuntimeResourcePack.writeJson(Conductance.id("lang/en_us.json"), null, json);
		}).toString().getBytes(StandardCharsets.UTF_8));
		Conductance.LOGGER.info("Conductance reloaded RuntimeResourcePack in {}ms", System.currentTimeMillis() - sysTime);
	}

	private static void addBlockState(final ResourceLocation location, final JsonElement blockState) {
		final ResourceLocation realLocation = RuntimeResourcePack.getBlockStateLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, blockState);
		RuntimeResourcePack.DATA.put(realLocation, blockState.toString().getBytes(StandardCharsets.UTF_8));
	}

	private static void addBlockModel(final ResourceLocation location, final JsonElement blockModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getBlockModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, blockModel);
		RuntimeResourcePack.DATA.put(realLocation, blockModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	private static void addItemModel(final ResourceLocation location, final JsonElement itemModel) {
		final ResourceLocation realLocation = RuntimeResourcePack.getItemModelLocation(location);
		RuntimeResourcePack.writeJson(realLocation, null, itemModel);
		RuntimeResourcePack.DATA.put(realLocation, itemModel.toString().getBytes(StandardCharsets.UTF_8));
	}

	private static void addTranslation(final String key, final String translation) {
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
