package conductance.init.recipe;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import conductance.api.CAPI;
import conductance.api.NCItems;
import conductance.api.NCMachines;
import conductance.api.NCMaterials;
import conductance.api.NCRecipeTypes;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;
import conductance.api.tier.TieredItemType;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.STORAGE_BLOCK;

final class MachineRecipes {

	public static void add(final RegisterRecipeEvent event) {
		event.shaped(NCMachines.STEAM_SOLID_FUEL_BOILER.getItem(), b -> b
			.pattern("aaa", "aWa", "bcb").key('a', PLATE, NCMaterials.BRONZE).key('b', STORAGE_BLOCK, NCMaterials.BRICK).key('c', Blocks.BLAST_FURNACE));
		MachineRecipes.addTieredMachines(event);
	}

	@SuppressWarnings("DataFlowIssue")
	private static void addTieredMachines(final RegisterRecipeEvent event) {
		for (final Tier tier : CAPI.tiers().getTiers()) {
			final TieredComponentMap map = tier.getComponentMap();
			final Supplier<? extends Item> hull = NCMachines.MACHINE_HULL.get(tier).getItem();
			final Holder<Item> motor = NCItems.TIERED.get(TieredItemType.ELECTRIC_MOTOR, tier);
			final Holder<Item> conveyor = NCItems.TIERED.get(TieredItemType.CONVEYOR_MODULE, tier);
			final Holder<Item> piston = NCItems.TIERED.get(TieredItemType.ELECTRIC_PISTON, tier);
			final Holder<Item> arm = NCItems.TIERED.get(TieredItemType.ROBOT_ARM, tier);
			final Holder<Item> pump = NCItems.TIERED.get(TieredItemType.ELECTRIC_PUMP, tier);
			final Holder<Item> circuit = NCItems.TIERED.get(TieredItemType.CIRCUIT, tier);

			event.shaped(NCMachines.STEAM_TURBINES.get(tier).getItem(), b -> b
				.pattern("aba", "cdc", "efe").key('a', map.getMachinePlateItem()).key('b', circuit).key('c', map.getMachineRotorItem()).key('d', hull).key('e', motor).key('f', map.getMachineWireItem()));

			event.shaped(NCMachines.BENDING_MACHINE.get(tier).getItem(), b -> b
				.pattern("aba", "cdc", "efe").key('a', piston).key('b', map.getMachinePlateItem()).key('c', circuit).key('d', hull).key('e', motor).key('f', map.getMachineWireItem()));
			event.shaped(NCMachines.PULVERIZER.get(tier).getItem(), b -> b
				.pattern("abc", "dde", "ffd").key('a', piston).key('b', motor).key('c', map.getMachineCuttingPartItem()).key('d', map.getMachineWireItem()).key('e', hull).key('f', circuit));
			event.shaped(NCMachines.EXTRUDER.get(tier).getItem(), b -> b
				.pattern("aab", "cda", "aab").key('a', map.getMachineHeatingWireItem()).key('b', circuit).key('c', piston).key('d', hull));
			event.shaped(NCMachines.WIREMILL.get(tier).getItem(), b -> b
				.pattern("aba", "cdc", "aba").key('a', motor).key('b', map.getMachineWireItem()).key('c', circuit).key('d', hull));
			event.shaped(NCMachines.LATHE.get(tier).getItem(), b -> b
				.pattern("aba", "cde", "baf").key('a', map.getMachineWireItem()).key('b', circuit).key('c', motor).key('d', hull).key('e', map.getMachineCuttingPartItem()).key('f', piston));
			event.shaped(NCMachines.EXTRACTOR.get(tier).getItem(), b -> b
				.pattern("aba", "cde", "fbf").key('a', Tags.Items.GLASS_BLOCKS_COLORLESS).key('b', circuit).key('c', piston).key('d', hull).key('e', pump).key('f', map.getMachineWireItem()));
			event.shaped(NCMachines.COMPRESSOR.get(tier).getItem(), b -> b
				.pattern("aba", "cdc", "aba").key('a', map.getMachineWireItem()).key('b', circuit).key('c', piston).key('d', hull));
			event.shaped(NCMachines.CUTTING_MACHINE.get(tier).getItem(), b -> b
				.pattern("abc", "def", "bag").key('a', map.getMachineWireItem()).key('b', circuit).key('c', Tags.Items.GLASS_BLOCKS_COLORLESS).key('d', conveyor).key('e', hull)
				.key('f', map.getMachineCuttingPartItem()).key('g', motor));
			event.shaped(NCMachines.ASSEMBLING_MACHINE.get(tier).getItem(), b -> b
				.pattern("aba", "cdc", "ebe").key('a', arm).key('b', circuit).key('c', conveyor).key('d', hull).key('e', map.getMachineWireItem()));
			event.shaped(NCMachines.CENTRIFUGE.get(tier).getItem(), b -> b
				.pattern("aba", "cdc", "aba").key('a', circuit).key('b', map.getMachineWireItem()).key('c', motor).key('d', hull));
			event.shaped(NCMachines.ELECTROLYZER.get(tier).getItem(), b -> b
				.pattern("aba", "aca", "ded").key('a', map.getElectricMotorWireItem()).key('b', Tags.Items.GLASS_BLOCKS_COLORLESS).key('c', hull).key('d', circuit).key('e', map.getMachineWireItem()));

			if (!tier.getNextTier().isMax()) {
				event.shaped(NCMachines.CIRCUIT_ASSEMBLER.get(tier).getItem(), b -> b
					.pattern("aba", "cdc", "ebe").key('a', arm).key('b', NCItems.TIERED.get(TieredItemType.CIRCUIT, tier.getNextTier())).key('c', conveyor).key('d', hull).key('e', map.getMachineWireItem()));
			}

			event.create(tier.getId().getPath() + "_input_bus", NCRecipeTypes.ASSEMBLING_MACHINE, b -> b
				.in(hull).in(Tags.Items.CHESTS_WOODEN).in(map.getPlasticFluid(), 100).out(NCMachines.INPUT_BUSES.get(tier).getItem()).program(1).energyIn(tier).duration(200));
			event.create(tier.getId().getPath() + "_output_bus", NCRecipeTypes.ASSEMBLING_MACHINE, b -> b
				.in(hull).in(Tags.Items.CHESTS_WOODEN).in(map.getPlasticFluid(), 100).out(NCMachines.OUTPUT_BUSES.get(tier).getItem()).program(2).energyIn(tier).duration(200));
			event.shapeless(tier.getId().getPath() + "_input_bus_from_output_bus", NCMachines.INPUT_BUSES.get(tier).getItem(), b -> b.add(NCMachines.OUTPUT_BUSES.get(tier).getItem()));
			event.shapeless(tier.getId().getPath() + "_output_bus_from_input_bus", NCMachines.OUTPUT_BUSES.get(tier).getItem(), b -> b.add(NCMachines.INPUT_BUSES.get(tier).getItem()));
			event.create(tier.getId().getPath() + "_input_hatch", NCRecipeTypes.ASSEMBLING_MACHINE, b -> b
				.in(hull).in(Tags.Items.GLASS_BLOCKS_COLORLESS).in(map.getPlasticFluid(), 100).out(NCMachines.INPUT_HATCHES.get(tier).getItem()).program(1).energyIn(tier).duration(200));
			event.create(tier.getId().getPath() + "_output_hatch", NCRecipeTypes.ASSEMBLING_MACHINE, b -> b
				.in(hull).in(Tags.Items.GLASS_BLOCKS_COLORLESS).in(map.getPlasticFluid(), 100).out(NCMachines.OUTPUT_HATCHES.get(tier).getItem()).program(2).energyIn(tier).duration(200));
			event.shapeless(tier.getId().getPath() + "_input_hatch_from_output_hatch", NCMachines.INPUT_HATCHES.get(tier).getItem(), b -> b.add(NCMachines.OUTPUT_HATCHES.get(tier).getItem()));
			event.shapeless(tier.getId().getPath() + "_output_hatch_from_input_hatch", NCMachines.OUTPUT_HATCHES.get(tier).getItem(), b -> b.add(NCMachines.INPUT_HATCHES.get(tier).getItem()));
		}
	}

	private MachineRecipes() {
	}
}
