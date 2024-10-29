package conductance.api.plugin;

import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;

public interface RecipeBuilderFactory {

	RecipeBuilder build(NCRecipeType recipeType, ResourceLocation recipeId);
}
