package conductance.init;

import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeElementTypes;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RecipeBuilderCallback;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;
import static conductance.api.NCRecipeTypes.BENDING_MACHINE;
import static conductance.api.NCRecipeTypes.COMPRESSOR;
import static conductance.api.NCRecipeTypes.CUTTING_MACHINE;
import static conductance.api.NCRecipeTypes.EXTRACTOR;
import static conductance.api.NCRecipeTypes.EXTRUDER;
import static conductance.api.NCRecipeTypes.LATHE;
import static conductance.api.NCRecipeTypes.PULVERIZER;
import static conductance.api.NCRecipeTypes.STEAM_TURBINE;
import static conductance.api.NCRecipeTypes.WIREMILL;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipesTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterRecipeTypeEvent event) {
		STEAM_TURBINE = event.register("steam_turbine", b -> b.setIO(0, 1, 0, 0));

		BENDING_MACHINE = event.register("bending_machine", b -> b.setIO(2, 0, 2, 0));
		PULVERIZER = event.register("pulverizer", b -> b.setIO(1, 0, 1, 0));
		EXTRUDER = event.register("extruder", b -> b.setIO(2, 0, 1, 0));
		WIREMILL = event.register("wiremill", b -> b.setIO(2, 0, 2, 0));
		LATHE = event.register("lathe", b -> b.setIO(1, 0, 2, 0));
		EXTRACTOR = event.register("extractor", b -> b.setIO(1, 0, 1, 1));
		COMPRESSOR = event.register("compressor", b -> b.setIO(1, 0, 1, 0));
		CUTTING_MACHINE = event.register("cutting_machine", b -> b.setIO(1, 1, 1, 0).recipeBuilderCallback(ConductanceRecipesTypes.CUTTING_MACHINE_CALLBACK));
	}

	private static final RecipeBuilderCallback CUTTING_MACHINE_CALLBACK = (recipeId, builder, recipeBuilderFactory) -> {
		if (builder.getInputs().containsKey(NCRecipeElementTypes.FLUID)) {
			return;
		}
		int time = builder.getDuration();
		recipeBuilderFactory.register(recipeId.withSuffix("_using_distilled_water"), copy ->
			copy.in(NCMaterials.DISTILLED_WATER, NCMaterialGenerationHandlers.LIQUID, time * 2).duration(time * 2));
		recipeBuilderFactory.register(recipeId.withSuffix("_using_water"), copy ->
			copy.in(NCMaterials.WATER, NCMaterialGenerationHandlers.LIQUID, time * 8).duration(time * 4));
		builder.in(NCMaterials.LUBRICANT, NCMaterialGenerationHandlers.LIQUID, time);
	};

	private ConductanceRecipesTypes() {
	}
}
