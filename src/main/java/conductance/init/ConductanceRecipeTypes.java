package conductance.init;

import conductance.api.NCRecipeTypes;
import conductance.api.plugin.RecipeTypeRegister;

public final class ConductanceRecipeTypes {

	public static void init(final RecipeTypeRegister register) {
		NCRecipeTypes.BENDER = register.register("bender")
				.setIO(2, 0, 2, 0)
				.build();
	}

	private ConductanceRecipeTypes() {
	}
}
