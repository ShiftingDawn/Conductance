package conductance.core.apiimpl;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.ResourceFinder;
import conductance.api.util.SafeOptional;
import conductance.Conductance;
import conductance.Config;

@SuppressWarnings({ "DataFlowIssue", "ConstantValue" })
final class ResourceFinderImpl implements ResourceFinder {

	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> blockTextureCache = HashBasedTable.create();
	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> itemTextureCache = HashBasedTable.create();
	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> fluidTextureCache = HashBasedTable.create();

	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> itemModelCache = HashBasedTable.create();
	private final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> blockModelCache = HashBasedTable.create();

	@Override
	public SafeOptional<ResourceLocation> getItemTexture(final MaterialTextureSet textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.itemTextureCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getItemTextureCascaded(textureSet, textureType, prefix, suffix);
		rootTable.put(prefix, suffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getItemModel(final MaterialTextureSet textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.itemModelCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getItemModelCascaded(textureSet, textureType, prefix, suffix);
		rootTable.put(prefix, suffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getBlockTexture(final MaterialTextureSet textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.blockTextureCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getBlockTextureCascaded(textureSet, textureType, prefix, suffix);
		rootTable.put(prefix, suffix, resource);
		return resource;
	}

	@Override
	public SafeOptional<ResourceLocation> getBlockModel(final MaterialTextureSet textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.blockModelCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getBlockModelCascaded(textureSet, textureType, prefix, suffix);
		rootTable.put(prefix, suffix, resource);
		return resource;
	}

	@Override
	/*
	 * Textures are stored in the block directory, We cache it in a different table
	 * to prevent clashes between the placeable fluid-block and the virtual fluid.
	 */ public SafeOptional<ResourceLocation> getFluidTexture(final MaterialTextureSet textureSet, final MaterialTextureType textureType, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		final Table<String, String, SafeOptional<ResourceLocation>> rootTable = ResourceFinderImpl.innerTable(this.fluidTextureCache, textureSet, textureType);
		if (rootTable.contains(prefix, suffix)) {
			return rootTable.get(prefix, suffix);
		}
		final SafeOptional<ResourceLocation> resource = this.getBlockTextureCascaded(textureSet, textureType, prefix, suffix);
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
	public boolean isTextureValid(final ResourceLocation texture) {
		final ResourceLocation location = ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), "textures/%s.png".formatted(texture.getPath()));
		return this.isResourceValid(location);
	}

	private SafeOptional<ResourceLocation> getItemTextureCascaded(final MaterialTextureSet set, final MaterialTextureType type, @Nullable final String prefix, @Nullable final String suffix) {
		return this.getResourceCascaded("textures", "item", set, type, "png", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getItemModelCascaded(final MaterialTextureSet set, final MaterialTextureType type, @Nullable final String prefix, @Nullable final String suffix) {
		return this.getResourceCascaded("models", "item", set, type, "json", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getBlockTextureCascaded(final MaterialTextureSet set, final MaterialTextureType type, @Nullable final String prefix, @Nullable final String suffix) {
		return this.getResourceCascaded("textures", "block", set, type, "png", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getBlockModelCascaded(final MaterialTextureSet set, final MaterialTextureType type, @Nullable final String prefix, @Nullable final String suffix) {
		return this.getResourceCascaded("models", "block", set, type, "json", prefix, suffix);
	}

	private SafeOptional<ResourceLocation> getResourceCascaded(final String resourceType, final String pathPrepend, final MaterialTextureSet set, final MaterialTextureType type, @Nullable final String extension,
			@Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		final String prefix = pathPrefix == null || pathPrefix.isBlank() ? "" : pathPrefix + "_";
		final String suffix = pathSuffix == null || pathSuffix.isBlank() ? "" : "_" + pathSuffix;
		MaterialTextureSet currentSet = set;
		while (!currentSet.isRootSet()) {
			final ResourceLocation location = ResourceFinderImpl.getResourceUnchecked("%s/%s".formatted(resourceType, pathPrepend), currentSet, type, prefix, suffix + (extension != null ? "." + extension : ""));
			if (this.isResourceValid(location)) {
				break;
			}
			currentSet = currentSet.getParentSet();
		}
		final ResourceLocation location = ResourceFinderImpl.getResourceUnchecked(pathPrepend, currentSet, type, prefix, suffix);
		if (!this.isResourceValid(location)) {
			if (Config.debug_textureSetDebugLogging.get()) {
				Conductance.LOGGER.warn("Could not find cascaded resource {} while looking for: {}", location,
						ResourceFinderImpl.getResourceUnchecked("%s/%s".formatted(resourceType, pathPrepend), set, type, prefix, suffix + (extension != null ? "." + extension : "")));
			}
			return SafeOptional.ofFallback(location);
		}
		return SafeOptional.of(location);
	}

	private static ResourceLocation getResourceUnchecked(final String pathPrepend, final MaterialTextureSet set, final MaterialTextureType texType, final String prefix, final String suffix) {
		return ResourceLocation.fromNamespaceAndPath(texType.getRegistryKey().getNamespace(), "%s/material/%s/%s%s%s".formatted(pathPrepend, set.getRegistryKey(), prefix, texType.getRegistryKey().getPath(), suffix));
	}

	private static Table<String, String, SafeOptional<ResourceLocation>> innerTable(final Table<MaterialTextureSet, MaterialTextureType, Table<String, String, SafeOptional<ResourceLocation>>> parentTable,
			final MaterialTextureSet set, final MaterialTextureType type) {
		Table<String, String, SafeOptional<ResourceLocation>> rootTable = parentTable.get(set, type);
		if (rootTable == null) {
			rootTable = HashBasedTable.create();
			parentTable.put(set, type, rootTable);
		}
		return rootTable;
	}
}
