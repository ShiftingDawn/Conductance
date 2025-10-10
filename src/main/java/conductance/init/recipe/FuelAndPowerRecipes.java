package conductance.init.recipe;

import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.recipe.event.RegisterRecipeEvent;

final class FuelAndPowerRecipes {

	public static void add(final RegisterRecipeEvent event) {
		event.create("steam", NCRecipeTypes.STEAM_TURBINE, b -> b
			.in(NCMaterials.STEAM, NCMaterialGenerationHandlers.GAS, 640).energyOut(NCTiers.LV.getVoltage()).duration(10)
		);
	}

	private FuelAndPowerRecipes() {
	}
}
