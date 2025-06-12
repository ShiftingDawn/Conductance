package conductance.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.cover.CoverManager;
import conductance.api.cover.ICoverable;
import conductance.api.machine.BaseBlockEntity;
import conductance.api.machine.IPaintable;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.RequireRenderUpdate;
import conductance.api.machine.sync.Synchronized;
import conductance.api.util.world.IExtendedInteractable;
import conductance.api.util.world.InteractType;
import conductance.lib.pipenet.INetworkNode;
import conductance.lib.pipenet.LevelPipeNetwork;
import conductance.lib.pipenet.PipeNetHelper;

public abstract class PipeBlockEntity<NODE extends INetworkNode<NODE, DATA>, DATA, LEVELNET extends LevelPipeNetwork<NODE, DATA>> extends BaseBlockEntity
		implements INetworkNode<NODE, DATA>, IManaged, ICoverable, IExtendedInteractable, IPaintable {

	private final ManagedDataMap dataMap = CAPI.syncHelper().requestDataMap(this);
	@Synchronized
	@Persisted(key = "covers")
	private final CoverManager coverManager = new CoverManager(this);
	@Getter
	@Setter
	@Synchronized
	@Persisted
	@RequireRenderUpdate
	private int connections = 0;
	@Getter
	@Setter
	@Synchronized
	@Persisted
	@RequireRenderUpdate
	private int paintColor = -1;

	public PipeBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
		super(type, pos, state);
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
	public void onUnload() {
		super.onUnload();
		if (this.getLevel() instanceof final ServerLevel serverLevel && serverLevel.isLoaded(this.getBlockPos())) {
			this.getNetwork(serverLevel).remove(this.getBlockPos());
		}
	}

	@Override
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

	@Override
	public ManagedDataMap getDataMap() {
		return this.dataMap;
	}

	@Override
	public CoverManager getCoverManager() {
		return this.coverManager;
	}

	@Override
	public void scheduleRenderUpdate() {
		super.scheduleRenderUpdate();
	}

	@Override
	public Direction getFrontFacing() {
		return Direction.NORTH;
	}

	@SuppressWarnings("unchecked")
	public PipeBlock<NODE, DATA, LEVELNET> getPipeBlock() {
		return (PipeBlock<NODE, DATA, LEVELNET>) this.getBlockState().getBlock();
	}

	@SuppressWarnings("unchecked")
	@Override
	public InteractionResult onToolUsed(@Nullable final InteractType interaction, final UseOnContext context, final Direction side) {
		if (interaction != this.getInteractType()) {
			return InteractionResult.PASS;
		}
		if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof final PipeBlockEntity<?, ?, ?> pipeBlockEntity) {
			final INetworkNode<?, ?> node = pipeBlockEntity.getPipeBlock().getPipeBlockEntity(context.getLevel(), context.getClickedPos().relative(side));
			if (node != null) {
				if (this.getLevel() instanceof final ServerLevel serverLevel) {
					final boolean connect = PipeNetHelper.isBlocked(this.getConnections(), side);
					this.getNetwork(serverLevel).setConnected((NODE) this, (NODE) node, side, connect);
				}
				return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
			}
			if (this.canConnectTo(context.getLevel(), context.getClickedPos(), side)) {
				if (this.getLevel() instanceof final ServerLevel serverLevel) {
					final boolean connect = PipeNetHelper.isBlocked(this.getConnections(), side);
					this.setConnections(PipeNetHelper.setConnection(this.getConnections(), side, connect));
					this.getNetwork(serverLevel).addEndpoint(this.getBlockPos(), side, connect);
				}
				return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public int getPaintColorDefault() {
		if (this.getBlockState().getBlock() instanceof final WireBlock materialPipeBlock) {
			return materialPipeBlock.getMaterial().getMaterialColorRGB();
		} else {
			return 0xFFFFFF;
		}
	}
}
