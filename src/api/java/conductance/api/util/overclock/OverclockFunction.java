package conductance.api.util.overclock;

import conductance.api.recipe.IRecipe;

public interface OverclockFunction {

	OverclockResult apply(IRecipe recipe, long recipeEnergy, long maxVoltage, int recipeProcessTime, int maxOverclocks);

}
