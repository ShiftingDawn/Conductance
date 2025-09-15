package conductance.lib.pack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import conductance.Conductance;

public abstract class AbstractRuntimePack implements PackResources {

	private final Set<String> namespaces = new HashSet<>();
	private final PackLocationInfo info;
	private final PackType type;

	protected AbstractRuntimePack(final PackLocationInfo info, final PackType type) {
		this.info = info;
		this.type = type;
		this.namespaces.addAll(List.of(Conductance.MODID, ResourceLocation.DEFAULT_NAMESPACE, "c", NeoForgeVersion.MOD_ID));
		this.namespaces.addAll(Conductance.getKnownPluginNamespaces());
	}

	protected abstract Map<ResourceLocation, byte[]> getAllData();

	@Override
	@Nullable
	public IoSupplier<InputStream> getRootResource(final String... strings) {
		return null;
	}

	@Override
	@Nullable
	public IoSupplier<InputStream> getResource(final PackType packType, final ResourceLocation resourceLocation) {
		if (packType == this.type && this.getAllData().containsKey(resourceLocation)) {
			return () -> new ByteArrayInputStream(this.getAllData().get(resourceLocation));
		}
		return null;
	}

	@Override
	public void listResources(final PackType packType, final String namespace, final String path, final ResourceOutput resourceOutput) {
		if (packType == this.type) {
			final String path2 = path.endsWith("/") ? path : path + "/";
			this.getAllData().keySet().stream().filter(Objects::nonNull).filter(loc -> loc.getPath().startsWith(path2)).forEach(location -> {
				final IoSupplier<InputStream> resource = this.getResource(packType, location);
				if (resource != null) {
					resourceOutput.accept(location, resource);
				}
			});
		}
	}

	@Override
	public Set<String> getNamespaces(final PackType packType) {
		return packType == this.type ? this.namespaces : Set.of();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getMetadataSection(final MetadataSectionType<T> metadataSectionType) {
		if (metadataSectionType == PackMetadataSection.TYPE) {
			return (T) new PackMetadataSection(this.info.title(), SharedConstants.getCurrentVersion().packVersion(this.type));
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

	public static void dump(final String type, final ResourceLocation id, @Nullable final String subDirectory, final JsonElement json) {
		final Path dumpPath = FMLPaths.getOrCreateGameRelativePath(Path.of(Conductance.MODID, "dumped", type));
		try {
			final Path file;
			if (subDirectory != null) {
				file = dumpPath.resolve(id.getNamespace()).resolve(subDirectory).resolve(id.getPath() + ".json");
			} else {
				file = dumpPath.resolve(id.getNamespace()).resolve(id.getPath());
			}
			Files.createDirectories(file.getParent());
			//noinspection CheckStyle
			try (final OutputStream output = Files.newOutputStream(file)) {
				output.write(json.toString().getBytes());
			}
		} catch (final IOException e) {
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
		}
	}
}
