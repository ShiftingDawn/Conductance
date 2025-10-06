package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MachineBlockEntity;

public class MultiMachineBlockEntity<T extends MultiMachineBlockEntity<T>> extends MachineBlockEntity<T> {

	public MultiMachineBlockEntity(final MultiMachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public MultiMachineType<T> getMachineType() {
		return (MultiMachineType<T>) super.getMachineType();
	}
}
