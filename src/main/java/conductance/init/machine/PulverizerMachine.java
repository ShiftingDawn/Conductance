package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

public final class PulverizerMachine extends MachineBlockEntity<PulverizerMachine> {

	public PulverizerMachine(final MachineType<PulverizerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
