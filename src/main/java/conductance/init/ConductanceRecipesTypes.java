package conductance.init;

import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.CapIO;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.RecipeDataTokens;
import conductance.api.recipe.event.RecipeBuilderCallback;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;
import conductance.Conductance;
import static conductance.api.NCRecipeTypes.ASSEMBLING_MACHINE;
import static conductance.api.NCRecipeTypes.BENDING_MACHINE;
import static conductance.api.NCRecipeTypes.CENTRIFUGE;
import static conductance.api.NCRecipeTypes.COMPRESSOR;
import static conductance.api.NCRecipeTypes.CUTTING_MACHINE;
import static conductance.api.NCRecipeTypes.ELECTRIC_BLAST_FURNACE;
import static conductance.api.NCRecipeTypes.ELECTROLYZER;
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
		STEAM_TURBINE = event.register("steam_turbine", b -> b.setIO(0, 1, 0, 0).setEnergyIO(CapIO.OUT).hidden());

		BENDING_MACHINE = event.register("bending_machine", b -> b.setIO(2, 0, 2, 0).setEnergyIO(CapIO.IN));
		PULVERIZER = event.register("pulverizer", b -> b.setIO(1, 0, 1, 0).setEnergyIO(CapIO.IN));
		EXTRUDER = event.register("extruder", b -> b.setIO(2, 0, 1, 0).setEnergyIO(CapIO.IN));
		WIREMILL = event.register("wiremill", b -> b.setIO(2, 0, 2, 0).setEnergyIO(CapIO.IN));
		LATHE = event.register("lathe", b -> b.setIO(1, 0, 2, 0).setEnergyIO(CapIO.IN));
		EXTRACTOR = event.register("extractor", b -> b.setIO(1, 0, 1, 1).setEnergyIO(CapIO.IN));
		COMPRESSOR = event.register("compressor", b -> b.setIO(1, 0, 1, 0).setEnergyIO(CapIO.IN));
		CUTTING_MACHINE = event.register("cutting_machine", b -> b.setIO(1, 1, 1, 0).setEnergyIO(CapIO.IN).recipeBuilderCallback(ConductanceRecipesTypes.CUTTING_MACHINE_CALLBACK));
		ASSEMBLING_MACHINE = event.register("assembling_machine", b -> b.setIO(9, 1, 1, 0).setEnergyIO(CapIO.IN));
		CENTRIFUGE = event.register("centrifuge", b -> b.setIO(2, 1, 6, 3).setEnergyIO(CapIO.IN));
		ELECTROLYZER = event.register("electrolyzer", b -> b.setIO(2, 1, 6, 3).setEnergyIO(CapIO.IN));

		ELECTRIC_BLAST_FURNACE = event.register("electric_blast_furnace", b -> b.setIO(3, 3, 1, 1).setEnergyIO(CapIO.IN)
			.data(RecipeDataTokens.BLAST_TEMP));
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
