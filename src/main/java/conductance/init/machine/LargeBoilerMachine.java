package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;
import conductance.api.machine.multi.MultiMachineType;

public class LargeBoilerMachine extends MultiControllerMachineBlockEntity<LargeBoilerMachine> {

	public LargeBoilerMachine(final MultiMachineType<LargeBoilerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
