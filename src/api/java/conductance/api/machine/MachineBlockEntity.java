package conductance.api.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import com.lowdragmc.lowdraglib.syncdata.IEnhancedManaged;
import com.lowdragmc.lowdraglib.syncdata.IManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAsyncAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoPersistBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.capability.cover.CoverManager;
import conductance.api.capability.cover.ICoverable;
import conductance.api.machine.capability.MachineCapability;
import conductance.api.machine.capability.MachineRecipeCapability;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeProcessor;
import conductance.api.util.IOMode;
import conductance.api.util.RotationState;

public abstract class MachineBlockEntity<T extends MachineBlockEntity<T>> extends BaseBlockEntity implements IAsyncAutoSyncBlockEntity, IAutoPersistBlockEntity, IEnhancedManaged, ICoverable {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineBlockEntity.class);
	private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
	@Getter
	private final MachineType<T> machineType;
	@Getter
	private final List<MachineCapability> capabilities = new ArrayList<>();
	@Getter
	@DescSynced
	@Persisted(key = "cover")
	protected final CoverManager coverManager;

	public MachineBlockEntity(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType.getBlockEntityType().get(), pos, blockState);
		this.machineType = machineType;
		this.coverManager = new CoverManager(this) {
			@Override
			public double getCoverBackplateThickness() {
				return 0;
			}
		};
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

	protected Predicate<ItemStack> getItemCapFilter(@Nullable final Direction side) {
		if (side != null && this instanceof final ICoverable coverable) {
			final Optional<?> cover = coverable.getCoverManager().getCover(side);
			if (cover.isPresent() && cover.get() instanceof final IItemFilterHolder filterHolder) {
				return filterHolder.getItemFilter();
			}
		}
		return item -> true;
	}

	protected Predicate<FluidStack> getFluidCapFilter(@Nullable final Direction side) {
		if (side != null && this instanceof final ICoverable coverable) {
			final Optional<?> cover = coverable.getCoverManager().getCover(side);
			if (cover.isPresent() && cover.get() instanceof final IFluidFilterHolder filterHolder) {
				return filterHolder.getFluidFilter();
			}
		}
		return fluid -> true;
	}

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
		if (!useCovers || side == null || !(this instanceof final ICoverable coverable)) {
			return transferList;
		}
		return coverable.getCoverManager().getCover(side)
				.filter(cover -> cover instanceof IItemCapabilityProvider)
				.map(cover -> ((IItemCapabilityProvider) cover).getItemHandlerCapability())
				.orElse(transferList);
	}

	@Nullable
	public IFluidHandler getFluidTransferCapability(@Nullable final Direction side, final boolean useCovers) {
		final List<IFluidHandler> handlers = this.capabilities.stream()
				.filter(capability -> capability instanceof IFluidHandler && capability.hasCapability(side))
				.map(IFluidHandler.class::cast)
				.toList();
		if (handlers.isEmpty()) {
			return null;
		}
		final IOMode ioMode = IOMode.INPUT_OUTPUT;
		//TODO auto output
//		if (side != null && this instanceof final IAutoOutputItem autoOutputItem && autoOutputItem.getItemOutputSide() == side && !autoOutputItem.allowItemInputFromOutputSide()) {
//			ioMode = IOMode.OUTPUT;
//		}
		final IOFluidTransferList transferList = new IOFluidTransferList(handlers, ioMode, this.getFluidCapFilter(side));
		if (!useCovers || side == null || !(this instanceof final ICoverable coverable)) {
			return transferList;
		}
		return coverable.getCoverManager().getCover(side)
				.filter(cover -> cover instanceof IFluidCapabilityProvider)
				.map(cover -> ((IFluidCapabilityProvider) cover).getFluidHandlerCapability())
				.orElse(transferList);
	}
	//endregion

	//region Update
	@Override
	public void onLoad() {
		super.onLoad();
		this.capabilities.forEach(MachineCapability::onLoad);
	}

	@Override
	public void onUnload() {
		super.onUnload();
		this.capabilities.forEach(MachineCapability::onUnload);
	}

	@Override
	public void scheduleRenderUpdate() {
		super.scheduleRenderUpdate();
	}

	//endregion

	//region Event

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
	@Override
	public Direction getFrontFacing() {
		final var blockState = this.getBlockState();
		if (blockState.getBlock() instanceof final IMachineBlock<?> machineBlock) {
			return machineBlock.getFrontFacing(blockState);
		}
		return Direction.NORTH;
	}

	public final boolean hasFrontFacing() {
		final var blockState = this.getBlockState();
		if (blockState.getBlock() instanceof final IMachineBlock<?> machineBlock) {
			return machineBlock.getRotationState() != RotationState.NONE;
		}
		return false;
	}

	public boolean isFacingValid(final Direction facing) {
		if (this.hasFrontFacing() && facing == this.getFrontFacing()) {
			return false;
		}
		final var blockState = this.getBlockState();
		if (blockState.getBlock() instanceof final IMachineBlock<?> machineBlock) {
			return machineBlock.getRotationState().test(facing);
		}
		return false;
	}
	//endregion
}
