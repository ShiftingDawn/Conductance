package conductance.api.block;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCBlockStateProperties;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.BlockStateModelPropsBuilder;
import conductance.api.resource.BlockStateVariantBuilder;
import conductance.api.util.IntPos;
import conductance.api.util.ModelUtils;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.EAST;
import static net.minecraft.core.Direction.NORTH;
import static net.minecraft.core.Direction.SOUTH;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.core.Direction.WEST;

public final class BlockRotationHelper {

	public static final ResourceLocation LOADER_ID = ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "extended_rotation");

	public static void addToBlockStateDefinition(final BlockRotationType rotationType, final StateDefinition.Builder<Block, BlockState> builder) {
		if (rotationType == BlockRotationType.NONE) {
			return;
		}
		builder.add(switch (rotationType) {
			case ALL -> NCBlockStateProperties.FACING_ALL;
			case HORIZONTAL -> NCBlockStateProperties.FACING_HORIZONTAL;
			case VERTICAL -> NCBlockStateProperties.FACING_VERTICAL;
			case EXTENDED -> NCBlockStateProperties.FACING_EXTENDED;
			default -> throw new AssertionError("Encountered unknown rotation type " + rotationType);
		});
	}

	public static BlockState addToDefaultState(final BlockRotationType type, final BlockState state) {
		return switch (type) {
			case ALL -> state.setValue(NCBlockStateProperties.FACING_ALL, Direction.NORTH);
			case HORIZONTAL -> state.setValue(NCBlockStateProperties.FACING_HORIZONTAL, Direction.NORTH);
			case VERTICAL -> state.setValue(NCBlockStateProperties.FACING_VERTICAL, Direction.UP);
			case EXTENDED -> state.setValue(NCBlockStateProperties.FACING_EXTENDED, FacingAndRotation.NORTH_UP);
			default -> state;
		};
	}

	public static BlockState setFacingOnPlacement(final BlockState state, final BlockPlaceContext ctx) {
		if (state.hasProperty(NCBlockStateProperties.FACING_HORIZONTAL)) {
			return state.setValue(NCBlockStateProperties.FACING_HORIZONTAL, ctx.getHorizontalDirection().getOpposite());
		}
		if (state.hasProperty(NCBlockStateProperties.FACING_VERTICAL)) {
			return state.setValue(NCBlockStateProperties.FACING_VERTICAL, ctx.getNearestLookingVerticalDirection().getOpposite());
		}
		if (state.hasProperty(NCBlockStateProperties.FACING_ALL)) {
			return state.setValue(NCBlockStateProperties.FACING_ALL, ctx.getNearestLookingDirection().getOpposite());
		}
		if (state.hasProperty(NCBlockStateProperties.FACING_EXTENDED)) {
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
			return state.setValue(NCBlockStateProperties.FACING_EXTENDED, FacingAndRotation.get(facing, rotation));
		}
		return state;
	}

	public static Direction getFacing(final BlockState state) {
		return BlockRotationHelper.getExtendedRotation(state).getFacing();
	}

	public static FacingAndRotation getExtendedRotation(final BlockState state) {
		if (state.hasProperty(NCBlockStateProperties.FACING_HORIZONTAL)) {
			return FacingAndRotation.get(state.getValue(NCBlockStateProperties.FACING_HORIZONTAL), Rotation.NONE);
		}
		if (state.hasProperty(NCBlockStateProperties.FACING_VERTICAL)) {
			return FacingAndRotation.get(state.getValue(NCBlockStateProperties.FACING_VERTICAL), Rotation.NONE);
		}
		if (state.hasProperty(NCBlockStateProperties.FACING_ALL)) {
			return FacingAndRotation.get(state.getValue(NCBlockStateProperties.FACING_ALL), Rotation.NONE);
		}
		if (state.hasProperty(NCBlockStateProperties.FACING_EXTENDED)) {
			return state.getValue(NCBlockStateProperties.FACING_EXTENDED);
		}
		return FacingAndRotation.get(NORTH, Rotation.NONE);
	}

	public static BlockState applyRotation(final BlockState state, final Rotation rotation) {
		for (final EnumProperty<Direction> prop : List.of(NCBlockStateProperties.FACING_ALL, NCBlockStateProperties.FACING_HORIZONTAL, NCBlockStateProperties.FACING_VERTICAL)) {
			if (state.hasProperty(prop)) {
				return state.setValue(prop, rotation.rotate(state.getValue(prop)));
			}
		}
		return state;
	}

	public static BlockState applyMirror(final BlockState state, final Mirror mirror) {
		for (final EnumProperty<Direction> prop : List.of(NCBlockStateProperties.FACING_ALL, NCBlockStateProperties.FACING_HORIZONTAL, NCBlockStateProperties.FACING_VERTICAL)) {
			if (state.hasProperty(prop)) {
				return state.setValue(prop, mirror.mirror(state.getValue(prop)));
			}
		}
		return state;
	}

	public static void handleBlockStateGeneration(
		final BlockStateBuilder builder, final BlockRotationType type, final Function<BlockStateVariantBuilder, BlockStateModelPropsBuilder> variantCallback,
		@Nullable final Consumer<BlockStateModelPropsBuilder> modelModifier
	) {
		if (type == BlockRotationType.NONE) {
			builder.simple(variant -> {
				final BlockStateModelPropsBuilder model = variantCallback.apply(variant);
				if (modelModifier != null) {
					modelModifier.accept(model);
				}
			});
			return;
		}
		final EnumProperty<Direction> prop = switch (type) {
			case ALL -> NCBlockStateProperties.FACING_ALL;
			case HORIZONTAL -> NCBlockStateProperties.FACING_HORIZONTAL;
			case VERTICAL -> NCBlockStateProperties.FACING_VERTICAL;
			default -> null;
		};
		if (prop != null) {
			builder.variants(variants -> {
				for (final Direction facing : prop.getPossibleValues()) {
					final IntPos rotation = ModelUtils.MODEL_ROTATION.get(facing);
					final BlockStateModelPropsBuilder model = variantCallback.apply(variants.variant(prop, facing)).x(rotation.x()).y(rotation.y());
					if (modelModifier != null) {
						modelModifier.accept(model);
					}
				}
			});
		} else if (type == BlockRotationType.EXTENDED) {
			builder.variants(variants -> {
				for (final FacingAndRotation facingAndRotation : FacingAndRotation.values()) {
					final IntPos rotationXY = ModelUtils.MODEL_ROTATION.get(facingAndRotation.getFacing());
					final BlockStateModelPropsBuilder model = variantCallback.apply(variants.variant(NCBlockStateProperties.FACING_EXTENDED, facingAndRotation))
						.addProperty("type", BlockRotationHelper.LOADER_ID)
						.x(rotationXY.x())
						.y(rotationXY.y())
						.addProperty("z", switch (facingAndRotation.getRotation()) {
							case NONE -> 0;
							case CLOCKWISE_90 -> 90;
							case CLOCKWISE_180 -> 180;
							case COUNTERCLOCKWISE_90 -> -90;
						});
					if (modelModifier != null) {
						modelModifier.accept(model);
					}
				}
			});
		}
	}

	public static void handleBlockStateGeneration(final BlockStateBuilder builder, final BlockRotationType type, final Function<BlockStateVariantBuilder, BlockStateModelPropsBuilder> variantCallback) {
		BlockRotationHelper.handleBlockStateGeneration(builder, type, variantCallback, null);
	}

	public static Direction getDirection(final Direction northFacing, final Direction dir) {
		return BlockRotationHelper.getExtendedDirection(northFacing, Rotation.NONE, dir);
	}

	public static Direction getExtendedDirection(final Direction northFacing, final Rotation rotation, final Direction dir) {
		return BlockRotationHelper.getExtendedDirection(FacingAndRotation.get(northFacing, rotation), dir);
	}

	public static Direction getExtendedDirection(final FacingAndRotation facingAndRotation, final Direction dir) {
		return switch (facingAndRotation) {
			case DOWN_UP -> switch (dir) {
				case DOWN -> SOUTH;
				case UP -> NORTH;
				case NORTH -> DOWN;
				case SOUTH -> UP;
				case WEST, EAST -> dir;
			};
			case DOWN_DOWN -> switch (dir) {
				case DOWN -> NORTH;
				case UP -> SOUTH;
				case NORTH -> DOWN;
				case SOUTH -> UP;
				case WEST -> EAST;
				case EAST -> WEST;
			};
			case DOWN_LEFT -> switch (dir) {
				case DOWN -> WEST;
				case UP -> EAST;
				case NORTH -> DOWN;
				case SOUTH -> UP;
				case WEST -> NORTH;
				case EAST -> SOUTH;
			};
			case DOWN_RIGHT -> switch (dir) {
				case DOWN -> EAST;
				case UP -> WEST;
				case NORTH -> DOWN;
				case SOUTH -> UP;
				case WEST -> SOUTH;
				case EAST -> NORTH;
			};
			case UP_UP -> switch (dir) {
				case DOWN -> NORTH;
				case UP -> SOUTH;
				case NORTH -> UP;
				case SOUTH -> DOWN;
				case WEST, EAST -> dir;
			};
			case UP_DOWN -> switch (dir) {
				case DOWN -> SOUTH;
				case UP -> NORTH;
				case NORTH -> UP;
				case SOUTH -> DOWN;
				case WEST -> EAST;
				case EAST -> WEST;
			};
			case UP_LEFT -> switch (dir) {
				case DOWN -> WEST;
				case UP -> EAST;
				case NORTH -> UP;
				case SOUTH -> DOWN;
				case WEST -> SOUTH;
				case EAST -> NORTH;
			};
			case UP_RIGHT -> switch (dir) {
				case DOWN -> WEST;
				case UP -> EAST;
				case NORTH -> UP;
				case SOUTH -> DOWN;
				case WEST -> NORTH;
				case EAST -> SOUTH;
			};
			case NORTH_UP -> dir;
			case NORTH_DOWN -> dir.getAxis() != Direction.Axis.Z ? dir.getOpposite() : dir;
			case NORTH_LEFT -> switch (dir) {
				case DOWN -> WEST;
				case UP -> EAST;
				case NORTH, SOUTH -> dir;
				case WEST -> UP;
				case EAST -> DOWN;
			};
			case NORTH_RIGHT -> switch (dir) {
				case DOWN -> EAST;
				case UP -> WEST;
				case NORTH, SOUTH -> dir;
				case WEST -> DOWN;
				case EAST -> UP;
			};
			case SOUTH_UP -> dir.getAxis() == Direction.Axis.Y ? dir : dir.getOpposite();
			case SOUTH_DOWN -> dir.getAxis() != Direction.Axis.X ? dir.getOpposite() : dir;
			case SOUTH_LEFT -> switch (dir) {
				case DOWN -> EAST;
				case UP -> WEST;
				case NORTH, SOUTH -> dir.getOpposite();
				case WEST -> UP;
				case EAST -> DOWN;
			};
			case SOUTH_RIGHT -> switch (dir) {
				case DOWN -> WEST;
				case UP -> EAST;
				case NORTH, SOUTH -> dir.getOpposite();
				case WEST -> DOWN;
				case EAST -> UP;
			};
			case WEST_UP -> switch (dir) {
				case DOWN, UP -> dir;
				case NORTH -> WEST;
				case SOUTH -> EAST;
				case WEST -> SOUTH;
				case EAST -> NORTH;
			};
			case WEST_DOWN -> switch (dir) {
				case DOWN, UP -> dir.getOpposite();
				case NORTH -> WEST;
				case SOUTH -> EAST;
				case WEST -> NORTH;
				case EAST -> SOUTH;
			};
			case WEST_LEFT -> switch (dir) {
				case DOWN -> SOUTH;
				case UP -> NORTH;
				case NORTH -> WEST;
				case SOUTH -> EAST;
				case WEST -> UP;
				case EAST -> DOWN;
			};
			case WEST_RIGHT -> switch (dir) {
				case DOWN -> NORTH;
				case UP -> SOUTH;
				case NORTH -> WEST;
				case SOUTH -> EAST;
				case WEST -> DOWN;
				case EAST -> UP;
			};
			case EAST_UP -> switch (dir) {
				case DOWN, UP -> dir;
				case NORTH -> EAST;
				case SOUTH -> WEST;
				case WEST -> NORTH;
				case EAST -> SOUTH;
			};
			case EAST_DOWN -> switch (dir) {
				case DOWN, UP -> dir.getOpposite();
				case NORTH -> EAST;
				case SOUTH -> WEST;
				case WEST -> SOUTH;
				case EAST -> NORTH;
			};
			case EAST_LEFT -> switch (dir) {
				case DOWN -> NORTH;
				case UP -> SOUTH;
				case NORTH -> EAST;
				case SOUTH -> WEST;
				case WEST -> UP;
				case EAST -> DOWN;
			};
			case EAST_RIGHT -> switch (dir) {
				case DOWN -> SOUTH;
				case UP -> NORTH;
				case NORTH -> EAST;
				case SOUTH -> WEST;
				case WEST -> DOWN;
				case EAST -> UP;
			};
		};
	}

	private BlockRotationHelper() {
	}
}
