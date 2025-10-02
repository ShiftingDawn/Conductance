package conductance.api.recipe.event;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public interface RecipeBuilderCallback {

	interface RecipeRegister {

		void register(ResourceLocation recipeId, Consumer<MachineRecipeBuilder> builder);
	}

	void accept(ResourceLocation recipeId, MachineRecipeBuilder builder, RecipeRegister register);
}
