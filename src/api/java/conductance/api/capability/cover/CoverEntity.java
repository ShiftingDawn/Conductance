package conductance.api.capability.cover;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.syncdata.IEnhancedManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.EnvironmentProvider;
import conductance.api.machine.IBlockEntity;

public class CoverEntity<COVER extends CoverEntity<COVER>> implements IEnhancedManaged, EnvironmentProvider {

	public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(CoverEntity.class);
	private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
	@Getter
	private final CoverManager manager;
	@Getter
	private final CoverType<COVER> coverType;
	@Getter
	private final Direction side;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	@Persisted
	@DescSynced
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
	public void onChanged() {
		this.manager.onChanged();
	}

	@Override
	public void scheduleRenderUpdate() {
		this.manager.scheduleRenderUpdate();
	}

	@Override
	public FieldManagedStorage getSyncStorage() {
		return this.syncStorage;
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return CoverEntity.MANAGED_FIELD_HOLDER;
	}
}
