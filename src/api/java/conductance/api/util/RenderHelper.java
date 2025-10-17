package conductance.api.util;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public final class RenderHelper {

	public static void renderBlock(
		final PoseStack poseStack, final VertexConsumer buffer, final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final int color, final int packedLight,
		final TextureAtlasSprite textureSprite
	) {
		final Matrix4f mat = poseStack.last().pose();
		final float uMin = textureSprite.getU0();
		final float uMax = textureSprite.getU1();
		final float vMin = textureSprite.getV0();
		final float vMax = textureSprite.getV1();

		buffer.addVertex(mat, minX, minY, minZ).setColor(color).setUv(uMin, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), -1, 0, 0);
		buffer.addVertex(mat, minX, minY, maxZ).setColor(color).setUv(uMax, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), -1, 0, 0);
		buffer.addVertex(mat, minX, maxY, maxZ).setColor(color).setUv(uMax, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), -1, 0, 0);
		buffer.addVertex(mat, minX, maxY, minZ).setColor(color).setUv(uMin, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), -1, 0, 0);
		buffer.addVertex(mat, maxX, minY, minZ).setColor(color).setUv(uMin, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 1, 0, 0);
		buffer.addVertex(mat, maxX, maxY, minZ).setColor(color).setUv(uMax, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 1, 0, 0);
		buffer.addVertex(mat, maxX, maxY, maxZ).setColor(color).setUv(uMax, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 1, 0, 0);
		buffer.addVertex(mat, maxX, minY, maxZ).setColor(color).setUv(uMin, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 1, 0, 0);
		buffer.addVertex(mat, minX, minY, minZ).setColor(color).setUv(uMin, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, -1, 0);
		buffer.addVertex(mat, maxX, minY, minZ).setColor(color).setUv(uMax, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, -1, 0);
		buffer.addVertex(mat, maxX, minY, maxZ).setColor(color).setUv(uMax, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, -1, 0);
		buffer.addVertex(mat, minX, minY, maxZ).setColor(color).setUv(uMin, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, -1, 0);
		buffer.addVertex(mat, minX, maxY, minZ).setColor(color).setUv(uMin, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
		buffer.addVertex(mat, minX, maxY, maxZ).setColor(color).setUv(uMax, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
		buffer.addVertex(mat, maxX, maxY, maxZ).setColor(color).setUv(uMax, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
		buffer.addVertex(mat, maxX, maxY, minZ).setColor(color).setUv(uMin, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
		buffer.addVertex(mat, minX, minY, minZ).setColor(color).setUv(uMin, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, -1);
		buffer.addVertex(mat, minX, maxY, minZ).setColor(color).setUv(uMax, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, -1);
		buffer.addVertex(mat, maxX, maxY, minZ).setColor(color).setUv(uMax, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, -1);
		buffer.addVertex(mat, maxX, minY, minZ).setColor(color).setUv(uMin, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, -1);
		buffer.addVertex(mat, minX, minY, maxZ).setColor(color).setUv(uMin, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, 1);
		buffer.addVertex(mat, maxX, minY, maxZ).setColor(color).setUv(uMax, vMax).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, 1);
		buffer.addVertex(mat, maxX, maxY, maxZ).setColor(color).setUv(uMax, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, 1);
		buffer.addVertex(mat, minX, maxY, maxZ).setColor(color).setUv(uMin, vMin).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(poseStack.last(), 0, 0, 1);
	}

	private RenderHelper() {
	}
}
