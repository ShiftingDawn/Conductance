package conductance.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import static conductance.api.block.BlockRotationHelper.FACING_ALL;
import static conductance.api.block.BlockRotationHelper.FACING_EXTENDED;
import static conductance.api.block.BlockRotationHelper.FACING_HORIZONTAL;
import static conductance.api.block.BlockRotationHelper.FACING_VERTICAL;

public final class GridInteractionHelper {

	public static final float QUADRANT_SIZE = 0.25F;

	private static boolean test(final float f) {
		return f < GridInteractionHelper.QUADRANT_SIZE || f > 1 - GridInteractionHelper.QUADRANT_SIZE;
	}

	public static Direction getLogicalSideFromGrid(final Direction side, final float x, final float y, final float z) {
		final Direction backSide = side.getOpposite();
		final Direction horizontalFallback = y < GridInteractionHelper.QUADRANT_SIZE ? Direction.DOWN : y > 1 - GridInteractionHelper.QUADRANT_SIZE ? Direction.UP : side;
		switch (side) {
			case DOWN, UP -> {
				if (x < GridInteractionHelper.QUADRANT_SIZE) {
					return GridInteractionHelper.test(z) ? backSide : Direction.WEST;
				} else if (x > 1 - GridInteractionHelper.QUADRANT_SIZE) {
					return GridInteractionHelper.test(z) ? backSide : Direction.EAST;
				} else {
					return z < GridInteractionHelper.QUADRANT_SIZE ? Direction.NORTH : z > 1 - GridInteractionHelper.QUADRANT_SIZE ? Direction.SOUTH : side;
				}
			}
			case NORTH, SOUTH -> {
				if (x < GridInteractionHelper.QUADRANT_SIZE) {
					return GridInteractionHelper.test(y) ? backSide : Direction.WEST;
				} else if (x > 1 - GridInteractionHelper.QUADRANT_SIZE) {
					return GridInteractionHelper.test(y) ? backSide : Direction.EAST;
				} else {
					return horizontalFallback;
				}
			}
			case EAST, WEST -> {
				if (z < GridInteractionHelper.QUADRANT_SIZE) {
					return GridInteractionHelper.test(y) ? backSide : Direction.NORTH;
				} else if (z > 1 - GridInteractionHelper.QUADRANT_SIZE) {
					return GridInteractionHelper.test(y) ? backSide : Direction.SOUTH;
				} else {
					return horizontalFallback;
				}
			}
		}
		return side;
	}

	public static Direction getLogicalSideFromGrid(final BlockHitResult hit) {
		final Vec3 position = hit.getLocation();
		return GridInteractionHelper.getLogicalSideFromGrid(hit.getDirection(), (float) position.x - hit.getBlockPos().getX(), (float) position.y - hit.getBlockPos().getY(),
			(float) position.z - hit.getBlockPos().getZ());
	}

	public static boolean shouldInteractUsingGrid(final UseOnContext ctx) {
		if (ctx.getPlayer() == null) {
			return false;
		}
		final BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
		final BlockEntity blockEntity = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (blockEntity != null) {
			if (ctx.getPlayer().isCrouching() && (blockState.getBlock() instanceof IGridInteractable || blockEntity instanceof IGridInteractable)) {
				return true;
			}
		}
		//Fall back to rotatable blocks
		if (InteractType.WRENCH.is(ctx.getItemInHand())) {
			final BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
			return state.hasProperty(FACING_ALL) || state.hasProperty(FACING_HORIZONTAL) || state.hasProperty(FACING_VERTICAL) || state.hasProperty(FACING_EXTENDED);
		}
		return false;
	}

	private GridInteractionHelper() {
	}
}
