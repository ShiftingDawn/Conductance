package conductance.init;

import conductance.api.NCRecipeTypes;
import conductance.api.plugin.RecipeTypeRegister;

public final class ConductanceRecipeTypes {

	public static void init(final RecipeTypeRegister register) {
		NCRecipeTypes.STEAM_BOILER = register.register("steam_boiler").setIO(1, 1, 0, 1).build();

		NCRecipeTypes.STEAM_TURBINE = register.register("steam_turbine").setIO(0, 1, 0, 0).build();

		NCRecipeTypes.BENDER = register.register("bender").setIO(2, 0, 2, 0).build();
	}

	private ConductanceRecipeTypes() {
	}
}
