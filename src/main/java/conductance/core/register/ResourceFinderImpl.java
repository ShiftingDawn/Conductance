package conductance.core.register;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCTextureSets;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;
import conductance.api.resource.ResourceFinder;
import conductance.api.util.SafeOptional;
import conductance.core.material.MaterialTextureSetLoader;

@SuppressWarnings({ "DataFlowIssue", "ConstantValue" })
final class ResourceFinderImpl implements ResourceFinder {

	private final Table<ResourceLocation, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> textureCache = HashBasedTable.create();

	private final Table<ResourceLocation, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> itemModelCache = HashBasedTable.create();
	private final Table<ResourceLocation, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> blockModelCache = HashBasedTable.create();

	@Override
	public SafeOptional<ResourceLocation> getMaterialTexture(final ResourceLocation textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = ResourceFinderImpl.makeSafe(pathPrefix);
		final String suffix = ResourceFinderImpl.makeSafe(pathSuffix);
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.textureCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getResourceCascaded(
				"textures", textureSet, textureType,
				() -> "material/%s/%s/" + prefix + "%s" + suffix + ".png",
				() -> "material/%s/%s/" + prefix + "%s" + suffix
		);
		rootTable.put(prefix, suffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getMaterialItemModel(final ResourceLocation textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix;
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.itemModelCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getResourceCascaded(
				"models", textureSet, textureType,
				() -> "item/material/%s/%s/" + prefix + "%s" + suffix + ".json",
				() -> "item/material/%s/%s/" + prefix + "%s" + suffix
		);
		rootTable.put(prefix, suffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getMaterialBlockModel(final ResourceLocation textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix;
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.blockModelCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getResourceCascaded(
				"models", textureSet, textureType,
				() -> "block/material/%s/%s/" + prefix + "%s" + suffix + ".json",
				() -> "block/material/%s/%s/" + prefix + "%s" + suffix
		);
		rootTable.put(prefix, suffix, resource);
		return resource;
	}

	@Override
	public boolean isResourceValid(final ResourceLocation resource) {
		if (ResourceFinderImpl.class.getResource(String.format("/assets/%s/%s", resource.getNamespace(), resource.getPath())) != null) {
			return true;
		}
		if (CAPI.isClient() && Minecraft.getInstance() != null && Minecraft.getInstance().getResourceManager() != null) {
			return Minecraft.getInstance().getResourceManager().getResource(resource).isPresent();
		}
		return false;
	}

	@Override
	@Nullable
	public ResourceLocation getCustomMaterialTexture(final Material material, final MaterialTextureType type) {
		final ResourceLocation texturePath = material.getRegistryKey().withPath("material/custom/%s/%s".formatted(material.getRegistryKey().getPath(), type.getRegistryKey().getPath()));
		return this.isTextureValid(texturePath) ? texturePath : null;
	}

	private SafeOptional<ResourceLocation> getResourceCascaded(final String resourceType, final ResourceLocation set, final MaterialTextureType type,
			final Supplier<String> resourcePathFactory, final Supplier<String> assetPathFactory) {
		ResourceLocation currentSet = set;
		while (currentSet != null) {
			final ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
					type.getRegistryKey().getNamespace(),
					resourceType + "/" + resourcePathFactory.get().formatted(currentSet.getNamespace(), currentSet.getPath(), type.getRegistryKey().getPath())
			);
			if (this.isResourceValid(location)) {
				break;
			}
			currentSet = MaterialTextureSetLoader.getParentSet(currentSet);
		}
		if (currentSet == null) {
			currentSet = NCTextureSets.DULL;
		}
		final ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
				type.getRegistryKey().getNamespace(),
				assetPathFactory.get().formatted(currentSet.getNamespace(), currentSet.getPath(), type.getRegistryKey().getPath())
		);
		return this.isResourceValid(location) ? SafeOptional.of(location) : SafeOptional.ofFallback(location);
	}

	private static Table<String, String, SafeOptional<ResourceLocation>> innerTable(final Table<ResourceLocation, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> parentTable,
			final ResourceLocation set, final MaterialTextureType type) {
		Table<String, String, SafeOptional<ResourceLocation>> rootTable = parentTable.get(set, type);
		if (rootTable == null) {
			rootTable = HashBasedTable.create();
			parentTable.put(set, type, rootTable);
		}
		return rootTable;
	}

	private static String makeSafe(@Nullable final String str) {
		return str == null || str.isBlank() ? "" : str;
	}
}
