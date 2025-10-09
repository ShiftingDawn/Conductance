package conductance.core.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import conductance.api.NCBlockStateProperties;

public final class ActiveBlock extends Block {

	public ActiveBlock(final Properties props) {
		super(props);
		this.registerDefaultState(this.getStateDefinition().any().setValue(NCBlockStateProperties.ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(NCBlockStateProperties.ACTIVE));
	}
}
