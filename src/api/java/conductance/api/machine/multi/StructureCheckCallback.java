package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface StructureCheckCallback {

	void onBlockChecked(BlockAndTintGetter level, BlockPos pos, BlockState state, StructureCheckContext ctx);
}
