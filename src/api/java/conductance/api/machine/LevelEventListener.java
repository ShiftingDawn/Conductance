package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface LevelEventListener {

	void onNeighborChanged(BlockPos neighborPos, BlockState neighborState, Direction neighborSide);
}
