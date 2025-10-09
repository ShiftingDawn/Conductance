package conductance.api.machine;

import java.util.List;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public interface RecipeCapabilityHolder {

	MachineRecipeType getRecipeType();

	List<MachineRecipeCapability<?>> getRecipeCapabilities(RecipeElementType<?> elementType, IO io);

	IntSortedSet getRecipePrograms();

	default RecipePerTickFailureAction getPerTickFailureAction() {
		return RecipePerTickFailureAction.REGRESS;
	}
}
