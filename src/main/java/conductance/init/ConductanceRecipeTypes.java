package conductance.init;

import conductance.api.plugin.RecipeTypeRegister;
import static conductance.api.NCRecipeTypes.BENDING_MACHINE;
import static conductance.api.NCRecipeTypes.COMPRESSOR;
import static conductance.api.NCRecipeTypes.CUTTING_MACHINE;
import static conductance.api.NCRecipeTypes.LATHE;
import static conductance.api.NCRecipeTypes.PULVERIZER;
import static conductance.api.NCRecipeTypes.STEAM_BOILER;
import static conductance.api.NCRecipeTypes.STEAM_TURBINE;

public final class ConductanceRecipeTypes {

	public static void init(final RecipeTypeRegister register) {
		STEAM_BOILER = register.register("steam_boiler").setIO(1, 1, 0, 1).setHidden().build();

		STEAM_TURBINE = register.register("steam_turbine").setIO(0, 1, 0, 0).setHidden().build();

		BENDING_MACHINE = register.register("bending_machine").setIO(2, 0, 2, 0).build();
		PULVERIZER = register.register("pulverizer").setIO(1, 0, 4, 0).build();
		CUTTING_MACHINE = register.register("cutting_machine").setIO(2, 1, 2, 0).build();
		LATHE = register.register("lathe").setIO(1, 0, 2, 0).build();
		COMPRESSOR = register.register("compressor").setIO(1, 0, 1, 0).build();
	}

	private ConductanceRecipeTypes() {
	}
}
