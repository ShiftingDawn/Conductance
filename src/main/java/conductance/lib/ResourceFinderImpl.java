package conductance.lib;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialTextureSets;
import conductance.api.material.Material;
import conductance.api.resource.ResourceFinder;
import conductance.api.util.SafeOptional;

@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
public final class ResourceFinderImpl implements ResourceFinder {

	private final Table<ResourceLocation, ResourceLocation, Table<String, String, SafeOptional<ResourceLocation>>> textureCache = HashBasedTable.create();
	private final Table<ResourceLocation, ResourceLocation, Table<String, String, SafeOptional<ResourceLocation>>> itemModelCache = HashBasedTable.create();
	private final Table<ResourceLocation, ResourceLocation, Table<String, String, SafeOptional<ResourceLocation>>> blockModelCache = HashBasedTable.create();

	@Override
	public SafeOptional<ResourceLocation> getMaterialTexture(final ResourceLocation textureSet, final ResourceLocation textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
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
	public SafeOptional<ResourceLocation> getMaterialItemModel(final ResourceLocation textureSet, final ResourceLocation textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
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
	public SafeOptional<ResourceLocation> getMaterialBlockModel(final ResourceLocation textureSet, final ResourceLocation textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
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
	@Nullable
	public ResourceLocation getCustomMaterialTexture(final Material material, final ResourceLocation textureType) {
		final ResourceLocation texturePath = material.getId().withPath(name -> "material/custom/%s/%s".formatted(name, textureType.getPath()));
		return this.isTextureValid(texturePath) ? texturePath : null;
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

	private SafeOptional<ResourceLocation> getResourceCascaded(
			final String resourceType, final ResourceLocation textureSet, final ResourceLocation textureType,
			final Supplier<String> resourcePathFactory, final Supplier<String> assetPathFactory) {
		ResourceLocation currentSet = textureSet;
		while (currentSet != null) {
			final ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
					textureType.getNamespace(),
					resourceType + "/" + resourcePathFactory.get().formatted(currentSet.getNamespace(), currentSet.getPath(), textureType.getPath())
			);
			if (this.isResourceValid(location)) {
				break;
			}
			currentSet = MaterialTextureSetLoader.getParentSet(currentSet);
		}
		if (currentSet == null) {
			currentSet = NCMaterialTextureSets.DULL;
		}
		final ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
				textureType.getNamespace(),
				assetPathFactory.get().formatted(currentSet.getNamespace(), currentSet.getPath(), textureType.getPath())
		);
		return this.isResourceValid(location) ? SafeOptional.of(location) : SafeOptional.ofFallback(location);
	}

	private static Table<String, String, SafeOptional<ResourceLocation>> innerTable(
			final Table<ResourceLocation, ResourceLocation, Table<String, String, SafeOptional<ResourceLocation>>> parentTable,
			final ResourceLocation textureSet, final ResourceLocation textureType) {
		Table<String, String, SafeOptional<ResourceLocation>> rootTable = parentTable.get(textureSet, textureType);
		if (rootTable == null) {
			rootTable = HashBasedTable.create();
			parentTable.put(textureSet, textureType, rootTable);
		}
		return rootTable;
	}

	private static String makeSafe(@Nullable final String str) {
		return str == null || str.isBlank() ? "" : str;
	}
}
