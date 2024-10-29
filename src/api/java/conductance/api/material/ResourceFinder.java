package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.SafeOptional;

public interface ResourceFinder {

	SafeOptional<ResourceLocation> getItemTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getItemModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getBlockTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getBlockModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getFluidTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	boolean isResourceValid(ResourceLocation resource);

	boolean isTextureValid(ResourceLocation texture);
}
