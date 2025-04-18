package conductance.core;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class InteractionHelper {

	public static final float QUADRANT_SIZE = 0.25F;

	private static boolean test(final float f) {
		return f < InteractionHelper.QUADRANT_SIZE || f > 1 - InteractionHelper.QUADRANT_SIZE;
	}

	public static Direction getInteractSide(final Direction side, final float x, final float y, final float z) {
		final Direction backSide = side.getOpposite();
		final Direction horizontalFallback = y < InteractionHelper.QUADRANT_SIZE ? Direction.DOWN : y > 1 - InteractionHelper.QUADRANT_SIZE ? Direction.UP : side;
		switch (side) {
			case DOWN, UP -> {
				if (x < InteractionHelper.QUADRANT_SIZE) {
					return InteractionHelper.test(z) ? backSide : Direction.WEST;
				} else if (x > 1 - InteractionHelper.QUADRANT_SIZE) {
					return InteractionHelper.test(z) ? backSide : Direction.EAST;
				} else {
					return z < InteractionHelper.QUADRANT_SIZE ? Direction.NORTH : z > 1 - InteractionHelper.QUADRANT_SIZE ? Direction.SOUTH : side;
				}
			}
			case NORTH, SOUTH -> {
				if (x < InteractionHelper.QUADRANT_SIZE) {
					return InteractionHelper.test(y) ? backSide : Direction.WEST;
				} else if (x > 1 - InteractionHelper.QUADRANT_SIZE) {
					return InteractionHelper.test(y) ? backSide : Direction.EAST;
				} else {
					return horizontalFallback;
				}
			}
			case EAST, WEST -> {
				if (z < InteractionHelper.QUADRANT_SIZE) {
					return InteractionHelper.test(y) ? backSide : Direction.NORTH;
				} else if (z > 1 - InteractionHelper.QUADRANT_SIZE) {
					return InteractionHelper.test(y) ? backSide : Direction.SOUTH;
				} else {
					return horizontalFallback;
				}
			}
		}
		return side;
	}

	public static Direction getInteractSide(final BlockHitResult hit) {
		final Vec3 position = hit.getLocation();
		return InteractionHelper.getInteractSide(hit.getDirection(), (float) position.x - hit.getBlockPos().getX(), (float) position.y - hit.getBlockPos().getY(), (float) position.z - hit.getBlockPos().getZ());
	}

	private InteractionHelper() {
	}
}
