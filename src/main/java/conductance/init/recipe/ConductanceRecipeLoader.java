package conductance.init.recipe;

import net.neoforged.neoforge.common.Tags;
import conductance.api.NCItems;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.NCTiers;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.tier.TieredItemType;
import conductance.api.util.ExtruderShape;
import conductance.Conductance;
import conductance.init.item.ProgramCircuitItem;

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
		event.shapeless(ProgramCircuitItem.makeStack(0), b -> b.add(NCItems.TIERED.get(TieredItemType.CIRCUIT, NCTiers.LV)));

		MaterialRecipes.add(event);
		TierRecipes.add(event);
		MaterialOreRecipes.add(event);
		MaterialRecycleRecipes.add(event);
		MachineRecipes.add(event);
		MultiBlockRecipes.add(event);
		CircuitRecipes.add(event);
		FuelAndPowerRecipes.add(event);
		ConductanceRecipeLoader.addExtruderShapes(event);
	}

	private static void addExtruderShapes(final RegisterRecipeEvent event) {
		event.shaped(NCItems.EMPTY_EXTRUDER_SHAPE, b -> b.pattern("aa", "aa").key('a', NCMaterialGenerationHandlers.PLATE, NCMaterials.STEEL));
		event.shaped(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.ROD), b -> b.pattern("a  ", " X ", "   ").key('a', NCItems.EMPTY_EXTRUDER_SHAPE));
		event.shaped(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.BOLT), b -> b.pattern(" a ", " X ", "   ").key('a', NCItems.EMPTY_EXTRUDER_SHAPE));
		event.shaped(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.GEAR), b -> b.pattern("  a", " X ", "   ").key('a', NCItems.EMPTY_EXTRUDER_SHAPE));
		event.shaped(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.GEAR_SMALL), b -> b.pattern("   ", "aX ", "   ").key('a', NCItems.EMPTY_EXTRUDER_SHAPE));
		event.shaped(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.RING), b -> b.pattern("   ", " Xa", "   ").key('a', NCItems.EMPTY_EXTRUDER_SHAPE));
		event.shaped(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.ROTOR), b -> b.pattern("   ", " X ", "a  ").key('a', NCItems.EMPTY_EXTRUDER_SHAPE));
		event.shaped(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.SCREW), b -> b.pattern("   ", " X ", " a ").key('a', NCItems.EMPTY_EXTRUDER_SHAPE));
	}

	private ConductanceRecipeLoader() {
	}
}
