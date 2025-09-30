package conductance.init;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.Conductance;
import static conductance.api.NCRecipeTypes.BENDING_MACHINE;
import static conductance.api.NCRecipeTypes.PULVERIZER;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipesTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeTypeEvent event) {
		BENDING_MACHINE = event.register("bending_machine", b -> b.setIO(2, 0, 2, 0));
		PULVERIZER = event.register("pulverizer", b -> b.setIO(1, 0, 1, 0));
	}

	@EventListener(priority = -100)
	private static void addTranslations(final AddTranslationEvent event) {
		event.add(BENDING_MACHINE, "Bending Machine");
		event.add(PULVERIZER, "Pulverizer");
	}

	private ConductanceRecipesTypes() {
	}
}
