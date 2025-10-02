package conductance.api.machine;

import net.neoforged.neoforge.fluids.FluidStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TankAwareFluidHandler implements IFluidHandlerModifiable {

	private final @Getter IFluidHandlerModifiable handler;
	private final @Getter int tank;

	@Override
	public void setFluidInTank(final int i, final FluidStack stack) {
		this.handler.setFluidInTank(this.tank, stack);
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(final int i) {
		return this.handler.getFluidInTank(this.tank);
	}

	@Override
	public int getTankCapacity(final int i) {
		return this.handler.getTankCapacity(this.tank);
	}

	@Override
	public boolean isFluidValid(final int i, final FluidStack stack) {
		return this.handler.isFluidValid(this.tank, stack);
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty() || !this.isFluidValid(0, resource)) {
			return 0;
		}
		if (this.getFluidInTank(0).isEmpty()) {
			final int filled = Math.min(resource.getAmount(), this.getTankCapacity(0));
			if (action.execute()) {
				this.setFluidInTank(0, resource.copyWithAmount(filled));
			}
			return filled;
		}
		return 0;
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, this.getFluidInTank(0))) {
			return FluidStack.EMPTY;
		}
		final FluidStack current = this.getFluidInTank(0);
		final int drained = Math.min(current.getAmount(), resource.getAmount());
		if (action.execute()) {
			if (drained == current.getAmount()) {
				this.setFluidInTank(0, FluidStack.EMPTY);
			} else {
				this.setFluidInTank(0, current.copyWithAmount(current.getAmount() - drained));
			}
		}
		return current.copyWithAmount(drained);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		if (maxDrain == 0) {
			return FluidStack.EMPTY;
		}
		final FluidStack current = this.getFluidInTank(0);
		if (current.isEmpty()) {
			return FluidStack.EMPTY;
		}
		final int drained = Math.min(current.getAmount(), maxDrain);
		if (action.execute()) {
			if (drained == current.getAmount()) {
				this.setFluidInTank(0, FluidStack.EMPTY);
			} else {
				this.setFluidInTank(0, current.copyWithAmount(current.getAmount() - drained));
			}
		}
		return current.copyWithAmount(drained);
	}
}
