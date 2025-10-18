package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.api.IWorkable;

public class WorkableMachineBlockEntity<T extends WorkableMachineBlockEntity<T>> extends MachineBlockEntity<T> implements IWorkable {

	public WorkableMachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
