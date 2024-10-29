package conductance.client.resourcepack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import net.neoforged.fml.loading.FMLPaths;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.Conductance;
import conductance.Config;

public final class RuntimeResourcePack implements PackResources {

	private static final Set<String> KNOWN_NAMESPACES = new ObjectOpenHashSet<>(Sets.newHashSet(CAPI.MOD_ID, ResourceLocation.DEFAULT_NAMESPACE, "c", "neoforge"));
	private static final List<Runnable> RELOAD_LISTENERS = new ArrayList<>();
	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();
	private final PackLocationInfo info;

	public RuntimeResourcePack(final PackLocationInfo info) {
		this.info = info;
	}

	@Nullable
	@Override
	public IoSupplier<InputStream> getRootResource(final String... strings) {
		return null;
	}

	@Nullable
	@Override
	public IoSupplier<InputStream> getResource(final PackType packType, final ResourceLocation resourceLocation) {
		if (packType == PackType.CLIENT_RESOURCES && RuntimeResourcePack.DATA.containsKey(resourceLocation)) {
			return () -> new ByteArrayInputStream(RuntimeResourcePack.DATA.get(resourceLocation));
		}
		return null;
	}

	@Override
	public void listResources(final PackType packType, final String namespace, final String path, final ResourceOutput resourceOutput) {
		if (packType == PackType.CLIENT_RESOURCES) {
			final String path2 = path.endsWith("/") ? path : path + "/";
			RuntimeResourcePack.DATA.keySet().stream().filter(Objects::nonNull).filter(loc -> loc.getPath().startsWith(path2)).forEach(location -> {
				final IoSupplier<InputStream> resource = this.getResource(packType, location);
				if (resource != null) {
					resourceOutput.accept(location, resource);
				}
			});
		}
	}

	@Override
	public Set<String> getNamespaces(final PackType packType) {
		return packType == PackType.CLIENT_RESOURCES ? RuntimeResourcePack.KNOWN_NAMESPACES : Set.of();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getMetadataSection(final MetadataSectionSerializer<T> metadataSectionSerializer) {
		if (metadataSectionSerializer == PackMetadataSection.TYPE) {
			return (T) new PackMetadataSection(Component.literal("Conductance Runtime ResourcePack"), SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
		}
		return null;
	}

	@Override
	public PackLocationInfo location() {
		return this.info;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isHidden() {
		return true;
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
			Path dumpPath = FMLPaths.getOrCreateGameRelativePath(Path.of(Conductance.MODID, "dumped", "assets"));
			try {
				final Path file;
				if (subDirectory != null) {
					file = dumpPath.resolve(id.getNamespace()).resolve(subDirectory).resolve(id.getPath() + ".json");
				} else {
					file = dumpPath.resolve(id.getNamespace()).resolve(id.getPath());
				}
				Files.createDirectories(file.getParent());
				try (final OutputStream output = Files.newOutputStream(file)) {
					output.write(json.toString().getBytes());
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}
