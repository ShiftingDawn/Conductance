package conductance.api.machine.recipe;

import java.util.List;
import javax.annotation.Nonnull;
import com.google.common.collect.Table;
import conductance.api.machine.capability.MachineRecipeCapability;
import conductance.api.util.IOMode;

public interface RecipeCapabilityHolder {

	default boolean hasRecipeCapabilities() {
		return !this.getRecipeCapabilities().isEmpty() && !this.getRecipeCapabilities().isEmpty();
	}

	@Nonnull
	Table<IOMode, IRecipeElementType<?>, List<MachineRecipeCapability<?>>> getRecipeCapabilities();

	default int getChanceTier() {
		return 0;
	}
}
