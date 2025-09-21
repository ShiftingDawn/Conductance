package conductance.api.machine.event;

import net.minecraft.world.level.block.state.BlockBehaviour;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

@FunctionalInterface
public interface MachineBlockFactory<T extends MachineBlockEntity<T>> {

	MachineBlock<T> apply(BlockBehaviour.Properties properties, MachineType<T> machineType);
}
