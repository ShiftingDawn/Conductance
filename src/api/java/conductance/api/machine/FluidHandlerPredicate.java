package conductance.api.machine;

import net.neoforged.neoforge.fluids.FluidStack;

public interface FluidHandlerPredicate {

	boolean test(int tank, FluidStack stack);
}
