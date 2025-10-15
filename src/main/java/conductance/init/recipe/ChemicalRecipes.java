package conductance.init.recipe;

import conductance.api.NCItems;
import conductance.api.NCRecipeTypes;
import conductance.api.recipe.event.RegisterRecipeEvent;
import static conductance.api.NCMaterialGenerationHandlers.LIQUID;
import static conductance.api.NCMaterials.GLUE;
import static conductance.api.NCTiers.LV;

final class ChemicalRecipes {

	public static void add(final RegisterRecipeEvent event) {
		event.create("glue", NCRecipeTypes.CENTRIFUGE, b -> b
			.in(NCItems.RESIN).out(NCItems.RESIN_PULP, 3).out(GLUE, LIQUID, 100).energyIn(LV).duration(60));
	}

	private ChemicalRecipes() {
	}
}
