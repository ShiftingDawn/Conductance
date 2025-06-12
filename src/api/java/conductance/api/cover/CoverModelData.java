package conductance.api.cover;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import conductance.api.resource.model.FaceQuadBuilder;
import conductance.api.resource.model.ModelUtils;

@OnlyIn(Dist.CLIENT)
public final class CoverModelData {

	public static final ModelProperty<CoverManager> MODEL_PROPERTY = new ModelProperty<>();

	public static List<BakedQuad> getCoverQuads(final CoverManager coverManager, final Direction side, final RandomSource rand, final Direction frontFacing) {
		final CoverEntity<?> coverEntity = coverManager.getCover(side).orElse(null);
		if (coverEntity == null) {
			return List.of();
		}
		final ModelState rotationState = ModelUtils.getModelRotationState(frontFacing);
		final CoverQuadProvider coverRenderer = coverEntity.getCoverType().getRenderer().get();
		final ArrayList<BakedQuad> quads = new ArrayList<>(coverRenderer.getCoverQuads(side, rand, coverEntity, frontFacing, rotationState));
		quads.addAll(CoverModelData.getCoverBackplateQuads(coverManager, coverEntity, side));
		return quads;
	}

	public static List<BakedQuad> getCoverBackplateQuads(final CoverManager manager, final CoverEntity<?> cover, final Direction side) {
		return Util.make(new ArrayList<>(), quads -> {
			final double thickness = manager.getCoverBackplateThickness();
			if (thickness > 0 && cover.shouldRenderCoverBackplate()) {
				for (final Direction face : Direction.values()) {
					//Don't render the back side
					if (face != side.getOpposite()) {
						final AABB cube = CoverModelData.calcBox(face, thickness);
						final TextureAtlasSprite sprite = ModelUtils.getSprite(ResourceLocation.withDefaultNamespace("block/iron_block"));
						quads.add(ModelUtils.bakeFace(cube, side, sprite, BlockModelRotation.X0_Y0, FaceQuadBuilder::uvCalc));
					}
				}
			}
		});
	}

	private static AABB calcBox(final Direction face, final double thickness) {
		final double max = 1d - thickness;
		return new AABB(
				face.getStepX() == 0 ? 0.001 : face.getStepX() > 0 ? max : 0.001,
				face.getStepY() == 0 ? 0.001 : face.getStepY() > 0 ? max : 0.001,
				face.getStepZ() == 0 ? 0.001 : face.getStepZ() > 0 ? max : 0.001,
				face.getStepX() == 0 ? 0.999 : face.getStepX() > 0 ? 0.999 : thickness,
				face.getStepY() == 0 ? 0.999 : face.getStepY() > 0 ? 0.999 : thickness,
				face.getStepZ() == 0 ? 0.999 : face.getStepZ() > 0 ? 0.999 : thickness
		);
	}

	private CoverModelData() {
	}
}
