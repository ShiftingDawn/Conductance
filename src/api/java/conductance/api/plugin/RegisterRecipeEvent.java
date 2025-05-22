package conductance.api.plugin;

import java.util.function.Consumer;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;

public interface RegisterRecipeEvent extends IConductancePluginEvent {

	void create(NCRecipeType recipeType, String recipePath, Consumer<RecipeBuilder> callback);

	ResourceLocation id(String recipeType, String recipePath);

	ResourceLocation id(String recipePath);

	RecipeOutput getOutput();
}
