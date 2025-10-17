package conductance.init.block;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import conductance.api.util.ModelUtils;
import conductance.api.util.RenderHelper;

public final class CreativeTankBlockEntityRenderer implements BlockEntityRenderer<CreativeTankBlockEntity> {

	public CreativeTankBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(
		final CreativeTankBlockEntity blockEntity, final float partialTick, final PoseStack poseStack, final MultiBufferSource bufferSource, final int packedLight, final int packedOverlay, final Vec3 cameraPos
	) {
		final FluidStack fluid = blockEntity.getFluid();
		if (fluid.isEmpty()) {
			return;
		}
		final IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid());
		final ResourceLocation texture = extensions.getStillTexture(fluid);
		final int color = ARGB.opaque(extensions.getTintColor(fluid));
		TextureAtlasSprite sprite = ModelUtils.getBlockSprite(texture);
		if (sprite == null) {
			sprite = ModelUtils.getBlockSprite(null);
		}
		poseStack.pushPose();
		final float minX = 0.0078125f;
		final float minY = 0.0078125f;
		final float minZ = 0.0078125f;
		final float maxX = 0.9921875f;
		final float maxY = 0.9921875f;
		final float maxZ = 0.9921875f;
		final VertexConsumer builder = bufferSource.getBuffer(Sheets.translucentItemSheet());
		RenderHelper.renderBlock(poseStack, builder, minX, minY, minZ, maxX, maxY, maxZ, color, 0xf000f0, sprite);
		poseStack.popPose();
	}
}
