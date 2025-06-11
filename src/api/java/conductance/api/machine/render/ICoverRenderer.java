package conductance.api.machine.render;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.capability.CapabilityHelper;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.ICoverable;

public interface ICoverRenderer extends IRenderer {

	@Override
	@OnlyIn(Dist.CLIENT)
	default List<BakedQuad> renderModel(
			@Nullable final BlockAndTintGetter level, @Nullable final BlockPos pos, @Nullable final BlockState state, @Nullable final Direction side, final RandomSource rand, final ModelData data,
			@Nullable final RenderType renderType) {
		final BlockEntity blockEntity = level == null ? null : level.getBlockEntity(pos);
		if (blockEntity != null) {
			final ICoverable coverable = CapabilityHelper.getCoverable(blockEntity.getLevel(), pos);
			if (coverable != null) {
				final List<BakedQuad> quads = new LinkedList<>();
				final ModelState modelState = ModelFactory.getRotation(coverable.getFrontFacing());
				final Direction modelFacing = side == null ? null : ModelFactory.modelFacing(side, coverable.getFrontFacing());
				this.renderCovers(quads, side, rand, coverable, modelFacing, modelState);
				return quads;
			}
		}
		return IRenderer.super.renderModel(level, pos, state, side, rand, data, renderType);
	}

	@OnlyIn(Dist.CLIENT)
	default void renderCovers(
			final List<BakedQuad> quads, @Nullable final Direction side, final RandomSource rand, @Nonnull final ICoverable coverable, @Nullable final Direction modelFacing, final ModelState modelState
	) {
		if (side == null) {
			return;
		}
		final CoverEntity<?> cover = coverable.getCoverManager().getCover(side).orElse(null);
		if (cover == null) {
			return;
		}
		final double thickness = coverable.getCoverManager().getCoverBackplateThickness();
		if (thickness > 0 && cover.shouldRenderCoverBackplate()) {
			for (final Direction face : Direction.values()) {
				if (side != face.getOpposite()) {
					final double max = 1d - thickness;
					final Vec3i normal = face.getNormal();
					final AABB cube = new AABB(
							normal.getX() == 0 ? 0.001 : normal.getX() > 0 ? max : 0.001,
							normal.getY() == 0 ? 0.001 : normal.getY() > 0 ? max : 0.001,
							normal.getZ() == 0 ? 0.001 : normal.getZ() > 0 ? max : 0.001,
							normal.getX() == 0 ? 0.999 : normal.getX() > 0 ? 0.999 : thickness,
							normal.getY() == 0 ? 0.999 : normal.getY() > 0 ? 0.999 : thickness,
							normal.getZ() == 0 ? 0.999 : normal.getZ() > 0 ? 0.999 : thickness);
					quads.add(FaceQuad.builder(side, ModelFactory.getBlockSprite(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "block/materialsets/dull/wire"))).cube(cube).cubeUV().tintIndex(-1).bake());
				}
			}
		}
		if (modelFacing != null && cover.getSide() == side) {
//			cover.getCoverType().getRenderer().renderCover(quads, side, rand, cover, modelFacing, modelState);
		}
	}
}
