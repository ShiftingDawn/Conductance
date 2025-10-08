package conductance.api.machine.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.MultiMachineBlockEntity;
import conductance.api.machine.multi.MultiMachineType;

@FunctionalInterface
public interface MultiMachineBlockEntityFactory<T extends MultiMachineBlockEntity<T> & IMultiBlockController<T>> {

	T apply(MultiMachineType<T> machineType, BlockPos blockPos, BlockState blockState);
}
