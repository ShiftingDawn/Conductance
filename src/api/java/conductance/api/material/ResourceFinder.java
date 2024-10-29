package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.SafeOptional;

public interface ResourceFinder {

	SafeOptional<ResourceLocation> getItemTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getItemTexture(final MaterialTextureSet textureSet, final MaterialTextureType textureType) {
		return this.getItemTexture(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getItemTexture(Material material, MaterialTextureType type, @Nullable String fallbackPathPrefix, @Nullable String fallbackPathSuffix);

	default SafeOptional<ResourceLocation> getItemTexture(final Material material, final MaterialTextureType type) {
		return this.getItemTexture(material, type, null, null);
	}

	SafeOptional<ResourceLocation> getItemModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getItemModel(final MaterialTextureSet textureSet, final MaterialTextureType textureType) {
		return this.getItemModel(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getBlockTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getBlockTexture(final MaterialTextureSet textureSet, final MaterialTextureType textureType) {
		return this.getBlockTexture(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getBlockTexture(Material material, MaterialTextureType type, @Nullable String fallbackPathPrefix, @Nullable String fallbackPathSuffix);

	default SafeOptional<ResourceLocation> getBlockTexture(final Material material, final MaterialTextureType type) {
		return this.getBlockTexture(material, type, null, null);
	}

	SafeOptional<ResourceLocation> getBlockModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getBlockModel(final MaterialTextureSet textureSet, final MaterialTextureType textureType) {
		return this.getBlockModel(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getFluidTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getFluidTexture(final MaterialTextureSet textureSet, final MaterialTextureType textureType) {
		return this.getFluidTexture(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getFluidTexture(Material material, MaterialTextureType type, @Nullable String fallbackPathPrefix, @Nullable String fallbackPathSuffix);

	default SafeOptional<ResourceLocation> getFluidTexture(final Material material, final MaterialTextureType type) {
		return this.getFluidTexture(material, type, null, null);
	}

	boolean isResourceValid(final ResourceLocation resource);

	boolean isTextureValid(final ResourceLocation texture);
}
