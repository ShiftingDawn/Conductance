package conductance.api.machine;

import java.util.Objects;
import java.util.function.IntUnaryOperator;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public class MachineFluidHandler implements IFluidHandlerModifiable, IChangeAware, ValueIOSerializable {

	private final Int2IntMap tankLimits = new Int2IntArrayMap();
	private NonNullList<FluidStack> stacks;
	@Getter
	@Setter
	@Nullable
	private Runnable changeListener = null;
	@Getter
	@Setter
	private FluidHandlerPredicate filter = (tank, stack) -> true;
	@Getter
	@Setter
	private boolean allowOverflow = false;

	public MachineFluidHandler(final NonNullList<FluidStack> stacks, final IntUnaryOperator capacityFactory) {
		this.stacks = stacks;
		for (int i = 0; i < this.stacks.size(); ++i) {
			this.tankLimits.put(i, capacityFactory.applyAsInt(i));
		}
	}

	public MachineFluidHandler(final NonNullList<FluidStack> stacks, final int capacity) {
		this(stacks, tank -> capacity);
	}

	public MachineFluidHandler(final int size, final int capacity) {
		this(NonNullList.withSize(size, FluidStack.EMPTY), capacity);
	}

	public MachineFluidHandler(final int capacity) {
		this(1, capacity);
	}

	protected void validateTankIndex(final int tank) {
		if (tank < 0 || tank >= this.stacks.size()) {
			throw new RuntimeException("Tank " + tank + " not in valid range - [0," + this.stacks.size() + ")");
		}
	}

	@Override
	public void setFluidInTank(final int tank, final FluidStack stack) {
		this.validateTankIndex(tank);
		this.stacks.set(tank, stack);
		this.onContentsChanged(tank);
	}

	@Override
	public int getTanks() {
		return this.stacks.size();
	}

	@Override
	public FluidStack getFluidInTank(final int tank) {
		this.validateTankIndex(tank);
		return this.stacks.get(tank);
	}

	@Override
	public int getTankCapacity(final int tank) {
		this.validateTankIndex(tank);
		return this.tankLimits.get(tank);
	}

	public int findFluid(final FluidStack fluid) {
		for (int i = 0; i < this.stacks.size(); ++i) {
			final FluidStack stack = this.stacks.get(i);
			if (!stack.isEmpty() && FluidStack.isSameFluidSameComponents(stack, fluid)) {
				return i;
			}
		}
		return -1;
	}

	public int fill(final int tank, final FluidStack stack, final FluidAction action) {
		if (stack.isEmpty()) {
			return 0;
		}
		if (!this.isFluidValid(tank, stack)) {
			return 0;
		}
		this.validateTankIndex(tank);
		final FluidStack existing = this.stacks.get(tank);
		if (existing.isEmpty()) {
			final int filled = Math.min(this.getTankCapacity(tank), stack.getAmount());
			if (action.execute()) {
				this.stacks.set(tank, stack.copyWithAmount(filled));
				this.onContentsChanged(tank);
			}
			return filled;
		}
		if (!FluidStack.isSameFluidSameComponents(existing, stack)) {
			return 0;
		}
		final int filled = Math.min(this.getTankCapacity(tank) - existing.getAmount(), stack.getAmount());
		if (action.execute()) {
			existing.grow(filled);
			this.onContentsChanged(tank);
		}
		return filled;
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty()) {
			return 0;
		}
		final int existingTank = this.findFluid(resource);
		int filled = 0;
		if (existingTank != -1) {
			filled = Math.min(this.getTankCapacity(existingTank) - this.getFluidInTank(existingTank).getAmount(), resource.getAmount());
			if (action.execute()) {
				this.getFluidInTank(existingTank).grow(filled);
				this.onContentsChanged(existingTank);
			}
			if (!this.allowOverflow || filled == resource.getAmount()) {
				return filled;
			}
		}
		for (int i = 0; i < this.stacks.size(); ++i) {
			final FluidStack current = this.getFluidInTank(i);
			if (current.isEmpty()) {
				if (this.isFluidValid(i, resource)) {
					final int amountFilled = Math.min(this.getTankCapacity(i), resource.getAmount() - filled);
					if (amountFilled > 0 && action.execute()) {
						this.setFluidInTank(i, resource.copyWithAmount(amountFilled));
						this.onContentsChanged(i);
					}
					filled += amountFilled;
				}
			} else if (FluidStack.isSameFluidSameComponents(current, resource)) {
				final int amountFilled = Math.min(this.getTankCapacity(i) - current.getAmount(), resource.getAmount() - filled);
				if (amountFilled > 0 && action.execute()) {
					current.grow(amountFilled);
					this.onContentsChanged(i);
				}
				filled += amountFilled;
			}
			if (filled == resource.getAmount()) {
				break;
			}
		}
		return filled;
	}

	public FluidStack drain(final int tank, final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty()) {
			return FluidStack.EMPTY;
		}
		this.validateTankIndex(tank);
		final FluidStack existing = this.getFluidInTank(tank);
		if (existing.isEmpty() || !FluidStack.isSameFluidSameComponents(existing, resource)) {
			return FluidStack.EMPTY;
		}
		final int drained = Math.min(existing.getAmount(), resource.getAmount());
		if (action.execute()) {
			if (drained == existing.getAmount()) {
				this.stacks.set(tank, FluidStack.EMPTY);
			} else {
				existing.shrink(drained);
			}
			this.onContentsChanged(tank);
		}
		return existing.copyWithAmount(drained);
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty()) {
			return FluidStack.EMPTY;
		}
		int drained = 0;
		for (int i = 0; i < this.stacks.size(); ++i) {
			final FluidStack current = this.getFluidInTank(i);
			if (FluidStack.isSameFluidSameComponents(current, resource)) {
				final int amountDrained = Math.min(current.getAmount(), resource.getAmount() - drained);
				if (amountDrained > 0 && action.execute()) {
					if (amountDrained == current.getAmount()) {
						this.setFluidInTank(i, FluidStack.EMPTY);
					} else {
						current.shrink(amountDrained);
					}
					this.onContentsChanged(i);
				}
				drained += amountDrained;
			}
			if (drained == resource.getAmount()) {
				break;
			}
		}
		return resource.copyWithAmount(drained);
	}

	public FluidStack drain(final int tank, final int maxDrain, final FluidAction action) {
		if (maxDrain <= 0) {
			return FluidStack.EMPTY;
		}
		this.validateTankIndex(tank);
		final FluidStack existing = this.getFluidInTank(tank);
		if (existing.isEmpty()) {
			return FluidStack.EMPTY;
		}
		final int drained = Math.min(existing.getAmount(), maxDrain);
		if (action.execute()) {
			if (drained == existing.getAmount()) {
				this.stacks.set(tank, FluidStack.EMPTY);
			} else {
				existing.shrink(drained);
			}
			this.onContentsChanged(tank);
		}
		return existing.copyWithAmount(drained);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		if (maxDrain <= 0) {
			return FluidStack.EMPTY;
		}
		FluidStack result = null;
		for (int i = 0; i < this.stacks.size(); ++i) {
			final FluidStack current = this.getFluidInTank(i);
			if (current.isEmpty()) {
				continue;
			}
			if (result == null) {
				result = current.copyWithAmount(Math.min(maxDrain, current.getAmount()));
				if (action.execute()) {
					if (result.getAmount() == current.getAmount()) {
						this.setFluidInTank(i, FluidStack.EMPTY);
					} else {
						current.shrink(maxDrain);
					}
					this.onContentsChanged(i);
				}
			} else if (FluidStack.isSameFluidSameComponents(result, current)) {
				final int amountDrained = Math.min(maxDrain - result.getAmount(), current.getAmount());
				result.grow(amountDrained);
				if (action.execute()) {
					if (amountDrained == current.getAmount()) {
						this.setFluidInTank(i, FluidStack.EMPTY);
					} else {
						current.shrink(amountDrained);
					}
					this.onContentsChanged(i);
				}
			}
			if (result.getAmount() == maxDrain) {
				break;
			}
		}
		return Objects.requireNonNullElse(result, FluidStack.EMPTY);
	}

	@Override
	public boolean isFluidValid(final int tank, final FluidStack stack) {
		return this.filter.test(tank, stack);
	}

	@Override
	public void serialize(final ValueOutput output) {
		final ValueOutput.TypedOutputList<FluidStackWithTank> fluidList = output.list("fluids", FluidStackWithTank.CODEC);
		for (int i = 0; i < this.stacks.size(); ++i) {
			final FluidStack stack = this.stacks.get(i);
			if (!stack.isEmpty()) {
				fluidList.add(new FluidStackWithTank(i, stack));
			}
		}
		output.putInt("tanks", this.stacks.size());
		output.putBoolean("allow_overflow", this.allowOverflow);
	}

	@Override
	public void deserialize(final ValueInput input) {
		this.stacks = NonNullList.withSize(input.getIntOr("tanks", this.stacks.size()), FluidStack.EMPTY);
		input.listOrEmpty("fluids", FluidStackWithTank.CODEC).forEach(fluid -> {
			if (fluid.isValidInContainer(this.stacks.size())) {
				this.stacks.set(fluid.tank(), fluid.stack());
			}
		});
		this.allowOverflow = input.getBooleanOr("allow_overflow", this.allowOverflow);
	}

	protected void onContentsChanged(final int tank) {
		if (this.changeListener != null) {
			this.changeListener.run();
		}
	}

	/**
	 * Creates a deep copy of this fluid handler and filter. Change listeners are NOT copied.
	 *
	 * @return an identical copy
	 */
	public MachineFluidHandler copy() {
		final NonNullList<FluidStack> copiedStacks = NonNullList.withSize(this.stacks.size(), FluidStack.EMPTY);
		for (int i = 0; i < this.stacks.size(); ++i) {
			copiedStacks.set(i, this.stacks.get(i).copy());
		}
		final MachineFluidHandler copied = new MachineFluidHandler(copiedStacks, this.tankLimits::get);
		copied.setFilter(this.filter);
		return copied;
	}
}
