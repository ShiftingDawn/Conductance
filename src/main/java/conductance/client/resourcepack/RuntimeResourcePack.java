package conductance.client.resourcepack;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.Conductance;

public final class RuntimeResourcePack implements PackResources {

	private static final Set<String> KNOWN_NAMESPACES = new ObjectOpenHashSet<>(Sets.newHashSet(CAPI.MOD_ID, ResourceLocation.DEFAULT_NAMESPACE, "c", "neoforge"));
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

	static void reset() {
		RuntimeResourcePack.DATA.clear();
	}

	public static void load() {
		final long sysTime = System.currentTimeMillis();

		Conductance.LOGGER.info("Conductance reloaded RuntimeResourcePack in {}ms", System.currentTimeMillis() - sysTime);
	}
}
