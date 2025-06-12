package conductance.api.recipe.event;

import java.util.function.Consumer;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.recipe.NCRecipeType;
import conductance.api.recipe.RecipeTypeBuilder;

public interface RegisterRecipeTypeEvent extends IConductancePluginEvent {

	NCRecipeType register(String name, Consumer<RecipeTypeBuilder> builder);
}
