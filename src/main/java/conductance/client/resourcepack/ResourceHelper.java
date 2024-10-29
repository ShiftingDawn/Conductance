package conductance.client.resourcepack;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;

public final class ResourceHelper {

	@Nullable
	public static ResourceLocation getCustomItemTexture(final Material material, final MaterialTextureType type) {
		final ResourceLocation texturePath = material.getRegistryKey().withPath("item/material_custom/%s/%s".formatted(material.getRegistryKey().getPath(), type.getRegistryKey().getPath()));
		if (CAPI.resourceFinder().isResourceValid(texturePath.withPrefix("textures/").withSuffix(".png"))) {
			return texturePath;
		}
		return null;
	}

	@Nullable
	public static ResourceLocation getCustomBlockTexture(final Material material, final MaterialTextureType type) {
		final ResourceLocation texturePath = material.getRegistryKey().withPath("block/material_custom/%s/%s".formatted(material.getRegistryKey().getPath(), type.getRegistryKey().getPath()));
		if (CAPI.resourceFinder().isResourceValid(texturePath.withPrefix("textures/").withSuffix(".png"))) {
			return texturePath;
		}
		return null;
	}

	@Nullable
	public static ResourceLocation getCustomFluidTexture(final Material material, final MaterialTextureType type) {
		final ResourceLocation texturePath = material.getRegistryKey().withPath("block/material_custom/%s/%s".formatted(material.getRegistryKey().getPath(), type.getRegistryKey().getPath()));
		if (CAPI.resourceFinder().isResourceValid(texturePath.withPrefix("textures/").withSuffix(".png"))) {
			return texturePath;
		}
		return null;
	}

	private ResourceHelper() {
	}
}
