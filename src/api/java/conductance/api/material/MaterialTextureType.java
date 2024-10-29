package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.registry.RegistryObject;
import conductance.api.util.SafeOptional;

public final class MaterialTextureType extends RegistryObject<ResourceLocation> {

	public MaterialTextureType(final ResourceLocation registryKey) {
		super(registryKey);
	}

	public SafeOptional<ResourceLocation> getItemTexture(final MaterialTextureSet textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getItemTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getItemModel(final MaterialTextureSet textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getItemModel(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getBlockTexture(final MaterialTextureSet textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getBlockTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getBlockModel(final MaterialTextureSet textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getBlockTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getFluidTexture(final MaterialTextureSet textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.RESOURCE_FINDER.getFluidTexture(textureSet, this, pathPrefix, pathSuffix);
	}
}
