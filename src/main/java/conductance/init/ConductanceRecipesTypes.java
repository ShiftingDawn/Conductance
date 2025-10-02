package conductance.init;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;
import static conductance.api.NCRecipeTypes.BENDING_MACHINE;
import static conductance.api.NCRecipeTypes.COMPRESSOR;
import static conductance.api.NCRecipeTypes.CUTTING_MACHINE;
import static conductance.api.NCRecipeTypes.EXTRACTOR;
import static conductance.api.NCRecipeTypes.EXTRUDER;
import static conductance.api.NCRecipeTypes.LATHE;
import static conductance.api.NCRecipeTypes.PULVERIZER;
import static conductance.api.NCRecipeTypes.WIREMILL;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipesTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeTypeEvent event) {
		BENDING_MACHINE = event.register("bending_machine", b -> b.setIO(2, 0, 2, 0));
		PULVERIZER = event.register("pulverizer", b -> b.setIO(1, 0, 1, 0));
		EXTRUDER = event.register("extruder", b -> b.setIO(2, 0, 1, 0));
		WIREMILL = event.register("wiremill", b -> b.setIO(2, 0, 2, 0));
		LATHE = event.register("lathe", b -> b.setIO(1, 0, 2, 0));
		EXTRACTOR = event.register("extractor", b -> b.setIO(1, 0, 1, 1));
		COMPRESSOR = event.register("compressor", b -> b.setIO(1, 0, 1, 0));
		CUTTING_MACHINE = event.register("cutting_machine", b -> b.setIO(1, 1, 1, 0));
	}

	private ConductanceRecipesTypes() {
	}
}
