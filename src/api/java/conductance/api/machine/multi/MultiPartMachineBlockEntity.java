package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MultiPartMachineBlockEntity<T extends MultiPartMachineBlockEntity<T>> extends MultiMachineBlockEntity<T> implements IMultiBlockPart {

	public MultiPartMachineBlockEntity(final MultiMachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public boolean isConnectedTo(final BlockPos controllerPos) {
		return false;
	}

	@Override
	public void setConnectedTo(final BlockPos controllerPos, final boolean connect) {
	}
}
