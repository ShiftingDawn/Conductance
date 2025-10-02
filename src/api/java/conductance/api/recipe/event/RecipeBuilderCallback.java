package conductance.api.recipe.event;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface RecipeBuilderCallback {

	void accept(MachineRecipeBuilder builder, BiConsumer<String, Consumer<MachineRecipeBuilder>> recipeBuilderFactory);
}
