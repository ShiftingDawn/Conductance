package conductance.api.recipe.event;

import conductance.api.machine.MachineBlockEntity;
import conductance.api.recipe.MachineRecipe;

public interface RecipeTestCallback {

	enum When {
		BEFORE,
		AFTER
	}

	boolean test(MachineBlockEntity<?> machine, MachineRecipe recipe);
}
