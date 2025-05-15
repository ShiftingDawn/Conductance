package conductance.api.capability.cover;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.machine.EnvironmentProvider;
import conductance.api.machine.IBlockEntity;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.Synchronized;

public class CoverEntity<COVER extends CoverEntity<COVER>> implements IManaged, EnvironmentProvider {

	private final ManagedDataMap dataMap = CAPI.syncHelper().requestDataMap(this);
	@Getter
	private final CoverManager manager;
	@Getter
	private final CoverType<COVER> coverType;
	@Getter
	private final Direction side;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	@Persisted
	@Synchronized
	private ItemStack attachItem = ItemStack.EMPTY;

	public CoverEntity(final CoverManager manager, final CoverType<COVER> coverType, final Direction side) {
		this.manager = manager;
		this.coverType = coverType;
		this.side = side;
	}

	public boolean blockConnections() {
		return true;
	}

	public boolean shouldRenderCoverBackplate() {
		return true;
	}

	public boolean canAttachTo(final CoverManager coverManager, final IBlockEntity blockEntity) {
		return true;
	}

	@Override
	public IBlockEntity getBlockEntity() {
		return this.manager.getBlockEntity();
	}

	public void onCoverAttached(final ItemStack itemStack, final ServerPlayer player) {
	}

	public void onCoverLoaded() {
	}

	public void onCoverUnloaded() {
	}

	public void onCoverRemoved() {
	}

	public void onCoverManagerChanged() {
	}

	@Override
	public void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
	}

	@Override
	public void scheduleRenderUpdate() {
		this.manager.scheduleRenderUpdate();
	}

	@Override
	public ManagedDataMap getDataMap() {
		return this.dataMap;
	}
}
