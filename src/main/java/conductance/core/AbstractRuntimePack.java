package conductance.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import conductance.Conductance;

public abstract class AbstractRuntimePack implements PackResources {

	private final PackLocationInfo locationInfo;
	private final PackType packType;

	public AbstractRuntimePack(PackLocationInfo locationInfo, PackType packType) {
		this.locationInfo = locationInfo;
		this.packType = packType;
	}

	protected abstract Map<ResourceLocation, byte[]> getData();

	protected abstract Set<String> getKnownNamespaces();

	@Nullable
	@Override
	public IoSupplier<InputStream> getRootResource(String... strings) {
		return null;
	}

	@Nullable
	@Override
	public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
		if (packType == this.packType && this.getData().containsKey(resourceLocation)) {
			return () -> new ByteArrayInputStream(this.getData().get(resourceLocation));
		}
		return null;
	}

	@Override
	public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
		if (packType == this.packType) {
			final String path2 = path.endsWith("/") ? path : path + "/";
			this.getData().keySet().stream().filter(Objects::nonNull).filter(loc -> loc.getPath().startsWith(path2)).forEach(location -> {
				final IoSupplier<InputStream> resource = this.getResource(packType, location);
				if (resource != null) {
					resourceOutput.accept(location, resource);
				}
			});
		}
	}

	@Override
	public Set<String> getNamespaces(PackType packType) {
		return packType == this.packType ? this.getKnownNamespaces() : Set.of();
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {
		if (metadataSectionSerializer == PackMetadataSection.TYPE) {
			return (T) new PackMetadataSection(Component.literal("Conductance RuntimePack " + this.packType), SharedConstants.getCurrentVersion().getPackVersion(this.packType));
		}
		return null;
	}

	@Override
	public PackLocationInfo location() {
		return this.locationInfo;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isHidden() {
		return true;
	}

	public static void dump(String type, final ResourceLocation id, @Nullable final String subDirectory, final JsonElement json) {
		Path dumpPath = FMLPaths.getOrCreateGameRelativePath(Path.of(Conductance.MODID, "dumped", type));
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
