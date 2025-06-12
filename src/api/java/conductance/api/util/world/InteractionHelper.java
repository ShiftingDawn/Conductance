package conductance.api.util.world;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import conductance.api.capability.CapabilityHelper;
import conductance.api.capability.cover.ICoverItem;
import conductance.api.capability.cover.ICoverable;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public final class InteractionHelper {

	public static final float QUADRANT_SIZE = 0.25F;

	private static boolean test(final float f) {
		return f < InteractionHelper.QUADRANT_SIZE || f > 1 - InteractionHelper.QUADRANT_SIZE;
	}

	public static Direction getLogicalSideFromGrid(final Direction side, final float x, final float y, final float z) {
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

	public static Direction getLogicalSideFromGrid(final BlockHitResult hit) {
		final Vec3 position = hit.getLocation();
		return InteractionHelper.getLogicalSideFromGrid(hit.getDirection(), (float) position.x - hit.getBlockPos().getX(), (float) position.y - hit.getBlockPos().getY(), (float) position.z - hit.getBlockPos().getZ());
	}

	public static boolean shouldInteractUsingGrid(final UseOnContext ctx) {
		if (ctx.getPlayer() == null) {
			return false;
		}
		final BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
		final BlockEntity blockEntity = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (blockEntity != null) {
			final ICoverable coverable = CapabilityHelper.getCoverable(ctx.getLevel(), ctx.getClickedPos());
			if (coverable != null && (ctx.getItemInHand().getItem() instanceof ICoverItem || ctx.getPlayer().isCrouching() || InteractType.HAMMER.is(ctx.getItemInHand()))) {
				return true;
			}
			if (ctx.getPlayer().isCrouching() && (blockState.getBlock() instanceof IExtendedInteractable || blockEntity instanceof IExtendedInteractable)) {
				return true;
			}
			//TODO pipelike
			//			if (ctx.getItemInHand().getItem() instanceof final PipeBlockItem pipeBlockItem && blockEntity instanceof final PipeBlockEntity<?, ?> pipeBlockEntity &&
			//					pipeBlockItem.getBlock().pipeType.type().equals(pipeBlockEntity.getPipeType().type())) {
			//				return true;
			//			}
		}
		//Fall back to rotatable blocks
		if (InteractType.WRENCH.is(ctx.getItemInHand())) {
			final BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
			return state.hasProperty(FACING) || state.hasProperty(HORIZONTAL_FACING) || state.hasProperty(RotationState.ALL.property);
		}
		return false;
	}

	private InteractionHelper() {
	}
}
