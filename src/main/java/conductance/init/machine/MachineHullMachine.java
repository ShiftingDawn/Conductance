package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

public class MachineHullMachine extends MachineBlockEntity<MachineHullMachine> {

	public MachineHullMachine(final MachineType<MachineHullMachine> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType, pos, blockState);
	}
}
