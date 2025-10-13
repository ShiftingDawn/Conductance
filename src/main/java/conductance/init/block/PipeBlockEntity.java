package conductance.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import conductance.api.block.GridInteractionContext;
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
	public boolean isConnectedTo(final Direction side) {
		return this.getBlockState().getValue(PipeBlock.CONNECTION_PROPS.get(side));
	}

	@Override
	public boolean isEndpoint(final Direction side) {
		if (this.level instanceof final ServerLevel serverLevel) {
			return this.isConnectedTo(side) && this.getNetwork(serverLevel).isEndpoint(this.getBlockPos(), side);
		}
		return false;
	}

	@Override
	public int getConnections() {
		int connections = 0;
		for (final Direction direction : Direction.values()) {
			if (this.isConnectedTo(direction)) {
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

	@SuppressWarnings("unchecked")
	public void onNeighborChanged() {
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			final LEVELNET network = this.getNetwork(serverLevel);
			for (final Direction side : Direction.values()) {
				final BlockPos neighborPos = this.getBlockPos().relative(side);
				final boolean isConnected = this.isConnectedTo(side);
				final NODE neighborNode = this.getPipeBlock().getPipeBlockEntity(serverLevel, neighborPos);
				if (neighborNode != null) {
					if (isConnected != neighborNode.isConnectedTo(side.getOpposite())) {
						network.setConnected((NODE) this, neighborNode, side, isConnected);
					}
				} else {
					if (isConnected && !this.canConnectTo(serverLevel, this.getBlockPos(), side)) {
						this.getNetwork(serverLevel).addEndpoint(this.getBlockPos(), side, false);
						this.setConnections(PipeNetHelper.setConnection(this.getConnections(), side, false));
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public PipeBlock<NODE, DATA, LEVELNET> getPipeBlock() {
		return (PipeBlock<NODE, DATA, LEVELNET>) this.getBlockState().getBlock();
	}

	@Override
	public InteractionResult onGridInteraction(final GridInteractionContext ctx, final Direction side) {
		if (this.getInteractType() != ctx.getInteractType()) {
			return PipeBlockEntity.handleMaybePipePlacedAgainst(ctx, side, this.getPipeBlock().getNetworkType());
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

	@Override
	public boolean shouldRenderGrid(final GridInteractionContext ctx) {
		if (this.getInteractType() == ctx.getInteractType()) {
			return true;
		}
		if (ctx.getPlayer() == null || !ctx.getPlayer().isCrouching()) {
			return false;
		}
		return ctx.getItemInHand().getItem() instanceof final PipeBlockItem pipeBlockItem && this.getPipeBlock().getNetworkType().equals(pipeBlockItem.getBlock().getNetworkType());
	}

	private static InteractionResult handleMaybePipePlacedAgainst(final GridInteractionContext ctx, final Direction side, final ResourceLocation selfNetworkType) {
		if (ctx.getPlayer() == null || !ctx.getPlayer().isCrouching()) {
			return InteractionResult.PASS;
		}
		if (!(ctx.getItemInHand().getItem() instanceof final PipeBlockItem pipeBlockItem) || !selfNetworkType.equals(pipeBlockItem.getBlock().getNetworkType())) {
			return InteractionResult.PASS;
		}
		final BlockPos placePos = ctx.getClickedPos().relative(side);
		final BlockState placeState = ctx.getLevel().getBlockState(placePos);
		final BlockHitResult newHit = new BlockHitResult(ctx.getClickLocation().relative(side, 1), ctx.getClickedFace(), placePos, false);
		final BlockPlaceContext placeContext = new BlockPlaceContext(ctx.getLevel(), ctx.getPlayer(), ctx.getHand(), ctx.getItemInHand(), newHit);
		if (!placeState.canBeReplaced(placeContext)) {
			return InteractionResult.SUCCESS_SERVER;
		}
		final InteractionResult placeResult = pipeBlockItem.place(placeContext);
		if (placeResult instanceof InteractionResult.Success) {
			final BlockState self = ctx.getLevel().getBlockState(ctx.getClickedPos());
			ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), self.setValue(PipeBlock.CONNECTION_PROPS.get(side), true));
			final BlockState other = ctx.getLevel().getBlockState(placePos);
			ctx.getLevel().setBlockAndUpdate(placePos, other.setValue(PipeBlock.CONNECTION_PROPS.get(side.getOpposite()), true));
		}
		return placeResult;
	}
}
