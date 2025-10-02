package conductance.api.machine;

import net.neoforged.neoforge.fluids.FluidStack;

public interface IDelegatedFluidHandler extends IFluidHandlerModifiable {

	IFluidHandlerModifiable getRealFluidHandler();

	@Override
	default void setFluidInTank(final int tank, final FluidStack stack) {
		this.getRealFluidHandler().setFluidInTank(tank, stack);
	}

	@Override
	default int getTanks() {
		return this.getRealFluidHandler().getTanks();
	}

	@Override
	default FluidStack getFluidInTank(final int tank) {
		return this.getRealFluidHandler().getFluidInTank(tank);
	}

	@Override
	default int getTankCapacity(final int tank) {
		return this.getRealFluidHandler().getTankCapacity(tank);
	}

	@Override
	default boolean isFluidValid(final int tank, final FluidStack stack) {
		return this.getRealFluidHandler().isFluidValid(tank, FluidStack.EMPTY);
	}

	@Override
	default int fill(final FluidStack resource, final FluidAction action) {
		return this.getRealFluidHandler().fill(resource, action);
	}

	@Override
	default FluidStack drain(final FluidStack resource, final FluidAction action) {
		return this.getRealFluidHandler().drain(resource, action);
	}

	@Override
	default FluidStack drain(final int maxDrain, final FluidAction action) {
		return this.getRealFluidHandler().drain(maxDrain, action);
	}
}
