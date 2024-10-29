package conductance.core.apiimpl;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.ResourceFinder;
import conductance.api.util.SafeOptional;

@SuppressWarnings({ "DataFlowIssue", "ConstantValue" })
final class ResourceFinderImpl implements ResourceFinder {

	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> blockTextureCache = HashBasedTable.create();
	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> itemTextureCache = HashBasedTable.create();
	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> fluidTextureCache = HashBasedTable.create();

	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> itemModelCache = HashBasedTable.create();
	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> blockModelCache = HashBasedTable.create();

	@Override
	public SafeOptional<ResourceLocation> getItemTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		pathPrefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		pathSuffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = innerTable(itemTextureCache, textureSet, textureType);
		if (rootTable.contains(pathPrefix, pathSuffix)) {
			return rootTable.get(pathPrefix, pathSuffix);
		}
		final SafeOptional<ResourceLocation> resource = getItemTextureCascaded(textureSet, textureType, pathPrefix, pathSuffix);
		rootTable.put(pathPrefix, pathSuffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getItemModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		pathPrefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		pathSuffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = innerTable(itemModelCache, textureSet, textureType);
		if (rootTable.contains(pathPrefix, pathSuffix)) {
			return rootTable.get(pathPrefix, pathSuffix);
		}
		final SafeOptional<ResourceLocation> resource = getItemModelCascaded(textureSet, textureType, pathPrefix, pathSuffix);
		rootTable.put(pathPrefix, pathSuffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getBlockTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		pathPrefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		pathSuffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = innerTable(blockTextureCache, textureSet, textureType);
		if (rootTable.contains(pathPrefix, pathSuffix)) {
			return rootTable.get(pathPrefix, pathSuffix);
		}
		final SafeOptional<ResourceLocation> resource = getBlockTextureCascaded(textureSet, textureType, pathPrefix, pathSuffix);
		rootTable.put(pathPrefix, pathSuffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getBlockModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		pathPrefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		pathSuffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = innerTable(blockModelCache, textureSet, textureType);
		if (rootTable.contains(pathPrefix, pathSuffix)) {
			return rootTable.get(pathPrefix, pathSuffix);
		}
		final SafeOptional<ResourceLocation> resource = getBlockModelCascaded(textureSet, textureType, pathPrefix, pathSuffix);
		rootTable.put(pathPrefix, pathSuffix, resource);
		return resource;
	}

	@Override
	/*
	 * Textures are stored in the block directory, We cache it in a different table
	 * to prevent clashes between the placeable fluid-block and the virtual fluid
	 */
	public SafeOptional<ResourceLocation> getFluidTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		pathPrefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		pathSuffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = innerTable(fluidTextureCache, textureSet, textureType);
		if (rootTable.contains(pathPrefix, pathSuffix)) {
			return rootTable.get(pathPrefix, pathSuffix);
		}
		final SafeOptional<ResourceLocation> resource = getBlockTextureCascaded(textureSet, textureType, pathPrefix, pathSuffix);
		rootTable.put(pathPrefix, pathSuffix, resource);
		return resource;
	}

	@Override
	public boolean isResourceValid(ResourceLocation resource) {
		if (ResourceFinderImpl.class.getResource(String.format("/assets/%s/%s", resource.getNamespace(), resource.getPath())) != null) {
			return true;
		}
		if (FMLLoader.getDist().isClient() && Minecraft.getInstance() != null && Minecraft.getInstance().getResourceManager() != null) {
			return Minecraft.getInstance().getResourceManager().getResource(resource).isPresent();
		}
		return false;
	}

	private SafeOptional<ResourceLocation> getItemTextureCascaded(final MaterialTextureSet set, final MaterialTextureType type, @javax.annotation.Nullable final String prefix, @javax.annotation.Nullable final String suffix) {
		return getResourceCascaded("textures", "item", set, type, "png", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getItemModelCascaded(final MaterialTextureSet set, final MaterialTextureType type, @javax.annotation.Nullable final String prefix, @javax.annotation.Nullable final String suffix) {
		return getResourceCascaded("models", "item", set, type, "json", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getBlockTextureCascaded(final MaterialTextureSet set, final MaterialTextureType type, @javax.annotation.Nullable final String prefix, @javax.annotation.Nullable final String suffix) {
		return getResourceCascaded("textures", "block", set, type, "png", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getBlockModelCascaded(final MaterialTextureSet set, final MaterialTextureType type, @javax.annotation.Nullable final String prefix, @javax.annotation.Nullable final String suffix) {
		return getResourceCascaded("models", "block", set, type, "json", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getResourceCascaded(final String resourceType, final String pathPrepend, final MaterialTextureSet set, final MaterialTextureType type, @javax.annotation.Nullable final String extension, @javax.annotation.Nullable String prefix, @javax.annotation.Nullable String suffix) {
		prefix = prefix == null || prefix.isBlank() ? "" : prefix.endsWith("_") ? prefix : prefix + "_";
		suffix = suffix == null || suffix.isBlank() ? "" : suffix.startsWith("_") ? suffix : "_" + suffix;
		MaterialTextureSet currentSet = set;
		while (!currentSet.isRootSet()) {
			final ResourceLocation location = getResourceUnchecked("%s/%s".formatted(resourceType, pathPrepend), currentSet, type, prefix, suffix + (extension != null ? "." + extension : ""));
			if (isResourceValid(location)) {
				break;
			}
			currentSet = currentSet.getParentSet();
		}
		final ResourceLocation location = getResourceUnchecked(pathPrepend, currentSet, type, prefix, suffix);
		if (!isResourceValid(location)) {
			// CAPI.LOGGER.warn("Could not find cascaded resource {} while looking for: {}",
			// location, MaterialUtils.getResourceUnchecked("%s/%s".formatted(resourceType,
			// pathPrepend), set, type, prefix, suffix + (extension != null ? "." +
			// extension : "")));
			return SafeOptional.ofFallback(location);
		}
		return SafeOptional.of(location);
	}

	private static ResourceLocation getResourceUnchecked(final String pathPrepend, final MaterialTextureSet set, final MaterialTextureType texType, final String prefix, final String suffix) {
		return ResourceLocation.fromNamespaceAndPath(texType.getRegistryKey().getNamespace(), "%s/material/%s/%s%s%s".formatted(pathPrepend, set.getRegistryKey(), prefix, texType.getRegistryKey().getPath(), suffix));
	}

	private static Table<String, String, SafeOptional<ResourceLocation>> innerTable(final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> parentTable, final MaterialTextureSet set, final MaterialTextureType type) {
		Table<String, String, SafeOptional<ResourceLocation>> rootTable = parentTable.get(set, type);
		if (rootTable == null) {
			rootTable = HashBasedTable.create();
			parentTable.put(set, type, rootTable);
		}
		return rootTable;
	}
}
