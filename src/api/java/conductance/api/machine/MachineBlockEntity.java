package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MachineBlockEntity<T extends MachineBlockEntity<T>> extends BlockEntity {

	public MachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type.getBlockEntityType().get(), pos, blockState);
	}
}
