package conductance.api.machine;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import conductance.api.NCBlockStateProperties;

public class MachineBlockWorkable<T extends MachineBlockEntity<T>> extends MachineBlock<T> {

	public MachineBlockWorkable(final Properties properties, final MachineType<T> machineType) {
		super(properties, machineType);
	}

	@Override
	protected BlockState createDefaultState() {
		return super.createDefaultState().setValue(NCBlockStateProperties.WORKING, false);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(NCBlockStateProperties.WORKING);
	}
}
