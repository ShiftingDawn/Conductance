package conductance.api.machine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import com.lowdragmc.lowdraglib.syncdata.IEnhancedManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAsyncAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoPersistBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.capability.MachineCapability;
import conductance.api.machine.capability.MachineRecipeCapability;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeProcessor;
import conductance.api.util.IOMode;

public abstract class MachineBlockEntity<T extends MachineBlockEntity<T>> extends BlockEntity implements IAsyncAutoSyncBlockEntity, IAutoPersistBlockEntity, IEnhancedManaged {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineBlockEntity.class);
	private final int timerOffset = CAPI.RANDOM.nextInt(20);
	private final List<MachineRunnable> ticks = new ArrayList<>();
	private final List<MachineRunnable> pending = new ArrayList<>();
	private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
	@Getter
	private final MachineType<T> machineType;
	@Getter
	private final List<MachineCapability> capabilities = new ArrayList<>();

	public MachineBlockEntity(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType.getBlockEntityType().get(), pos, blockState);
		this.machineType = machineType;
	}

	//region SyncData
	@Override
	public ManagedFieldHolder getFieldHolder() {
		return MachineBlockEntity.MANAGED_FIELD_HOLDER;
	}

	@Override
	public IManagedStorage getSyncStorage() {
		return this.syncStorage;
	}

	@Override
	public IManagedStorage getRootStorage() {
		return this.getSyncStorage();
	}

	@Override
	public void onChanged() {
		super.setChanged();
	}
	//endregion

	//region Capability
	public final void registerCapability(final MachineCapability capability) {
		this.capabilities.add(capability);
	}

	@Nullable
	public final <C extends MachineCapability> C getCapability(final Class<C> type, final boolean exact) {
		for (final MachineCapability capability : this.capabilities) {
			if (exact) {
				if (type.equals(capability.getClass())) {
					return type.cast(capability);
				}
			} else if (type.isAssignableFrom(capability.getClass())) {
				return type.cast(capability);
			}
		}
		return null;
	}

	@Nullable
	public final <C extends MachineCapability> C getCapability(final Class<C> type) {
		return this.getCapability(type, false);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public final <C> MachineRecipeCapability<C> getRecipeCapability(final IRecipeElementType<C> type) {
		for (final MachineCapability capability : this.capabilities) {
			if (capability instanceof final MachineRecipeCapability<?> recipeCapability && recipeCapability.getElementType() == type) {
				return (MachineRecipeCapability<C>) capability;
			}
		}
		return null;
	}

	//TODO covers
	protected Predicate<ItemStack> getItemCapFilter(@Nullable final Direction side) {
//		if (side != null) {
//			final Optional<?> cover = this.getCoverManager().getCover(side);
//			if (cover.isPresent() && cover.get() instanceof final IItemFilterHolder filterHolder) {
//				return filterHolder.getItemFilter();
//			}
//		}
		return item -> true;
	}

	//TODO fluid
//	protected Predicate<FluidStack> getFluidCapFilter(@Nullable final Direction side) {
//		if (side != null) {
//			final Optional<?> cover = this.getCoverManager().getCover(side);
//			if (cover.isPresent() && cover.get() instanceof final IFluidFilterHolder filterHolder) {
//				return filterHolder.getFluidFilter();
//			}
//		}
//		return fluid -> true;
//	}

	@Nullable
	public IItemHandler getItemTransferCapability(@Nullable final Direction side, final boolean useCovers) {
		final List<IItemHandlerModifiable> handlers = this.capabilities.stream()
				.filter(capability -> capability instanceof IItemHandlerModifiable && capability.hasCapability(side))
				.map(IItemHandlerModifiable.class::cast)
				.toList();
		if (handlers.isEmpty()) {
			return null;
		}
		final IOMode ioMode = IOMode.INPUT_OUTPUT;
		//TODO auto output
//		if (side != null && this instanceof final IAutoOutputItem autoOutputItem && autoOutputItem.getItemOutputSide() == side && !autoOutputItem.allowItemInputFromOutputSide()) {
//			ioMode = IOMode.OUTPUT;
//		}
		final IOItemTransferList transferList = new IOItemTransferList(handlers, ioMode, this.getItemCapFilter(side));
		if (!useCovers || side == null) {
			return transferList;
		}
		return null;
		//TODO covers
//		return this.getCoverManager().getCover(side)
//				.filter(cover -> cover instanceof IDelegateItemHandler)
//				.map(cover -> ((IDelegateItemHandler) cover).getItemHandlerCapability())
//				.orElse(transferList);
	}
	//endregion

	//region Update
	@Override
	public void onLoad() {
		super.onLoad();
		this.capabilities.forEach(MachineCapability::onLoad);
	}

	public void onUnload() {
		this.ticks.forEach(MachineRunnable::invalidate);
		this.ticks.clear();
		this.capabilities.forEach(MachineCapability::onUnload);
	}

	@Nullable
	public final MachineRunnable addTick(final Runnable action) {
		if (!this.isRemote()) {
			return Util.make(new MachineRunnable(action), result -> {
				this.pending.add(result);
				if (!this.getBlockState().getValue(MachineBlock.ACTIVE) && this.getLevel() instanceof final ServerLevel serverLevel) {
					final BlockState newState = this.getBlockState().setValue(MachineBlock.ACTIVE, true);
					serverLevel.setBlockAndUpdate(this.getBlockPos(), newState);
				}
			});
		}
		return null;
	}

	@Nullable
	public final MachineRunnable addTick(@Nullable final MachineRunnable previous, final Runnable action) {
		if (previous == null || !previous.isValid()) {
			return this.addTick(action);
		}
		return previous;
	}

	@OnlyIn(Dist.CLIENT)
	protected void onClientTick() {
	}

	protected final void handleServerTick() {
		if (!this.pending.isEmpty()) {
			this.ticks.addAll(this.pending);
			this.pending.clear();
		}
		final Iterator<MachineRunnable> iterator = this.ticks.iterator();
		while (iterator.hasNext()) {
			final MachineRunnable tick = iterator.next();
			tick.tick();
			if (this.isInvalid()) {
				break;
			}
			if (!tick.isValid()) {
				iterator.remove();
			}
		}
		if (this.isValid() && this.ticks.isEmpty() && this.pending.isEmpty()) {
			assert this.level != null;
			this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(MachineBlock.ACTIVE, false));
		}
	}

	@Override
	public void scheduleRenderUpdate() {
		if (this.level != null) {
			final var state = this.level.getBlockState(this.getBlockPos());
			if (this.level.isClientSide) {
				this.level.sendBlockUpdated(this.getBlockPos(), state, state, 1 << 3);
			} else {
				this.level.blockEvent(this.getBlockPos(), state.getBlock(), 1, 0);
			}
		}
	}
	//endregion

	//region Event
	protected void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
	}

	protected boolean canConnectRedstone(@Nullable final Direction direction) {
		return false;
	}

	protected int getRedstone(final Direction side) {
		return 0;
	}

	protected int getRedstoneDirect(final Direction side) {
		return 0;
	}

	protected int getRedstoneAnalog() {
		return 0;
	}

	public void onStateChanged(final RecipeProcessor.State oldState, final RecipeProcessor.State newState) {
	}
	//endregion

	//region Misc

	public final long getTimerOffset() {
		return this.level != null ? this.level.getGameTime() + this.timerOffset : this.timerOffset;
	}

	public final int getTimerOffsetValue() {
		return this.timerOffset;
	}

	/**
	 * @return <code>true</code> if the currently executing code is running on a client or <code>false</code> if it's a server
	 */
	public boolean isRemote() {
		return this.getLevel() == null ? CAPI.isClient() : this.getLevel().isClientSide();
	}

	/**
	 * @return <code>true</code> if the BlockEntity should be considered valid, <code>false</code> otherwise
	 * @see #isInvalid()
	 */
	public boolean isValid() {
		return !this.isRemoved();
	}

	/**
	 * @return <code>true</code> if the BlockEntity should be considered invalid, <code>false</code> otherwise
	 * @see #isValid()
	 */
	public boolean isInvalid() {
		return this.isRemoved();
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		this.onUnload();
	}
	//endregion
}
