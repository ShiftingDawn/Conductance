package conductance.api.util.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import static conductance.api.block.BlockRotationHelper.getDirection;

public final class StructureHelper {

	public static BlockPos getStructureCheckStartPos(final BlockPos controllerPos, final MultiBlockStructure structure, final Direction controllerFacing) {
		return controllerPos
			.relative(getDirection(controllerFacing, Direction.NORTH), structure.zOffset())
			.relative(getDirection(controllerFacing, Direction.DOWN), structure.yOffset())
			.relative(getDirection(controllerFacing, Direction.EAST), structure.xOffset());
	}

	public static BlockPos getStructureCheckStartEndPos(final BlockPos structureCheckStartPos, final Direction controllerFacing, final int x, final int y, final int z) {
		return structureCheckStartPos
			.relative(getDirection(controllerFacing, Direction.SOUTH), z)
			.relative(getDirection(controllerFacing, Direction.UP), y)
			.relative(getDirection(controllerFacing, Direction.WEST), x);
	}

	private StructureHelper() {
	}
}
