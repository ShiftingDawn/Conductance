package conductance.api.machine.capability;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.fluid.IFluidHandlerModifiable;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.FluidStackTransfer;
import conductance.api.machine.ICapabilityHandler;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.util.IOMode;

public class MachineRecipeCapabilityFluids extends MachineRecipeCapability<SizedFluidIngredient> implements ICapabilityHandler, IFluidHandlerModifiable {

	public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineRecipeCapabilityFluids.class, MachineRecipeCapability.MANAGED_FIELD_HOLDER);
	@Persisted(subPersisted = true)
	@Getter
	private final FluidStackTransfer[] fluidTanks;
	@Setter
	protected boolean allowFluidOverflow;
	@Nullable
	private Boolean isEmpty;

	public MachineRecipeCapabilityFluids(final MachineBlockEntity<?> machine, final int slots, final IOMode capabilityIoMode, final IOMode handlerIoMode, final Function<Integer, FluidStackTransfer> tankFactory) {
		super(machine, NCRecipeElementTypes.FLUID, capabilityIoMode, handlerIoMode);
		this.fluidTanks = new FluidStackTransfer[slots];
		for (int i = 0; i < slots; ++i) {
			this.fluidTanks[i] = tankFactory.apply(i);
			this.fluidTanks[i].setOnContentsChanged(this::onContentsChanged);
		}
		if (this.getCapabilityIoMode().isInput()) {
			this.allowFluidOverflow = true;
		}
	}

	public MachineRecipeCapabilityFluids(final MachineBlockEntity<?> machine, final int slots, final int capacity, final IOMode capabilityIoMode, final IOMode handlerIoMode) {
		this(machine, slots, capabilityIoMode, handlerIoMode, ignored -> new FluidStackTransfer(capacity));
	}

	public MachineRecipeCapabilityFluids(final MachineBlockEntity<?> machine, final int slots, final int capacity, final IOMode ioMode) {
		this(machine, slots, capacity, ioMode, ioMode);
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return MachineRecipeCapabilityFluids.MANAGED_FIELD_HOLDER;
	}

	public MachineRecipeCapabilityFluids setFilter(final Predicate<FluidStack> filter) {
		for (final FluidStackTransfer storage : this.fluidTanks) {
			storage.setValidator(filter);
		}
		return this;
	}

	public void exportToNearby(final Direction... facings) {
		if (this.isEmpty()) {
			return;
		}
		final Level level = this.getMachineBlockEntity().getLevel();
		assert level != null;
		final BlockPos pos = this.getMachineBlockEntity().getBlockPos();
		for (final Direction facing : facings) {
			FluidTransferHelper.exportToTarget(this, Integer.MAX_VALUE, f -> true, level, pos.relative(facing), facing.getOpposite());
		}
	}

	public void importFromNearby(final Direction... facings) {
		final Level level = this.getMachineBlockEntity().getLevel();
		assert level != null;
		final BlockPos pos = this.getMachineBlockEntity().getBlockPos();
		for (final Direction facing : facings) {
			FluidTransferHelper.importToTarget(this, Integer.MAX_VALUE, f -> true, level, pos.relative(facing), facing.getOpposite());
		}
	}

	@Override
	@Nullable
	protected List<SizedFluidIngredient> handleInternal(final IOMode ioMode, final IRecipe recipe, final List<SizedFluidIngredient> inputs, final boolean simulate) {
		if (ioMode != this.getCapabilityIoMode()) {
			return inputs;
		}
		final FluidStackTransfer[] tanks = simulate ? Arrays.stream(this.fluidTanks).map(FluidStackTransfer::copy).toArray(FluidStackTransfer[]::new) : this.fluidTanks;
		for (final FluidStackTransfer capability : tanks) {
			final Iterator<SizedFluidIngredient> iterator = inputs.iterator();
			if (ioMode == IOMode.INPUT) {
				while (iterator.hasNext()) {
					final SizedFluidIngredient fluidStack = iterator.next();
					if (fluidStack.ingredient().hasNoFluids()) {
						iterator.remove();
						continue;
					}
					boolean found = false;
					FluidStack foundStack = null;
					for (int i = 0; i < capability.getTanks(); ++i) {
						final FluidStack stored = capability.getFluidInTank(i);
						if (!fluidStack.test(stored)) {
							continue;
						}
						found = true;
						foundStack = stored;
					}
					if (!found) {
						continue;
					}
					final FluidStack copy = foundStack.copy();
					copy.setAmount(fluidStack.amount());
					final FluidStack drained = capability.drain(copy, FluidAction.EXECUTE);

					if (fluidStack.amount() - drained.getAmount() <= 0) {
						iterator.remove();
					}
				}
			} else if (ioMode == IOMode.OUTPUT) {
				while (iterator.hasNext()) {
					final SizedFluidIngredient fluidStack = iterator.next();
					if (fluidStack.ingredient().hasNoFluids()) {
						iterator.remove();
						continue;
					}
					final FluidStack[] fluids = fluidStack.getFluids();
					if (fluids.length == 0) {
						iterator.remove();
						continue;
					}
					final FluidStack output = fluids[0];
					final int filled = capability.fill(output.copy(), FluidAction.EXECUTE);
					if (fluidStack.amount() - filled <= 0) {
						iterator.remove();
					}
				}
			}
			if (inputs.isEmpty()) {
				break;
			}
		}
		return inputs.isEmpty() ? null : inputs;
	}

	public void onContentsChanged() {
		this.isEmpty = null;
		this.notifyListeners();
	}

	@Override
	public int getTanks() {
		return this.fluidTanks.length;
	}

	@Override
	public FluidStack getFluidInTank(final int tank) {
		return this.fluidTanks[tank].getFluid();
	}

	@Override
	public void setFluidInTank(final int tank, final FluidStack fluidStack) {
		this.fluidTanks[tank].setFluid(fluidStack);
	}

	@Override
	public int getTankCapacity(final int tank) {
		return this.fluidTanks[tank].getCapacity();
	}

	@Override
	public boolean isFluidValid(final int tank, final FluidStack stack) {
		return this.fluidTanks[tank].isFluidValid(stack);
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (this.canCapabilityInput()) {
			return this.fillInternal(resource, action);
		}
		return 0;
	}

	public int fillInternal(final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty()) {
			return 0;
		}
		final FluidStack copied = resource.copy();
		FluidStackTransfer existingStorage = null;
		if (!this.allowFluidOverflow) {
			for (final FluidStackTransfer fluidTank : this.fluidTanks) {
				if (!fluidTank.getFluid().isEmpty() && FluidStack.isSameFluidSameComponents(fluidTank.getFluid(), resource)) {
					existingStorage = fluidTank;
					break;
				}
			}
		}
		if (existingStorage == null) {
			for (final FluidStackTransfer fluidTank : this.fluidTanks) {
				final int filled = fluidTank.fill(copied.copy(), action);
				if (filled > 0) {
					copied.shrink(filled);
					if (!this.allowFluidOverflow) {
						break;
					}
				}
				if (copied.isEmpty()) {
					break;
				}
			}
		} else {
			copied.shrink(existingStorage.fill(copied.copy(), action));
		}
		return resource.getAmount() - copied.getAmount();
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (this.canCapabilityOutput()) {
			return this.drainInternal(resource, action);
		}
		return FluidStack.EMPTY;
	}

	public FluidStack drainInternal(final FluidStack resource, final FluidAction action) {
		if (!resource.isEmpty()) {
			final FluidStack copied = resource.copy();
			for (final FluidStackTransfer transfer : this.fluidTanks) {
				final FluidStack candidate = copied.copy();
				copied.shrink(transfer.drain(candidate, action).getAmount());
				if (copied.isEmpty()) {
					break;
				}
			}
			copied.setAmount(resource.getAmount() - copied.getAmount());
			return copied;
		}
		return FluidStack.EMPTY;
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction simulate) {
		if (this.canCapabilityOutput()) {
			return this.drainInternal(maxDrain, simulate);
		}
		return FluidStack.EMPTY;
	}

	public FluidStack drainInternal(int amount, final FluidAction action) {
		if (amount == 0) {
			return FluidStack.EMPTY;
		}
		FluidStack totalDrained = null;
		for (final FluidStackTransfer fluidTank : this.fluidTanks) {
			if (totalDrained == null || totalDrained.isEmpty()) {
				totalDrained = fluidTank.drain(amount, action);
				if (totalDrained.isEmpty()) {
					totalDrained = null;
				} else {
					amount -= totalDrained.getAmount();
				}
			} else {
				final FluidStack copy = totalDrained.copy();
				copy.setAmount(amount);
				final FluidStack drain = fluidTank.drain(copy, action);
				totalDrained.grow(drain.getAmount());
				amount -= drain.getAmount();
			}
			if (amount <= 0) {
				break;
			}
		}
		return totalDrained == null ? FluidStack.EMPTY : totalDrained;
	}

	@Override
	public boolean supportsFill(final int tank) {
		return this.canCapabilityInput();
	}

	@Override
	public boolean supportsDrain(final int tank) {
		return this.canCapabilityOutput();
	}

	public boolean isEmpty() {
		if (this.isEmpty == null) {
			this.isEmpty = true;
			for (final FluidStackTransfer tank : this.fluidTanks) {
				if (!tank.getFluid().isEmpty()) {
					this.isEmpty = false;
					break;
				}
			}
		}
		return this.isEmpty;
	}
}
