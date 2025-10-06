package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

public record MultiBlockInfo(BlockAndTintGetter level, BlockPos controllerPos, Direction facing) {
}
