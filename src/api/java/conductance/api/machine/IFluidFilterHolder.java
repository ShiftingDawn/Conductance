package conductance.api.machine;

import java.util.function.Predicate;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IFluidFilterHolder {

	Predicate<FluidStack> getFluidFilter();
}
