package conductance.api.machine.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

@FunctionalInterface
public interface MachineBlockEntityFactory<T extends MachineBlockEntity<T>> {

	T apply(MachineType<T> machineType, BlockPos blockPos, BlockState blockState);
}
