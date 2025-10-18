package conductance.api.recipe.event;

import conductance.api.machine.BaseBlockEntity;
import conductance.api.recipe.MachineRecipe;

public interface RecipeTestCallback {

	enum When {
		BEFORE,
		AFTER
	}

	boolean test(BaseBlockEntity machine, MachineRecipe recipe);
}
