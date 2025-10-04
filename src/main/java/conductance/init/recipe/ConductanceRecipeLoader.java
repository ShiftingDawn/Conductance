package conductance.init.recipe;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipeLoader {

	@EventListener(priority = -100)
	private static void addRecipes(final RegisterRecipeEvent event) {
		MaterialRecipes.add(event);
		TierRecipes.add(event);
		MaterialOreRecipes.add(event);
		MaterialRecycleRecipes.add(event);
		CircuitRecipes.add(event);
	}

	private ConductanceRecipeLoader() {
	}
}
