package conductance.init.machine;

import java.util.function.BiConsumer;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSortedSets;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.machine.RecipePerTickFailureAction;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;

public interface BoilerFakeRecipeCapabilityHolder extends RecipeCapabilityHolder {

	MachineRecipeCapabilityFluids getWaterTank();

	MachineRecipeCapabilityFluids getSteamTank();

	int getFuelForInput();

	void addInputs(BiConsumer<RecipeElementType<?>, Object> consumer);

	@Override
	default MachineRecipeType getRecipeType() {
		return null;
	}

	@Override
	default IntSortedSet getRecipePrograms() {
		return IntSortedSets.EMPTY_SET;
	}

	@Override
	default RecipePerTickFailureAction getPerTickFailureAction() {
		return RecipePerTickFailureAction.NOTHING;
	}
}
