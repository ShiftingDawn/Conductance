package conductance.api.recipe.event;

import java.util.function.Consumer;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.recipe.MachineRecipeType;

public interface RegisterRecipeTypeEvent extends IConductancePluginEvent {

	MachineRecipeType register(String registryName, Consumer<RecipeTypeBuilder> builder);
}
