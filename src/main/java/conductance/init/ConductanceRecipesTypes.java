package conductance.init;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;
import static conductance.api.NCRecipeTypes.PULVERIZER;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipesTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeTypeEvent event) {
		PULVERIZER = event.register("pulverizer", b -> b.setIO(1, 0, 1, 0));
	}

	private ConductanceRecipesTypes() {
	}
}
