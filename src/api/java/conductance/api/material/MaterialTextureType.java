package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.registry.RegistryObject;
import conductance.api.util.SafeOptional;

public final class MaterialTextureType extends RegistryObject<ResourceLocation> {

	public MaterialTextureType(ResourceLocation registryKey) {
		super(registryKey);
	}

	public SafeOptional<ResourceLocation> getItemTexture(MaterialTextureSet textureSet, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getItemTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getItemTexture(MaterialTextureSet textureSet) {
		return CAPI.RESOURCE_FINDER.getItemTexture(textureSet, this, null, null);
	}

	public SafeOptional<ResourceLocation> getItemModel(MaterialTextureSet textureSet, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getItemModel(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getItemModel(MaterialTextureSet textureSet) {
		return CAPI.RESOURCE_FINDER.getItemModel(textureSet, this, null, null);
	}

	public SafeOptional<ResourceLocation> getBlockTexture(MaterialTextureSet textureSet, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getBlockTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getBlockTexture(MaterialTextureSet textureSet) {
		return CAPI.RESOURCE_FINDER.getBlockTexture(textureSet, this, null, null);
	}

	public SafeOptional<ResourceLocation> getBlockModel(MaterialTextureSet textureSet, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getBlockTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getBlockModel(MaterialTextureSet textureSet) {
		return CAPI.RESOURCE_FINDER.getBlockModel(textureSet, this, null, null);
	}

	public SafeOptional<ResourceLocation> getFluidTexture(MaterialTextureSet textureSet, @Nullable String pathPrefix, @Nullable String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getFluidTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getFluidTexture(MaterialTextureSet textureSet, MaterialTextureType textureType) {
		return CAPI.RESOURCE_FINDER.getFluidTexture(textureSet, this, null, null);
	}
}
