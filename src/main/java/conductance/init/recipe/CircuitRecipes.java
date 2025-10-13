package conductance.init.recipe;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import conductance.api.NCItems;
import conductance.api.NCTiers;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.tier.TieredItemType;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.FINE_WIRE;
import static conductance.api.NCMaterialGenerationHandlers.FOIL;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.ROD;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.STEEL;
import static conductance.api.NCMaterials.TIN;

final class CircuitRecipes {

	public static void add(final RegisterRecipeEvent event) {
		CircuitRecipes.addTier1(event);
	}

	private static void addTier1(final RegisterRecipeEvent event) {
		event.shaped(NCItems.WOOD_CIRCUIT_BOARD, b -> b.pattern("aaa", "bbb")
			.key('a', DUST, REDSTONE).key('b', ItemTags.WOODEN_SLABS));
		event.shaped(NCItems.WOOD_CIRCUIT_SUBSTRATE, b -> b.pattern("aaa", "bcb", "aaa")
			.key('a', FINE_WIRE, TIN).key('b', ROD, COPPER).key('c', NCItems.WOOD_CIRCUIT_BOARD));
		event.shaped(NCItems.DIODE, b -> b.pattern("aba", "cdc", "aea")
			.key('a', Items.PAPER).key('b', DUST, REDSTONE).key('c', FOIL, STEEL).key('d', NCItems.WOOD_CIRCUIT_BOARD).key('e', ROD, STEEL));
		event.shaped(NCItems.RESISTOR, b -> b.pattern("aba", "cdc", "aea")
			.key('a', Items.PAPER).key('b', DUST, REDSTONE).key('c', FOIL, COPPER).key('d', NCItems.WOOD_CIRCUIT_BOARD).key('e', ROD, COPPER));
		event.shaped(NCItems.TRANSISTOR, b -> b.pattern("aba", "cdc", "aea")
			.key('a', Items.PAPER).key('b', DUST, REDSTONE).key('c', FOIL, TIN).key('d', NCItems.WOOD_CIRCUIT_BOARD).key('e', ROD, TIN));
		event.shaped(NCItems.tiered(TieredItemType.CIRCUIT, NCTiers.LV, 1), b -> b.pattern("aba", "cdc", "efe")
			.key('a', NCItems.TRANSISTOR).key('b', PLATE, STEEL).key('c', NCItems.RESISTOR).key('d', NCItems.WOOD_CIRCUIT_SUBSTRATE).key('e', NCItems.DIODE).key('f', FINE_WIRE, COPPER));
	}

	private CircuitRecipes() {
	}
}
