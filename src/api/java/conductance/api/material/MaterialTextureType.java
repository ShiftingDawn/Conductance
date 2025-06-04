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

	public SafeOptional<ResourceLocation> getTexture(final ResourceLocation textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.resourceFinder().getMaterialTexture(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getItemModel(final ResourceLocation textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.resourceFinder().getMaterialItemModel(textureSet, this, pathPrefix, pathSuffix);
	}

	public SafeOptional<ResourceLocation> getBlockModel(final ResourceLocation textureSet, @Nullable final String pathPrefix, @Nullable final String pathSuffix) {
		return CAPI.resourceFinder().getMaterialBlockModel(textureSet, this, pathPrefix, pathSuffix);
	}
}
