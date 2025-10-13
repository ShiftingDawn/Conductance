package conductance.init.recipe;

import net.neoforged.neoforge.common.Tags;
import conductance.api.NCItems;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipeLoader {

	@EventListener(priority = -100)
	private static void addRecipes(final RegisterRecipeEvent event) {
		event.shaped(NCItems.HAMMER, b -> b
			.pattern("aa ", "abc", "aa ").key('a', NCMaterialGenerationHandlers.INGOT, NCMaterials.COPPER).key('b', NCMaterialGenerationHandlers.INGOT, NCMaterials.IRON)
			.key('c', NCMaterialGenerationHandlers.ROD, NCMaterials.WOOD));
		event.shaped(NCItems.WRENCH, b -> b
			.pattern("aHa", "aaa", " a ").key('a', NCMaterialGenerationHandlers.PLATE, NCMaterials.COPPER));
		event.shaped(NCItems.WIRE_CUTTERS, b -> b
			.pattern("aHa", "bab", "cWc").key('a', NCMaterialGenerationHandlers.PLATE, NCMaterials.COPPER).key('b', Tags.Items.STRINGS).key('c', NCMaterialGenerationHandlers.ROD, NCMaterials.WOOD));

		MaterialRecipes.add(event);
		TierRecipes.add(event);
		MaterialOreRecipes.add(event);
		MaterialRecycleRecipes.add(event);
		MachineRecipes.add(event);
		MultiBlockRecipes.add(event);
		CircuitRecipes.add(event);
		FuelAndPowerRecipes.add(event);
	}

	private ConductanceRecipeLoader() {
	}
}
