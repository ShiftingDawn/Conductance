package conductance.api.plugin;

import conductance.api.machine.recipe.RecipeTypeBuilder;

public interface RecipeTypeRegister {

	RecipeTypeBuilder register(String name);
}
