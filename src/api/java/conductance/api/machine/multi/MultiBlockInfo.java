package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Rotation;

public record MultiBlockInfo(BlockAndTintGetter level, BlockPos controllerPos, Direction facing, Rotation rotation) {
}
