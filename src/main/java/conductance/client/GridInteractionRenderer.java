package conductance.client;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import conductance.api.util.world.InteractionHelper;
import conductance.Conductance;
import static conductance.api.util.world.InteractionHelper.QUADRANT_SIZE;

@EventBusSubscriber(value = Dist.CLIENT, modid = Conductance.MODID)
final class GridInteractionRenderer {

	private static final Vector3f DOWN_VECTOR = new Vector3f(0, -1, 0);

	@SubscribeEvent
	private static void onRenderBlockHighlight(final RenderHighlightEvent.Block event) {
		GridInteractionRenderer.renderBlockHighLight(event.getPoseStack(), event.getCamera(), event.getTarget(), event.getMultiBufferSource(), event.getDeltaTracker().getGameTimeDeltaTicks());
	}

	private static void renderBlockHighLight(final PoseStack poseStack, final Camera camera, final BlockHitResult target, final MultiBufferSource multiBufferSource, final float partialTick) {
		final Player player = Minecraft.getInstance().player;
		assert player != null;
		final Level level = player.getCommandSenderWorld();
		final ItemStack stack = player.getMainHandItem();

		if (!InteractionHelper.shouldInteractUsingGrid(new UseOnContext(level, player, player.getUsedItemHand(), stack, target))) {
			return;
		}

		final BlockPos blockPos = target.getBlockPos();
		final BlockState blockState = level.getBlockState(blockPos);
		final Vec3 cameraPos = camera.getPosition();
		final Direction face = target.getDirection();
		poseStack.pushPose();
		poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		final VertexConsumer builder = multiBufferSource.getBuffer(RenderType.lines());
		RenderSystem.lineWidth(3);

		final AABB blockBox = new AABB(blockPos);
		final Vector3f topRight = new Vector3f((float) blockBox.maxX, (float) blockBox.maxY, (float) blockBox.maxZ);
		final Vector3f bottomRight = new Vector3f((float) blockBox.maxX, (float) blockBox.minY, (float) blockBox.maxZ);
		final Vector3f bottomLeft = new Vector3f((float) blockBox.minX, (float) blockBox.minY, (float) blockBox.maxZ);
		final Vector3f topLeft = new Vector3f((float) blockBox.minX, (float) blockBox.maxY, (float) blockBox.maxZ);
		final Vector3f shift = new Vector3f(QUADRANT_SIZE, 0, 0);
		final Vector3f shiftVert = new Vector3f(0, QUADRANT_SIZE, 0);
		final Vector3f cubeCenter = blockBox.getCenter().toVector3f();
		topRight.sub(cubeCenter);
		bottomRight.sub(cubeCenter);
		bottomLeft.sub(cubeCenter);
		topLeft.sub(cubeCenter);
		switch (face) {
			case WEST -> {
				topRight.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				bottomRight.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				bottomLeft.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				topLeft.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				shift.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				shiftVert.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
			}
			case EAST -> {
				topRight.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				bottomRight.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				bottomLeft.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				topLeft.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				shift.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
				shiftVert.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, GridInteractionRenderer.DOWN_VECTOR));
			}
			case NORTH -> {
				topRight.rotate(new Quaternionf().rotateAxis(Mth.PI, GridInteractionRenderer.DOWN_VECTOR));
				bottomRight.rotate(new Quaternionf().rotateAxis(Mth.PI, GridInteractionRenderer.DOWN_VECTOR));
				bottomLeft.rotate(new Quaternionf().rotateAxis(Mth.PI, GridInteractionRenderer.DOWN_VECTOR));
				topLeft.rotate(new Quaternionf().rotateAxis(Mth.PI, GridInteractionRenderer.DOWN_VECTOR));
				shift.rotate(new Quaternionf().rotateAxis(Mth.PI, GridInteractionRenderer.DOWN_VECTOR));
				shiftVert.rotate(new Quaternionf().rotateAxis(Mth.PI, GridInteractionRenderer.DOWN_VECTOR));
			}
			case UP -> {
				final Vector3f side = new Vector3f(1, 0, 0);
				topRight.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, side));
				bottomRight.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, side));
				bottomLeft.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, side));
				topLeft.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, side));
				shift.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, side));
				shiftVert.rotate(new Quaternionf().rotateAxis(-Mth.HALF_PI, side));
			}
			case DOWN -> {
				final Vector3f side = new Vector3f(1, 0, 0);
				topRight.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, side));
				bottomRight.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, side));
				bottomLeft.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, side));
				topLeft.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, side));
				shift.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, side));
				shiftVert.rotate(new Quaternionf().rotateAxis(Mth.HALF_PI, side));
			}
		}
		topRight.add(cubeCenter);
		bottomRight.add(cubeCenter);
		bottomLeft.add(cubeCenter);
		topLeft.add(cubeCenter);

		final Matrix4f mat = poseStack.last().pose();
		GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topRight).add(new Vector3f(shift).mul(-1)), new Vector3f(bottomRight).add(new Vector3f(shift).mul(-1)));
		GridInteractionRenderer.drawLine(builder, mat, new Vector3f(bottomLeft).add(shift), new Vector3f(topLeft).add(shift));
		GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topLeft).add(new Vector3f(shiftVert).mul(-1)), new Vector3f(topRight).add(new Vector3f(shiftVert).mul(-1)));
		GridInteractionRenderer.drawLine(builder, mat, new Vector3f(bottomLeft).add(shiftVert), new Vector3f(bottomRight).add(shiftVert));

		final BlockEntity tile = level.getBlockEntity(blockPos);
		final Predicate<Direction> test = dir -> GridInteractionRenderer.shouldRenderCross(level, blockPos, blockState, tile, dir);
		final boolean front = GridInteractionRenderer.shouldRenderCross(level, blockPos, blockState, tile, face);
		final boolean back = GridInteractionRenderer.shouldRenderCross(level, blockPos, blockState, tile, face.getOpposite());
		final boolean left;
		final boolean right;
		final boolean up;
		final boolean down;
		if (face.getAxis().isVertical()) {
			if (face == Direction.UP) {
				right = test.test(Direction.WEST);
				left = test.test(Direction.EAST);
				up = test.test(Direction.NORTH);
				down = test.test(Direction.SOUTH);
			} else {
				right = test.test(Direction.EAST);
				left = test.test(Direction.WEST);
				up = test.test(Direction.SOUTH);
				down = test.test(Direction.NORTH);
			}
		} else {
			if (face == Direction.EAST || face == Direction.NORTH) {
				right = test.test(face.getCounterClockWise());
				left = test.test(face.getClockWise());
			} else {
				right = test.test(face.getClockWise());
				left = test.test(face.getCounterClockWise());
			}
			up = test.test(Direction.UP);
			down = test.test(Direction.DOWN);
		}
		if (back) {
			GridInteractionRenderer.drawLine(builder, mat, topLeft, new Vector3f(topLeft).add(shift).sub(shiftVert));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topLeft).sub(shiftVert), new Vector3f(topLeft).add(shift));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topRight).sub(shift), new Vector3f(topRight).sub(shiftVert));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topRight).sub(shift).sub(shiftVert), topRight);
			GridInteractionRenderer.drawLine(builder, mat, bottomLeft, new Vector3f(bottomLeft).add(shift).add(shiftVert));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(bottomLeft).add(shiftVert), new Vector3f(bottomLeft).add(shift));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(bottomRight).add(shiftVert).sub(shift), bottomRight);
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(bottomRight).sub(shift), new Vector3f(bottomRight).add(shiftVert));
		}
		if (front) {
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topLeft).add(shift).sub(shiftVert), new Vector3f(bottomRight).sub(shift).add(shiftVert));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topRight).sub(shift).sub(shiftVert), new Vector3f(bottomLeft).add(shift).add(shiftVert));
		}
		if (left) {
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topLeft).sub(shiftVert), new Vector3f(bottomLeft).add(shiftVert).add(shift));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topLeft).sub(shiftVert).add(shift), new Vector3f(bottomLeft).add(shiftVert));
		}
		if (right) {
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topRight).sub(shiftVert).sub(shift), new Vector3f(bottomRight).add(shiftVert));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topRight).sub(shiftVert), new Vector3f(bottomRight).add(shiftVert).sub(shift));
		}
		if (up) {
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topLeft).add(shift), new Vector3f(topRight).sub(shiftVert).sub(shift));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(topLeft).add(shift).sub(shiftVert), new Vector3f(topRight).sub(shift));
		}
		if (down) {
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(bottomLeft).add(shift).add(shiftVert), new Vector3f(bottomRight).sub(shift));
			GridInteractionRenderer.drawLine(builder, mat, new Vector3f(bottomLeft).add(shift), new Vector3f(bottomRight).add(shiftVert).sub(shift));
		}
		poseStack.popPose();
	}

	private static boolean shouldRenderCross(final Level level, final BlockPos pos, final BlockState state, @Nullable final BlockEntity tile, final Direction side) {
		//TODO pipe
		//		if (tile instanceof final IPipeNode<?, ?> pipeNode && pipeNode.isConnected(endSide)) {
		//			return true;
		//TODO covers
		//		} else if (tile != null) {
		//			final ICoverable coverable = CapabilityHelper.getCoverable(level, pos, null);
		//			if (coverable != null && coverable.getCoverManager().getCover(endSide).isPresent()) {
		//				return true;
		//			}
		//		}
		return false;
	}

	private static void drawLine(final VertexConsumer builder, final Matrix4f pose, final Vector3f from, final Vector3f to) {
		final float color = 0f;
		final float alpha = 0.4f;
		final Vector3f normal = new Vector3f(from).sub(to);
		builder.addVertex(pose, from.x, from.y, from.z).setColor(color, color, color, alpha).setNormal(normal.x, normal.y, normal.z);
		builder.addVertex(pose, to.x, to.y, to.z).setColor(color, color, color, alpha).setNormal(normal.x, normal.y, normal.z);
	}

	private GridInteractionRenderer() {
	}
}
