package conductance.api.material;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.SafeOptional;

public interface ResourceFinder {

	SafeOptional<ResourceLocation> getItemTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getItemTexture(MaterialTextureSet textureSet, MaterialTextureType textureType) {
		return getItemTexture(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getItemModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getItemModel(MaterialTextureSet textureSet, MaterialTextureType textureType) {
		return getItemModel(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getBlockTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getBlockTexture(MaterialTextureSet textureSet, MaterialTextureType textureType) {
		return getBlockTexture(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getBlockModel(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getBlockModel(MaterialTextureSet textureSet, MaterialTextureType textureType) {
		return getBlockModel(textureSet, textureType, null, null);
	}

	SafeOptional<ResourceLocation> getFluidTexture(MaterialTextureSet textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	default SafeOptional<ResourceLocation> getFluidTexture(MaterialTextureSet textureSet, MaterialTextureType textureType) {
		return getFluidTexture(textureSet, textureType, null, null);
	}

	boolean isResourceValid(final ResourceLocation resource);
}
