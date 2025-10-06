package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.block.BlockRotationHelper;

public class MultiControllerMachineBlockEntity<T extends MultiControllerMachineBlockEntity<T>> extends MultiMachineBlockEntity<T> implements IMultiBlockController {

	public MultiControllerMachineBlockEntity(final MultiMachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public MultiBlockStructure getStructure() {
		return this.getMachineType().getStructure();
	}

	@Override
	public MultiBlockInfo getMultiBlockInfo() {
		return new MultiBlockInfo(this.level, this.worldPosition, BlockRotationHelper.getFacing(this.getBlockState()));
	}

	@Override
	public boolean hasPart(final IMultiBlockPart part) {
		return false;
	}

	@Override
	public void addPart(final IMultiBlockPart part) {
	}

	@Override
	public void removePart(final IMultiBlockPart part) {
	}
}
