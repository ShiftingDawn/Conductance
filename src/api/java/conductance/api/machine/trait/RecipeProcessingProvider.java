package conductance.api.machine.trait;

import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.NCRecipeType;

public interface RecipeProcessingProvider {

	NCRecipeType[] getRecipeTypes();

	int getActiveRecipeType();

	RecipeProcessor getProcessor();

	boolean consumeInputs(IRecipe recipe, boolean simulate);

	boolean produceOutputs(IRecipe recipe, boolean simulate);
}
