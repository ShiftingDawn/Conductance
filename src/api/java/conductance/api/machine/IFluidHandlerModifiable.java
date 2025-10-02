package conductance.api.machine;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public interface IFluidHandlerModifiable extends IFluidHandler {

	void setFluidInTank(int tank, FluidStack stack);

}
