package conductance.api.plugin;

import java.util.function.Consumer;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeTypeBuilder;

public interface RegisterRecipeTypeEvent extends IConductancePluginEvent {

	NCRecipeType register(String name, Consumer<RecipeTypeBuilder> builder);
}
