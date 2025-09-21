package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

public final class TestMachine extends MachineBlockEntity<TestMachine> {

	public TestMachine(final MachineType<TestMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
