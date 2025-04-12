package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface MachineBlockEntityFactory<T extends MachineBlockEntity<T>> {

	T newInstance(MachineType<T> type, BlockPos pos, BlockState blockState);
}
