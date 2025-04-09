package conductance.api.machine.recipe;

import java.util.function.DoubleSupplier;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public record RecipeHolder(
		DoubleSupplier progressSupplier,
		IItemHandlerModifiable inputItems,
		IItemHandlerModifiable outputItems,
		IFluidHandler inputFluids,
		IFluidHandler outputFluids
) {

}
