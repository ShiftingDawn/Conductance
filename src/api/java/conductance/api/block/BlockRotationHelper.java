package conductance.api.block;

import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import conductance.api.CAPI;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.BlockStateModelPropsBuilder;
import conductance.api.resource.BlockStateVariantBuilder;
import conductance.api.util.IntPos;
import conductance.api.util.ModelUtils;

public final class BlockRotationHelper {

	public static final ResourceLocation LOADER_ID = ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "extended_rotation");
	public static final EnumProperty<Direction> FACING_ALL = BlockStateProperties.FACING;
	public static final EnumProperty<Direction> FACING_HORIZONTAL = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<Direction> FACING_VERTICAL = BlockStateProperties.VERTICAL_DIRECTION;
	public static final EnumProperty<FacingAndRotation> FACING_EXTENDED = EnumProperty.create("facing", FacingAndRotation.class);

	public static void addToBlockStateDefinition(final BlockRotationType rotationType, final StateDefinition.Builder<Block, BlockState> builder) {
		if (rotationType == BlockRotationType.NONE) {
			return;
		}
		builder.add(switch (rotationType) {
			case ALL -> BlockRotationHelper.FACING_ALL;
			case HORIZONTAL -> BlockRotationHelper.FACING_HORIZONTAL;
			case VERTICAL -> BlockRotationHelper.FACING_VERTICAL;
			case EXTENDED -> BlockRotationHelper.FACING_EXTENDED;
			default -> throw new AssertionError("Encountered unknown rotation type " + rotationType);
		});
	}

	public static BlockState addToDefaultState(final BlockRotationType type, final BlockState state) {
		return switch (type) {
			case ALL -> state.setValue(BlockRotationHelper.FACING_ALL, Direction.NORTH);
			case HORIZONTAL -> state.setValue(BlockRotationHelper.FACING_HORIZONTAL, Direction.NORTH);
			case VERTICAL -> state.setValue(BlockRotationHelper.FACING_VERTICAL, Direction.UP);
			case EXTENDED -> state.setValue(BlockRotationHelper.FACING_EXTENDED, FacingAndRotation.NORTH_UP);
			default -> state;
		};
	}

	public static BlockState setFacingOnPlacement(final BlockState state, final BlockPlaceContext ctx) {
		if (state.hasProperty(BlockRotationHelper.FACING_HORIZONTAL)) {
			return state.setValue(BlockRotationHelper.FACING_HORIZONTAL, ctx.getHorizontalDirection().getOpposite());
		}
		if (state.hasProperty(BlockRotationHelper.FACING_VERTICAL)) {
			return state.setValue(BlockRotationHelper.FACING_VERTICAL, ctx.getNearestLookingVerticalDirection().getOpposite());
		}
		if (state.hasProperty(BlockRotationHelper.FACING_ALL)) {
			return state.setValue(BlockRotationHelper.FACING_ALL, ctx.getNearestLookingDirection().getOpposite());
		}
		if (state.hasProperty(BlockRotationHelper.FACING_EXTENDED)) {
			final Direction facing = ctx.getNearestLookingDirection().getOpposite();
			Rotation rotation = Rotation.NONE;
			if (facing.getAxis().isVertical()) {
				rotation = switch (ctx.getHorizontalDirection()) {
					case NORTH -> facing == Direction.DOWN ? Rotation.NONE : Rotation.CLOCKWISE_180;
					case EAST -> Rotation.CLOCKWISE_90;
					case SOUTH -> facing == Direction.DOWN ? Rotation.CLOCKWISE_180 : Rotation.NONE;
					case WEST -> Rotation.COUNTERCLOCKWISE_90;
					default -> throw new AssertionError();
				};
			}
			return state.setValue(BlockRotationHelper.FACING_EXTENDED, FacingAndRotation.get(facing, rotation));
		}
		return state;
	}

	public static BlockState applyRotation(final BlockState state, final Rotation rotation) {
		for (final EnumProperty<Direction> prop : List.of(BlockRotationHelper.FACING_ALL, BlockRotationHelper.FACING_HORIZONTAL, BlockRotationHelper.FACING_VERTICAL)) {
			if (state.hasProperty(prop)) {
				return state.setValue(prop, rotation.rotate(state.getValue(prop)));
			}
		}
		return state;
	}

	public static BlockState applyMirror(final BlockState state, final Mirror mirror) {
		for (final EnumProperty<Direction> prop : List.of(BlockRotationHelper.FACING_ALL, BlockRotationHelper.FACING_HORIZONTAL, BlockRotationHelper.FACING_VERTICAL)) {
			if (state.hasProperty(prop)) {
				return state.setValue(prop, mirror.mirror(state.getValue(prop)));
			}
		}
		return state;
	}

	public static void handleBlockStateGeneration(final BlockStateBuilder builder, final BlockRotationType type, final Function<BlockStateVariantBuilder, BlockStateModelPropsBuilder> variantCallback) {
		if (type == BlockRotationType.NONE) {
			builder.simple(variantCallback::apply);
			return;
		}
		final EnumProperty<Direction> prop = switch (type) {
			case ALL -> BlockRotationHelper.FACING_ALL;
			case HORIZONTAL -> BlockRotationHelper.FACING_HORIZONTAL;
			case VERTICAL -> BlockRotationHelper.FACING_VERTICAL;
			default -> null;
		};
		if (prop != null) {
			builder.variants(variants -> {
				for (final Direction facing : prop.getPossibleValues()) {
					final IntPos rotation = ModelUtils.MODEL_ROTATION.get(facing);
					variantCallback.apply(variants.variant(prop, facing)).x(rotation.x()).y(rotation.y());
				}
			});
		} else if (type == BlockRotationType.EXTENDED) {
			builder.variants(variants -> {
				for (final FacingAndRotation facingAndRotation : FacingAndRotation.values()) {
					final IntPos rotationXY = ModelUtils.MODEL_ROTATION.get(facingAndRotation.getFacing());
					variantCallback.apply(variants.variant(BlockRotationHelper.FACING_EXTENDED, facingAndRotation))
						.addProperty("type", BlockRotationHelper.LOADER_ID)
						.x(rotationXY.x())
						.y(rotationXY.y())
						.addProperty("z", switch (facingAndRotation.getRotation()) {
							case NONE -> 0;
							case CLOCKWISE_90 -> 90;
							case CLOCKWISE_180 -> 180;
							case COUNTERCLOCKWISE_90 -> -90;
						});
				}
			});
		}
	}

	private BlockRotationHelper() {
	}
}
