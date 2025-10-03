package conductance.api.block;

import java.util.List;
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
import conductance.api.resource.BlockStateBuilder;
import conductance.api.util.IntPos;
import conductance.api.util.ModelUtils;

public final class BlockHelper {

	public static final EnumProperty<Direction> FACING_ALL = BlockStateProperties.FACING;
	public static final EnumProperty<Direction> FACING_HORIZONTAL = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<Direction> FACING_VERTICAL = BlockStateProperties.VERTICAL_DIRECTION;
	public static final EnumProperty<ExtendedDirection> FACING_EXTENDED = EnumProperty.create("facing", ExtendedDirection.class);

	public static void addRotationProperties(final RotationType type, final StateDefinition.Builder<Block, BlockState> builder) {
		if (type == RotationType.NONE) {
			return;
		}
		builder.add(switch (type) {
			case ALL -> BlockHelper.FACING_ALL;
			case HORIZONTAL -> BlockHelper.FACING_HORIZONTAL;
			case VERTICAL -> BlockHelper.FACING_VERTICAL;
			case ALL_DIRECTIONAL -> BlockHelper.FACING_EXTENDED;
			default -> throw new AssertionError("Encountered unknown rotation type " + type);
		});
	}

	public static BlockState setDefaultRotationValues(final RotationType type, final BlockState state) {
		return switch (type) {
			case ALL -> state.setValue(BlockHelper.FACING_ALL, Direction.NORTH);
			case HORIZONTAL -> state.setValue(BlockHelper.FACING_HORIZONTAL, Direction.NORTH);
			case VERTICAL -> state.setValue(BlockHelper.FACING_VERTICAL, Direction.UP);
			case ALL_DIRECTIONAL -> state.setValue(BlockHelper.FACING_EXTENDED, ExtendedDirection.NORTH_UP);
			default -> state;
		};
	}

	public static BlockState setFacingOnPlacement(final BlockState state, final BlockPlaceContext ctx) {
		if (state.hasProperty(BlockHelper.FACING_HORIZONTAL)) {
			return state.setValue(BlockHelper.FACING_HORIZONTAL, ctx.getHorizontalDirection().getOpposite());
		}
		if (state.hasProperty(BlockHelper.FACING_VERTICAL)) {
			return state.setValue(BlockHelper.FACING_VERTICAL, ctx.getNearestLookingVerticalDirection().getOpposite());
		}
		if (state.hasProperty(BlockHelper.FACING_ALL)) {
			return state.setValue(BlockHelper.FACING_ALL, ctx.getNearestLookingDirection().getOpposite());
		}
		return state;
	}

	public static BlockState applyRotation(final BlockState state, final Rotation rotation) {
		for (final EnumProperty<Direction> prop : List.of(BlockHelper.FACING_ALL, BlockHelper.FACING_HORIZONTAL, BlockHelper.FACING_VERTICAL)) {
			if (state.hasProperty(prop)) {
				return state.setValue(prop, rotation.rotate(state.getValue(prop)));
			}
		}
		return state;
	}

	public static BlockState applyMirror(final BlockState state, final Mirror mirror) {
		for (final EnumProperty<Direction> prop : List.of(BlockHelper.FACING_ALL, BlockHelper.FACING_HORIZONTAL, BlockHelper.FACING_VERTICAL)) {
			if (state.hasProperty(prop)) {
				return state.setValue(prop, mirror.mirror(state.getValue(prop)));
			}
		}
		return state;
	}

	public static void applyRotation(final BlockStateBuilder builder, final RotationType type, final ResourceLocation modelLocation) {
		if (type == RotationType.NONE) {
			builder.simple(variant -> variant.model(modelLocation));
			return;
		}
		final EnumProperty<Direction> prop = switch (type) {
			case ALL -> BlockHelper.FACING_ALL;
			case HORIZONTAL -> BlockHelper.FACING_HORIZONTAL;
			case VERTICAL -> BlockHelper.FACING_VERTICAL;
			//TODO extended rotation
			default -> null;
		};
		if (prop != null) {
			builder.variants(variants -> {
				for (final Direction facing : prop.getPossibleValues()) {
					final IntPos rotation = ModelUtils.MODEL_ROTATION.get(facing);
					variants.variant(prop, facing).model(modelLocation).x(rotation.x()).y(rotation.y());
				}
			});
		}
	}

	private BlockHelper() {
	}
}
