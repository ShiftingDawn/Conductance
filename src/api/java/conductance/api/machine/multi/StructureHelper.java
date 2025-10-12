package conductance.api.machine.multi;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.CAPI;
import conductance.api.NCBlockStateProperties;
import static conductance.api.block.BlockRotationHelper.getExtendedDirection;

public final class StructureHelper {

	public static BlockPos getStructureCheckStartPos(final BlockPos controllerPos, final MultiBlockStructure structure, final Direction controllerFacing, final Rotation controllerRotation) {
		return controllerPos
			.relative(getExtendedDirection(controllerFacing, controllerRotation, Direction.NORTH), structure.zOffset())
			.relative(getExtendedDirection(controllerFacing, controllerRotation, Direction.DOWN), structure.yOffset())
			.relative(getExtendedDirection(controllerFacing, controllerRotation, Direction.EAST), structure.xOffset());
	}

	public static BlockPos getStructureCheckStartEndPos(final BlockPos structureCheckStartPos, final Direction controllerFacing, final Rotation controllerRotation, final int x, final int y, final int z) {
		return structureCheckStartPos
			.relative(getExtendedDirection(controllerFacing, controllerRotation, Direction.SOUTH), z)
			.relative(getExtendedDirection(controllerFacing, controllerRotation, Direction.UP), y)
			.relative(getExtendedDirection(controllerFacing, controllerRotation, Direction.WEST), x);
	}

	public static boolean checkStructure(
		final BlockAndTintGetter level, final BlockPos controllerPos, final Direction facing, final Rotation rotation, final MultiBlockStructure structure, final StructureCheckContext ctx
	) {
		final BlockPos startPos = StructureHelper.getStructureCheckStartPos(controllerPos, structure, facing, rotation);
		for (int x = 0; x < structure.expectedStates().length; ++x) {
			final StructurePredicate[][] slicePredicates = structure.expectedStates()[x];
			for (int y = 0; y < slicePredicates.length; ++y) {
				final StructurePredicate[] layerPredicates = slicePredicates[y];
				for (int z = 0; z < layerPredicates.length; ++z) {
					final StructurePredicate predicate = layerPredicates[z];
					final StructureCheckCallback callback = structure.callbacks()[x][y][z];
					final BlockPos currentPos = StructureHelper.getStructureCheckStartEndPos(startPos, facing, rotation, x, y, z);
					final BlockState currentState = level.getBlockState(currentPos);
					if (!predicate.test(level, currentPos, currentState, ctx)) {
						return false;
					} else {
						if (currentPos != controllerPos) {
							if (currentState.hasProperty(NCBlockStateProperties.ACTIVE)) {
								ctx.get(StructureCheckContext.ACTIVE_BLOCKS).add(currentPos);
							}
							CAPI.make(level.getBlockEntity(currentPos), blockEntity -> {
								if (blockEntity instanceof final IMultiBlockPart part) {
									ctx.get(StructureCheckContext.PARTS).add(part);
								}
							});
						}
						if (callback != null) {
							callback.onBlockChecked(level, currentPos, currentState, ctx);
						}
						structure.globalCallbacks().forEach(globalCallback -> {
							globalCallback.onBlockChecked(level, currentPos, currentState, ctx);
						});
					}
				}
			}
		}
		final Set<StructurePredicate> allPredicates = new HashSet<>();
		for (final StructurePredicate predicate : structure.predicates()) {
			StructureHelper.collectPredicates(allPredicates, predicate);
		}
		for (final StructurePredicate predicate : allPredicates) {
			final int matchCount = ctx.get(StructureCheckContext.MATCH_COUNT).getOrDefault(predicate, 0);
			if (predicate.getMinMatches() != -1 && matchCount < predicate.getMinMatches()) {
				return false;
			}
			if (predicate.getMaxMatches() != -1 && matchCount > predicate.getMaxMatches()) {
				return false;
			}
		}
		return true;
	}

	private static void collectPredicates(final Set<StructurePredicate> set, final StructurePredicate predicate) {
		set.add(predicate);
		for (final StructurePredicate child : predicate.getChildren()) {
			StructureHelper.collectPredicates(set, child);
		}
	}

	private StructureHelper() {
	}
}
