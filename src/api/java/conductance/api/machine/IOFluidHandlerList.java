package conductance.api.machine;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import lombok.Getter;
import lombok.Setter;

public class IOFluidHandlerList implements IFluidHandlerModifiable, ValueIOSerializable {

	private final IFluidHandler[] handlers;
	private final @Getter CapIO io;
	private @Setter Predicate<FluidStack> filter = fluid -> true;

	public IOFluidHandlerList(final IFluidHandler[] handlers, final CapIO io) {
		this.handlers = handlers;
		this.io = io;
	}

	public IOFluidHandlerList(final List<IFluidHandler> handlers, final CapIO io) {
		this(handlers.toArray(IFluidHandler[]::new), io);
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		final ValueOutput.ValueOutputList list = valueOutput.childrenList("handlers");
		for (int i = 0; i < this.handlers.length; ++i) {
			final ValueOutput entry = list.addChild();
			entry.putInt("i", i);
			if (this.handlers[i] instanceof final ValueIOSerializable serializable) {
				serializable.serialize(entry.child("data"));
			}
		}
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		valueInput.childrenList("handlers").ifPresent(list -> {
			for (final ValueInput entry : list) {
				final int i = entry.getIntOr("i", -1);
				if (i < 0 || i >= this.handlers.length) {
					continue;
				}
				if (this.handlers[i] instanceof final ValueIOSerializable serializable) {
					entry.child("data").ifPresent(serializable::deserialize);
				}
			}
		});
	}

	@Override
	public void setFluidInTank(final int tank, final FluidStack stack) {
		int index = 0;
		for (final IFluidHandler handler : this.handlers) {
			if (handler instanceof final IFluidHandlerModifiable modifiable) {
				if (tank - index < handler.getTanks()) {
					modifiable.setFluidInTank(tank - index, stack);
					return;
				}
			}
			index += handler.getTanks();
		}
	}

	@Override
	public int getTanks() {
		int tanks = 0;
		for (final IFluidHandler handler : this.handlers) {
			tanks += handler.getTanks();
		}
		return tanks;
	}

	@Override
	public FluidStack getFluidInTank(final int tank) {
		int index = 0;
		for (final IFluidHandler handler : this.handlers) {
			if (tank - index < handler.getTanks()) {
				return handler.getFluidInTank(tank - index);
			}
			index += handler.getTanks();
		}
		return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(final int tank) {
		int index = 0;
		for (final IFluidHandler handler : this.handlers) {
			if (tank - index < handler.getTanks()) {
				return handler.getTankCapacity(tank - index);
			}
			index += handler.getTanks();
		}
		return 0;
	}

	@Override
	public boolean isFluidValid(final int tank, final FluidStack stack) {
		if (!this.filter.test(stack)) {
			return false;
		}
		int index = 0;
		for (final IFluidHandler handler : this.handlers) {
			if (tank - index < handler.getTanks()) {
				return handler.isFluidValid(tank - index, stack);
			}
			index += handler.getTanks();
		}
		return false;
	}

	@Override
	public int fill(final FluidStack stack, final FluidAction action) {
		if (!this.io.isInput() || stack.isEmpty() || !this.filter.test(stack)) {
			return 0;
		}
		final FluidStack copied = stack.copy();
		for (final IFluidHandler handler : this.handlers) {
			final FluidStack candidate = copied.copy();
			copied.shrink(handler.fill(candidate, action));
			if (copied.isEmpty()) {
				break;
			}
		}
		return stack.getAmount() - copied.getAmount();
	}

	@Override
	public FluidStack drain(final FluidStack stack, final FluidAction action) {
		if (!this.io.isOutput() || stack.isEmpty() || !this.filter.test(stack)) {
			return FluidStack.EMPTY;
		}
		final FluidStack copy = stack.copy();
		for (final IFluidHandler handler : this.handlers) {
			final FluidStack copy2 = copy.copy();
			copy.shrink(handler.drain(copy2, action).getAmount());
			if (copy.isEmpty()) {
				break;
			}
		}
		copy.setAmount(stack.getAmount() - copy.getAmount());
		return copy;
	}

	@Override
	public FluidStack drain(final int maxAmount, final FluidAction action) {
		if (!this.io.isOutput() || maxAmount == 0) {
			return FluidStack.EMPTY;
		}
		int amount = maxAmount;
		FluidStack drainedStack = null;
		for (final IFluidHandler handler : this.handlers) {
			if (drainedStack == null || drainedStack.isEmpty()) {
				drainedStack = handler.drain(amount, action);
				if (drainedStack.isEmpty()) {
					drainedStack = null;
				} else {
					amount -= drainedStack.getAmount();
				}
			} else {
				final FluidStack copy = drainedStack.copy();
				copy.setAmount(amount);
				final FluidStack drain = handler.drain(copy, action);
				drainedStack.grow(drain.getAmount());
				amount -= drain.getAmount();
			}
			if (amount <= 0) {
				break;
			}
		}
		return drainedStack == null ? FluidStack.EMPTY : drainedStack;
	}

	@Override
	public boolean canFill(final int tank) {
		return this.io.isInput();
	}

	@Override
	public boolean canDrain(final int tank) {
		return this.io.isOutput();
	}
}
