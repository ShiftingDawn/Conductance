package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MachineType;
import conductance.api.machine.api.IWorkable;

public abstract class WorkableMultiPartMachineBlockEntity<T extends WorkableMultiPartMachineBlockEntity<T>> extends MultiPartMachineBlockEntity<T> implements IWorkable {

	public WorkableMultiPartMachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
