package conductance.init.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import conductance.api.block.BlockHelper;
import conductance.api.block.RotationType;

public class TestBlock extends Block {

	private static final ThreadLocal<RotationType> ROTATION_TYPE = new ThreadLocal<>();

	private TestBlock(final Properties props) {
		super(props);
		this.registerDefaultState(BlockHelper.setDefaultRotationValues(TestBlock.ROTATION_TYPE.get(), this.getStateDefinition().any()));
	}

	public static TestBlock create(final Properties props, final RotationType rotationType) {
		TestBlock.ROTATION_TYPE.set(rotationType);
		return new TestBlock(props);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		BlockHelper.addRotationProperties(TestBlock.ROTATION_TYPE.get(), builder);
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		state = BlockHelper.setFacingOnPlacement(state, context);
		return state;
	}

	@Override
	protected BlockState rotate(final BlockState state, final Rotation rotation) {
		return BlockHelper.applyRotation(state, rotation);
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		return BlockHelper.applyMirror(state, mirror);
	}
}
