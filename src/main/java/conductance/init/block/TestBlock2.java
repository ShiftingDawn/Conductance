package conductance.init.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import conductance.api.block.BlockRotationHelper;
import conductance.api.block.BlockRotationType;

public class TestBlock2 extends Block {

	public TestBlock2(final Properties props) {
		super(props);
		this.registerDefaultState(BlockRotationHelper.addToDefaultState(BlockRotationType.EXTENDED, this.getStateDefinition().any()));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		BlockRotationHelper.addToBlockStateDefinition(BlockRotationType.EXTENDED, builder);
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		state = BlockRotationHelper.setFacingOnPlacement(state, context);
		return state;
	}

	@Override
	protected BlockState rotate(final BlockState state, final Rotation rotation) {
		return BlockRotationHelper.applyRotation(state, rotation);
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		return BlockRotationHelper.applyMirror(state, mirror);
	}
}
