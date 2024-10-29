package conductance.api.machine;

import conductance.api.machine.recipe.NCRecipeType;

public interface MachineBuilder<T extends MetaBlockEntity<T>> {

	MachineBuilder<T> recipeType(NCRecipeType recipeType, NCRecipeType... moreTypes);

	MetaBlockEntityType<T> build();
}
