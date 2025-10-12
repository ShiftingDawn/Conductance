package conductance.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.IGridInteractable;
import conductance.api.block.InteractType;
import conductance.lib.pipenet.INetworkNode;
import conductance.lib.pipenet.LevelPipeNetwork;
import conductance.lib.pipenet.PipeNetHelper;

public abstract class PipeBlockEntity<NODE extends INetworkNode<NODE, DATA>, DATA, LEVELNET extends LevelPipeNetwork<NODE, DATA>> extends BlockEntity implements INetworkNode<NODE, DATA>, IGridInteractable {

	public PipeBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void setConnections(final int connections) {
		BlockState state = this.getBlockState();
		for (final Direction direction : Direction.values()) {
			state = state.setValue(PipeBlock.CONNECTION_PROPS.get(direction), PipeNetHelper.isConnected(connections, direction));
		}
		this.level.setBlockAndUpdate(this.getBlockPos(), state);
	}

	@Override
	public int getConnections() {
		int connections = 0;
		for (final Direction direction : Direction.values()) {
			if (this.getBlockState().getValue(PipeBlock.CONNECTION_PROPS.get(direction))) {
				connections = PipeNetHelper.setConnection(connections, direction, true);
			}
		}
		return connections;
	}

	@Override
	public abstract LEVELNET getNetwork(ServerLevel serverLevel);

	protected abstract InteractType getInteractType();

	@Override
	public void onLoad() {
		super.onLoad();
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			this.getNetwork(serverLevel).add(this.getBlockPos(), this.getConnections());
			PipeNetHelper.getConnections(this.getConnections()).stream()
				.filter(dir -> this.getPipeBlock().getPipeBlockEntity(serverLevel, this.getBlockPos().relative(dir)) == null)
				.filter(dir -> this.canConnectTo(serverLevel, this.getBlockPos(), dir))
				.forEach(dir -> this.getNetwork(serverLevel).addEndpoint(this.getBlockPos(), dir, true));
		}
	}

	@Override
	public void setRemoved() {
		if (this.getLevel() instanceof final ServerLevel serverLevel && serverLevel.isLoaded(this.getBlockPos())) {
			this.getNetwork(serverLevel).remove(this.getBlockPos());
		}
		super.setRemoved();
	}

	public void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			if (this.getPipeBlock().getPipeBlockEntity(serverLevel, neighborPos) == null) {
				if (this.getNetwork(serverLevel).isEndpoint(this.getBlockPos(), neighborSide)) {
					if (!this.canConnectTo(serverLevel, this.getBlockPos(), neighborSide)) {
						this.getNetwork(serverLevel).addEndpoint(this.getBlockPos(), neighborSide, false);
						this.setConnections(PipeNetHelper.setConnection(this.getConnections(), neighborSide, false));
					}
				} else {
					this.setConnections(PipeNetHelper.setConnection(this.getConnections(), neighborSide, false));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public PipeBlock<NODE, DATA, LEVELNET> getPipeBlock() {
		return (PipeBlock<NODE, DATA, LEVELNET>) this.getBlockState().getBlock();
	}

	@SuppressWarnings("unchecked")
	@Override
	public InteractionResult onToolUsed(@Nullable final InteractType interaction, final UseOnContext ctx, final Direction side) {
		if (interaction != this.getInteractType()) {
			if (ctx.getItemInHand().getItem() instanceof final PipeBlockItem pipeBlockItem && pipeBlockItem.getBlock().getNetworkType().equals(this.getPipeBlock().getNetworkType())) {
				final BlockState placeState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(side));
				final BlockHitResult newHit = new BlockHitResult(ctx.getClickLocation().relative(side, 1), ctx.getClickedFace(), ctx.getClickedPos().relative(side), false);
				final BlockPlaceContext placeContext = new BlockPlaceContext(ctx.getLevel(), ctx.getPlayer(), ctx.getHand(), ctx.getItemInHand(), newHit);
				if (placeState.canBeReplaced(placeContext)) {
					final InteractionResult placeResult = pipeBlockItem.place(placeContext);
					if (placeResult instanceof InteractionResult.Success) {
						final BlockState self = ctx.getLevel().getBlockState(ctx.getClickedPos());
						ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), self.setValue(PipeBlock.CONNECTION_PROPS.get(side), true));
						final BlockState other = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(side));
						ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos().relative(side), other.setValue(PipeBlock.CONNECTION_PROPS.get(side.getOpposite()), true));
					}
					return placeResult;
				}
			}
			return InteractionResult.PASS;
		}
		if (ctx.getLevel().getBlockEntity(ctx.getClickedPos()) instanceof final PipeBlockEntity<?, ?, ?> pipeBlockEntity) {
			final INetworkNode<?, ?> node = pipeBlockEntity.getPipeBlock().getPipeBlockEntity(ctx.getLevel(), ctx.getClickedPos().relative(side));
			if (node != null) {
				if (this.getLevel() instanceof final ServerLevel serverLevel) {
					final boolean connect = PipeNetHelper.isBlocked(this.getConnections(), side);
					this.getNetwork(serverLevel).setConnected((NODE) this, (NODE) node, side, connect);
				}
				return InteractionResult.SUCCESS_SERVER;
			}
			if (this.canConnectTo(ctx.getLevel(), ctx.getClickedPos(), side)) {
				if (this.getLevel() instanceof final ServerLevel serverLevel) {
					final boolean connect = PipeNetHelper.isBlocked(this.getConnections(), side);
					this.setConnections(PipeNetHelper.setConnection(this.getConnections(), side, connect));
					this.getNetwork(serverLevel).addEndpoint(this.getBlockPos(), side, connect);
				}
				return InteractionResult.SUCCESS_SERVER;
			}
		}
		return InteractionResult.PASS;
	}
}
