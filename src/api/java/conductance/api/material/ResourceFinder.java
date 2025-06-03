package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.SafeOptional;

public interface ResourceFinder {

	SafeOptional<ResourceLocation> getItemTexture(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getItemModel(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getBlockTexture(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getBlockModel(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getFluidTexture(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	boolean isResourceValid(ResourceLocation resource);

	boolean isTextureValid(ResourceLocation texture);
}
