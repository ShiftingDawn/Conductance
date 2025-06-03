package conductance.runtimepack.client;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;

public final class ResourceHelper {

	@Nullable
	public static ResourceLocation getCustomMaterialTexture(final Material material, final MaterialTextureType type) {
		final ResourceLocation texturePath = material.getRegistryKey().withPath("material/custom/%s/%s".formatted(material.getRegistryKey().getPath(), type.getRegistryKey().getPath()));
		if (CAPI.resourceFinder().isResourceValid(texturePath.withPrefix("textures/").withSuffix(".png"))) {
			return texturePath;
		}
		return null;
	}

	@Nullable
	public static ResourceLocation getCustomItemTexture(final ResourceLocation itemId) {
		final ResourceLocation texturePath = itemId.withPrefix("item/");
		if (CAPI.resourceFinder().isResourceValid(texturePath.withPrefix("textures/").withSuffix(".png"))) {
			return texturePath;
		}
		return null;
	}

	private ResourceHelper() {
	}
}
