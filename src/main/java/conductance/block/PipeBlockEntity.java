package conductance.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.syncdata.IEnhancedManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAsyncAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoPersistBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.capability.cover.CoverManager;
import conductance.api.capability.cover.ICoverable;
import conductance.api.machine.BaseBlockEntity;
import conductance.api.machine.IPaintable;
import conductance.api.util.IExtendedInteractable;
import conductance.api.util.InteractType;
import conductance.core.pipenet.INetworkNode;
import conductance.core.pipenet.LevelPipeNetwork;
import conductance.core.pipenet.PipeNetHelper;

public abstract class PipeBlockEntity<DATA, LEVELNET extends LevelPipeNetwork<DATA>> extends BaseBlockEntity
		implements INetworkNode<DATA>, IEnhancedManaged, IAsyncAutoSyncBlockEntity, IAutoPersistBlockEntity, ICoverable, IExtendedInteractable, IPaintable {

	public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(PipeBlockEntity.class);
	@Getter
	private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

	@DescSynced
	@Persisted(key = "covers")
	private final CoverManager coverManager = new CoverManager(this);
	@Getter
	@Setter
	@DescSynced
	@Persisted
	@RequireRerender
	private int connections = 0;
	@Getter
	@Setter
	@DescSynced
	@Persisted
	@RequireRerender
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
			final INetworkNode<DATA> node = this.getPipeBlock().getPipeBlockEntity(serverLevel, neighborPos);
			if (node != null) {
				System.out.println(neighborState);
			}
		}
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return PipeBlockEntity.MANAGED_FIELD_HOLDER;
	}

	@Override
	public IManagedStorage getRootStorage() {
		return this.syncStorage;
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
	public void onChanged() {
		this.setChanged();
	}

	@Override
	public Direction getFrontFacing() {
		return Direction.NORTH;
	}

	@SuppressWarnings("unchecked")
	public PipeBlock<DATA, LEVELNET> getPipeBlock() {
		return (PipeBlock<DATA, LEVELNET>) this.getBlockState().getBlock();
	}

	@SuppressWarnings("unchecked")
	@Override
	public InteractionResult onToolUsed(@Nullable final InteractType interaction, final UseOnContext context, final Direction side) {
		if (interaction != this.getInteractType()) {
			return InteractionResult.PASS;
		}
		if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof final PipeBlockEntity<?, ?> pipeBlockEntity) {
			final INetworkNode<?> node = pipeBlockEntity.getPipeBlock().getPipeBlockEntity(context.getLevel(), context.getClickedPos().relative(side));
			if (node != null) {
				if (this.getLevel() instanceof final ServerLevel serverLevel) {
					final boolean connect = PipeNetHelper.isBlocked(this.getConnections(), side);
					this.getNetwork(serverLevel).setConnected(this, (INetworkNode<DATA>) node, side, connect);
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
		if (this.getBlockState().getBlock() instanceof final CableBlock materialPipeBlock) {
			return materialPipeBlock.getMaterial().getMaterialColorRGB();
		} else {
			return 0xFFFFFF;
		}
	}
}
