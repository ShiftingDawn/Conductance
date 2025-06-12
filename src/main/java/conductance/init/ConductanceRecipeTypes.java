package conductance.init;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;
import static conductance.api.NCRecipeTypes.BENDING_MACHINE;
import static conductance.api.NCRecipeTypes.COMPRESSOR;
import static conductance.api.NCRecipeTypes.CUTTING_MACHINE;
import static conductance.api.NCRecipeTypes.LATHE;
import static conductance.api.NCRecipeTypes.PULVERIZER;
import static conductance.api.NCRecipeTypes.STEAM_BOILER;
import static conductance.api.NCRecipeTypes.STEAM_TURBINE;
import static conductance.api.NCRecipeTypes.WIREMILL;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipeTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeTypeEvent event) {
		STEAM_BOILER = event.register("steam_boiler", builder -> builder.setIO(1, 1, 0, 1).setHidden());

		STEAM_TURBINE = event.register("steam_turbine", builder -> builder.setIO(0, 1, 0, 0).setHidden());

		WIREMILL = event.register("wiremill", builder -> builder.setIO(2, 0, 2, 0));
		BENDING_MACHINE = event.register("bending_machine", builder -> builder.setIO(2, 0, 2, 0));
		PULVERIZER = event.register("pulverizer", builder -> builder.setIO(1, 0, 4, 0));
		CUTTING_MACHINE = event.register("cutting_machine", builder -> builder.setIO(2, 1, 2, 0));
		LATHE = event.register("lathe", builder -> builder.setIO(1, 0, 2, 0));
		COMPRESSOR = event.register("compressor", builder -> builder.setIO(1, 0, 1, 0));
	}

	private ConductanceRecipeTypes() {
	}
}
