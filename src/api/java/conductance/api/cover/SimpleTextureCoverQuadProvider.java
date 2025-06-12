package conductance.api.cover;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.model.ModelUtils;

@RequiredArgsConstructor
public final class SimpleTextureCoverQuadProvider implements CoverQuadProvider {

	private final ResourceLocation texture;

	@Override
	public List<BakedQuad> getCoverQuads(final Direction face, final RandomSource rand, @NotNull final CoverEntity<?> cover, @Nullable final Direction modelFacing, final ModelState modelState) {
		final TextureAtlasSprite sprite = ModelUtils.getSprite(this.texture);
		return List.of(ModelUtils.bakeFace(face, sprite, modelState, null));
	}
}
