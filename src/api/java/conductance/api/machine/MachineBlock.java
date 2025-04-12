package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import com.lowdragmc.lowdraglib.gui.factory.BlockEntityUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.MiscUtils;

@SuppressWarnings("deprecation")
public class MachineBlock<T extends MachineBlockEntity<T>> extends Block implements IMachineBlock<T> {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	private final MachineType<T> machineType;

	public MachineBlock(final Properties props, final MachineType<T> machineType) {
		super(props);
		this.machineType = machineType;
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(MachineBlock.FACING, Direction.NORTH)
				.setValue(MachineBlock.LIT, false)
				.setValue(MachineBlock.ACTIVE, false)
		);
	}

	@Override
	public MachineType<T> getMachineType() {
		return this.machineType;
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(MachineBlock.FACING, MachineBlock.LIT, MachineBlock.ACTIVE);
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return this.defaultBlockState().setValue(MachineBlock.FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected BlockState rotate(final BlockState state, final Rotation rotation) {
		return state.setValue(MachineBlock.FACING, rotation.rotate(state.getValue(MachineBlock.FACING)));
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(MachineBlock.FACING)));
	}

	@Override
	public BlockEntity newBlockEntity(final BlockPos blockPos, final BlockState blockState) {
		return this.getMachineType().getBlockEntityType().get().create(blockPos, blockState);
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
		return this.<InteractionResult>getMachine(level, pos, mbt -> {
			if (mbt instanceof final IUIHolder.Block holder && !level.isClientSide && player instanceof final ServerPlayer serverPlayer) {
				BlockEntityUIFactory.INSTANCE.openUI(holder.self(), serverPlayer);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}, () -> super.useWithoutItem(state, level, pos, player, hitResult));
	}

	@Nullable
	@Override
	public <BE extends BlockEntity> BlockEntityTicker<BE> getTicker(final Level level, final BlockState state, final BlockEntityType<BE> blockEntityType) {
		if (blockEntityType == this.machineType.getBlockEntityType().get()) {
			if (level.isClientSide()) {
				return (level1, blockPos, blockState, be) -> {
					if (be instanceof final MachineBlockEntity<?> baseBlockEntity) {
						baseBlockEntity.onClientTick();
					}
				};
			} else {
				if (state.getValue(MachineBlock.ACTIVE)) {
					return (level1, blockPos, blockState, be) -> {
						if (be instanceof final MachineBlockEntity<?> baseBlockEntity) {
							baseBlockEntity.handleServerTick();
						}
					};
				}
			}
		}
		return null;
	}

	@Override
	public void onNeighborChange(final BlockState state, final LevelReader level, final BlockPos pos, final BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
		this.setMachine(level, pos, machine -> machine.onNeighborChanged(neighbor, level.getBlockState(neighbor), MiscUtils.getNeighborSide(pos, neighbor)));
	}

	@Override
	public boolean canConnectRedstone(final BlockState state, final BlockGetter level, final BlockPos pos, @Nullable final Direction direction) {
		return this.getMachine(level, pos, machine -> machine.canConnectRedstone(direction), super.canConnectRedstone(state, level, pos, direction));
	}

	@Override
	protected int getSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		return this.getMachine(level, pos, machine -> machine.getRedstone(direction), super.getSignal(state, level, pos, direction));
	}

	@Override
	protected int getDirectSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
		return this.getMachine(level, pos, machine -> machine.getRedstoneDirect(direction), super.getDirectSignal(state, level, pos, direction));
	}

	@Override
	protected int getAnalogOutputSignal(final BlockState state, final Level level, final BlockPos pos) {
		return this.getMachine(level, pos, MachineBlockEntity::getRedstoneAnalog, super.getAnalogOutputSignal(state, level, pos));
	}
}
