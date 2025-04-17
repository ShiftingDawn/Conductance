package conductance.api.machine.render;

import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record TextureOverrider(Map<String, ResourceLocation> overrides) implements Function<Material, TextureAtlasSprite> {

	@Override
	public TextureAtlasSprite apply(final Material material) {
		return material.sprite();
	}
}
