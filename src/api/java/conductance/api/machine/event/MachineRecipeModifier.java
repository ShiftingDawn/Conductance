package conductance.api.machine.event;

import conductance.api.recipe.MachineRecipe;

public interface MachineRecipeModifier {

	MachineRecipe modifyRecipe(MachineRecipe input);
}
