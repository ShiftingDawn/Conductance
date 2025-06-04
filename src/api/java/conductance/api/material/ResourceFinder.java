package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.SafeOptional;

public interface ResourceFinder {

	SafeOptional<ResourceLocation> getMaterialTexture(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getMaterialItemModel(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	SafeOptional<ResourceLocation> getMaterialBlockModel(ResourceLocation textureSet, MaterialTextureType textureType, @Nullable String pathPrefix, @Nullable String pathSuffix);

	@Nullable
	ResourceLocation getCustomMaterialTexture(Material material, MaterialTextureType type);

	default ResourceLocation getBlockTexture(final ResourceLocation location) {
		return location.withPrefix("block/");
	}

	default ResourceLocation getItemTexture(final ResourceLocation location) {
		return location.withPrefix("item/");
	}

	boolean isResourceValid(ResourceLocation resource);

	default boolean isTextureValid(final ResourceLocation texture) {
		return this.isResourceValid(texture.withPath("textures/%s.png"::formatted));
	}

	default boolean isBlockTextureValid(final ResourceLocation texture) {
		return this.isResourceValid(texture.withPath("textures/block/%s.png"::formatted));
	}

	default boolean isItemTextureValid(final ResourceLocation texture) {
		return this.isResourceValid(texture.withPath("textures/item/%s.png"::formatted));
	}
}
