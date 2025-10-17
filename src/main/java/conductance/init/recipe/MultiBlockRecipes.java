package conductance.init.recipe;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import conductance.api.CAPI;
import conductance.api.NCBlocks;
import conductance.api.NCMachines;
import conductance.api.NCMaterials;
import conductance.api.NCTiers;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.tier.TieredItemType;
import static conductance.api.NCItems.tiered;
import static conductance.api.NCMaterialGenerationHandlers.FRAME_BOX;
import static conductance.api.NCMaterialGenerationHandlers.LIQUID;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_1X;

final class MultiBlockRecipes {

	public static void add(final RegisterRecipeEvent event) {
		MultiBlockRecipes.addEnergyHatches(event);
		event.shaped(NCBlocks.CASING_BRONZE, b -> b.pattern("aWa", "aba", "aHa").key('a', PLATE, NCMaterials.BRONZE).key('b', FRAME_BOX, NCMaterials.BRONZE));
		event.shaped(NCBlocks.CASING_STEEL, b -> b.pattern("aWa", "aba", "aHa").key('a', PLATE, NCMaterials.STEEL).key('b', FRAME_BOX, NCMaterials.STEEL));
		event.shaped(NCBlocks.CASING_INVAR, b -> b.pattern("aWa", "aba", "aHa").key('a', PLATE, NCMaterials.INVAR).key('b', FRAME_BOX, NCMaterials.INVAR));
		event.shaped(NCBlocks.CASING_ALUMINIUM, b -> b.pattern("aWa", "aba", "aHa").key('a', PLATE, NCMaterials.ALUMINIUM).key('b', FRAME_BOX, NCMaterials.ALUMINIUM));

		event.shaped(NCMachines.LARGE_BRONZE_BOILER.getItem(), b -> b
			.pattern("aba", "bcb", "aba").key('a', WIRE_1X, NCMaterials.GOLD).key('b', tiered(TieredItemType.ADVANCED_CIRCUIT, NCTiers.LV, 1)).key('c', NCBlocks.CASING_BRONZE_FIREBOX));
		event.shaped(NCMachines.ELECTRIC_BLAST_FURNACE.getItem(), b -> b
			.pattern("aaa", "bcb", "dbd").key('a', Blocks.BLAST_FURNACE).key('b', tiered(TieredItemType.ADVANCED_CIRCUIT, NCTiers.LV, 1)).key('c', NCBlocks.CASING_INVAR).key('d', WIRE_1X, NCMaterials.COPPER));
		event.shaped(NCMachines.CRYSTALLIZING_ARC_FURNACE.getItem(), b -> b
			.pattern("aba", "cdc", "aba").key('a', tiered(TieredItemType.CIRCUIT, NCTiers.LV, 1)).key('b', Items.CAULDRON).key('c', tiered(TieredItemType.ROBOT_ARM, NCTiers.LV, 1)).key('d', NCBlocks.CASING_STEEL));
	}

	private static void addEnergyHatches(final RegisterRecipeEvent event) {
		event.shaped(NCMachines.ENERGY_HATCHES.get(NCTiers.LV).getItem(), b -> b
			.pattern("abc", "def", "abc").key('a', NCTiers.LV.getComponentMap().getMachineWireItem()).key('b', tiered(TieredItemType.INDUCTION_COIL, NCTiers.LV, 1))
			.key('c', CAPI.materials().getItemTag(NCMaterials.LUBRICANT, LIQUID)).key('d', tiered(TieredItemType.ADVANCED_CIRCUIT, NCTiers.LV, 1)).key('e', NCMachines.MACHINE_HULL.get(NCTiers.LV).getItem())
			.key('f', tiered(TieredItemType.ELECTRIC_PUMP, NCTiers.LV, 1)));
	}

	private MultiBlockRecipes() {
	}
}
