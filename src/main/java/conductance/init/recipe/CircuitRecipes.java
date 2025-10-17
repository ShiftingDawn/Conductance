package conductance.init.recipe;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import conductance.api.NCItems;
import conductance.api.NCRecipeTypes;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.tier.TieredItemType;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.FINE_WIRE;
import static conductance.api.NCMaterialGenerationHandlers.FOIL;
import static conductance.api.NCMaterialGenerationHandlers.LIQUID;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.ROD;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.RED_ALLOY;
import static conductance.api.NCMaterials.STEEL;
import static conductance.api.NCMaterials.TIN;
import static conductance.api.NCTiers.LV;

final class CircuitRecipes {

	public static void add(final RegisterRecipeEvent event) {
		CircuitRecipes.addTier1(event);
		CircuitRecipes.addTier2(event);
		CircuitRecipes.addWafers(event);
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
		event.shaped(NCItems.tiered(TieredItemType.CIRCUIT, LV, 1), b -> b.pattern("aba", "cdc", "efe")
			.key('a', NCItems.TRANSISTOR).key('b', PLATE, STEEL).key('c', NCItems.RESISTOR).key('d', NCItems.WOOD_CIRCUIT_SUBSTRATE).key('e', NCItems.DIODE).key('f', FINE_WIRE, COPPER));

		event.create(NCItems.tiered(TieredItemType.CIRCUIT, LV, 2), NCRecipeTypes.CIRCUIT_ASSEMBLER, b -> b.in(TIN, LIQUID, 144)
			.in(NCItems.TRANSISTOR, 2).in(NCItems.RESISTOR, 2).in(NCItems.DIODE, 2).in(STEEL, PLATE).in(NCItems.WOOD_CIRCUIT_SUBSTRATE).in(COPPER, FINE_WIRE).duration(200).energyIn(LV));
		event.create(NCItems.tiered(TieredItemType.ADVANCED_CIRCUIT, LV, 1), NCRecipeTypes.CIRCUIT_ASSEMBLER, b -> b.duration(400).energyIn(LV).in(COPPER, LIQUID, 288)
			.in(NCItems.tiered(TieredItemType.CIRCUIT, LV, 3)).in(NCItems.CPU_CHIP).in(NCItems.RAM_CHIP, 2).in(RED_ALLOY, FINE_WIRE, 4).in(NCItems.DIODE, 3).in(NCItems.TRANSISTOR, 3));
	}

	private static void addTier2(final RegisterRecipeEvent event) {
	}

	private static void addWafers(final RegisterRecipeEvent event) {
		event.create(new ItemStack(NCItems.SILICON_WAFER, 30), NCRecipeTypes.CUTTING_MACHINE, b -> b.in(NCItems.SILICON_BOULE).duration(1200).energyIn(LV));
		event.create(new ItemStack(NCItems.CPU_CHIP, 8), NCRecipeTypes.CUTTING_MACHINE, b -> b.in(NCItems.CPU_WAFER).duration(1200).energyIn(LV));
		event.create(new ItemStack(NCItems.RAM_CHIP, 16), NCRecipeTypes.CUTTING_MACHINE, b -> b.in(NCItems.RAM_WAFER).duration(1200).energyIn(LV));
	}

	private CircuitRecipes() {
	}
}
