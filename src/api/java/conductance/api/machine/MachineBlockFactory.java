package conductance.api.machine;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface MachineBlockFactory<T extends MachineBlockEntity<T>> {

	MachineBlock<T> newInstance(BlockBehaviour.Properties properties, MachineType<T> machineType);
}
