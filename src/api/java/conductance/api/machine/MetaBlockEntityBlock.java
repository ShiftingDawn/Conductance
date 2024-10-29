package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.MiscUtils;

public class MetaBlockEntityBlock<T extends MetaBlockEntity<T>> extends Block implements IMbeBlock, EntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty ACTIVE = BlockStateProperties.LIT;
	public static final BooleanProperty UPDATABLE = BooleanProperty.create("updatable");

	@Getter
	private final MetaBlockEntityType<T> metaBlockEntityType;

	public MetaBlockEntityBlock(final Properties props, final MetaBlockEntityType<T> metaBlockEntityType) {
		super(props);
		this.metaBlockEntityType = metaBlockEntityType;
		this.registerDefaultState(this.getStateDefinition().any().setValue(MetaBlockEntityBlock.FACING, Direction.NORTH).setValue(MetaBlockEntityBlock.ACTIVE, false));
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return this.defaultBlockState().setValue(MetaBlockEntityBlock.FACING, context.getHorizontalDirection().getOpposite());
	}
	@Override
	protected BlockState rotate(final BlockState state, final Rotation rotation) {
		return state.setValue(MetaBlockEntityBlock.FACING, rotation.rotate(state.getValue(MetaBlockEntityBlock.FACING)));
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(MetaBlockEntityBlock.FACING)));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(MetaBlockEntityBlock.FACING, MetaBlockEntityBlock.ACTIVE);
	}

	@Override
	@Nullable
	public BlockEntity newBlockEntity(final BlockPos blockPos, final BlockState blockState) {
		return new MetaBlockEntityBlockEntity<>(blockPos, blockState, this.metaBlockEntityType);
	}

	@Override
	public void onNeighborChange(final BlockState state, final LevelReader level, final BlockPos pos, final BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
		if (level.getBlockEntity(pos) instanceof final LevelEventListener levelEventListener) {
			levelEventListener.onNeighborChanged(neighbor, level.getBlockState(neighbor), MiscUtils.getNeighborSide(pos, neighbor));
		}
	}

	@Override
	public boolean canConnectRedstone(final BlockState state, final BlockGetter level, final BlockPos pos, @Nullable final Direction direction) {
		if (level.getBlockEntity(pos) instanceof final IRedstoneHandler redstoneHandler) {
			return redstoneHandler.canConnectRedstone(direction);
		}
		return super.canConnectRedstone(state, level, pos, direction);
	}

	@Override
	protected int getSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		if (level.getBlockEntity(pos) instanceof final IRedstoneProvider redstoneProvider) {
			return redstoneProvider.getRedstone(direction);
		}
		return super.getSignal(state, level, pos, direction);
	}

	@Override
	protected int getDirectSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		if (level.getBlockEntity(pos) instanceof final IRedstoneProvider redstoneProvider) {
			return redstoneProvider.getRedstoneDirect(direction);
		}
		return super.getDirectSignal(state, level, pos, direction);
	}

	@Override
	protected int getAnalogOutputSignal(final BlockState state, final Level level, final BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof final IRedstoneProvider redstoneProvider) {
			return redstoneProvider.getRedstoneAnalog();
		}
		return super.getAnalogOutputSignal(state, level, pos);
	}

	@Override
	protected boolean isSignalSource(final BlockState state) {
		return super.isSignalSource(state);
	}
}
