package conductance.init;

import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import conductance.api.CAPI;
import conductance.api.NCBlocks;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.NCRecipeModifiers;
import conductance.api.NCRecipeTypes;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlockWorkable;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.multi.MultiBlockControllerGuiSetup;
import conductance.api.machine.multi.StructurePredicate;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.tier.Tier;
import conductance.api.util.IO;
import conductance.api.util.TextHelper;
import conductance.Conductance;
import conductance.core.machine.MachineCore;
import conductance.init.machine.GenericGeneratorMachine;
import conductance.init.machine.GenericRecipeMachine;
import conductance.init.machine.GenericRecipeMachineGuiSetup;
import conductance.init.machine.GenericRecipeMultiBlockMachine;
import conductance.init.machine.MachineHullMachine;
import conductance.init.machine.MultiBlockEnergyHatchPartMachine;
import conductance.init.machine.MultiBlockFluidHatchPartMachine;
import conductance.init.machine.MultiBlockFluidHatchPartMachineGuiSetup;
import conductance.init.machine.MultiBlockItemBusPartMachine;
import conductance.init.machine.MultiBlockItemBusPartMachineGuiSetup;
import conductance.init.machine.MultiBlockOverclockedEnergyHatchPartMachine;
import conductance.init.machine.TransformerMachine;
import conductance.init.machine.boiler.LargeBoilerMachine;
import conductance.init.machine.boiler.SteamSolidFuelBoilerMachine;
import conductance.init.machine.boiler.SteamSolidFuelBoilerMachineGuiSetup;
import static conductance.api.NCMachines.ASSEMBLING_MACHINE;
import static conductance.api.NCMachines.BENDING_MACHINE;
import static conductance.api.NCMachines.CENTRIFUGE;
import static conductance.api.NCMachines.CIRCUIT_ASSEMBLER;
import static conductance.api.NCMachines.COMPRESSOR;
import static conductance.api.NCMachines.CRYSTALLIZING_ARC_FURNACE;
import static conductance.api.NCMachines.CUTTING_MACHINE;
import static conductance.api.NCMachines.DYNAMO_HATCHES;
import static conductance.api.NCMachines.ELECTRIC_BLAST_FURNACE;
import static conductance.api.NCMachines.ELECTROLYZER;
import static conductance.api.NCMachines.ENERGY_HATCHES;
import static conductance.api.NCMachines.EXTRACTOR;
import static conductance.api.NCMachines.EXTRUDER;
import static conductance.api.NCMachines.INPUT_BUSES;
import static conductance.api.NCMachines.INPUT_HATCHES;
import static conductance.api.NCMachines.LARGE_BRONZE_BOILER;
import static conductance.api.NCMachines.LATHE;
import static conductance.api.NCMachines.MACHINE_HULL;
import static conductance.api.NCMachines.OUTPUT_BUSES;
import static conductance.api.NCMachines.OUTPUT_HATCHES;
import static conductance.api.NCMachines.OVERCLOCKED_DYNAMO_HATCHES;
import static conductance.api.NCMachines.OVERLOCKED_ENERGY_HATCHES;
import static conductance.api.NCMachines.PULVERIZER;
import static conductance.api.NCMachines.STEAM_SOLID_FUEL_BOILER;
import static conductance.api.NCMachines.STEAM_TURBINES;
import static conductance.api.NCMachines.TRANSFORMER;
import static conductance.api.NCMachines.WIREMILL;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMachines {

	@EventListener(priority = -100)
	private static void init(final RegisterMachineEvent event) {
		MACHINE_HULL = CAPI.tiers().newMap(tier -> event.<MachineHullMachine>register(tier.getId().getPath() + "_machine_hull",
			(machineType, blockPos, blockState) -> new MachineHullMachine(machineType, blockPos, blockState, tier),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("machine_hull")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("machine_hull", tier).guiSetup(null)
		));
		ConductanceMachines.initGenerators(event);
		TRANSFORMER = CAPI.tiers().newMap(tier -> tier.getNextTier().isMax() ? null : event.<TransformerMachine>register(tier.getNextTier().getId().getPath() + "_transformer",
			(machineType, blockPos, blockState) -> new TransformerMachine(machineType, blockPos, blockState, tier, 1),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("transformer")), tier.getNextTier().getName())
			).rotationType(BlockRotationType.ALL).tieredModel("transformer", tier).guiSetup(null).blockFactory(MachineBlockWorkable::new).tooltip(() -> List.of(
				Component.translatable("machine.conductance.transformer.tooltip.0"),
				Component.translatable("machine.conductance.transformer.tooltip.1", 4, tier.getName(), TextHelper.ENERGY_FORMAT, 1, tier.getNextTier().getName(), TextHelper.ENERGY_FORMAT),
				Component.translatable("machine.conductance.transformer.tooltip.2", 1, tier.getNextTier().getName(), TextHelper.ENERGY_FORMAT, 4, tier.getName(), TextHelper.ENERGY_FORMAT)
			))
		));
		ConductanceMachines.initRecipeMachines(event);
		ConductanceMachines.initMultiBlocks(event);
		ConductanceMachines.initMultiParts(event);
	}

	private static void initGenerators(final RegisterMachineEvent event) {
		STEAM_SOLID_FUEL_BOILER = event.register("solid_fuel_steam_boiler", SteamSolidFuelBoilerMachine::new, b -> b
			.blockFactory(MachineBlockWorkable::new).sidedMachineModel(Conductance.id("block/casing/bronze")).guiSetup(new SteamSolidFuelBoilerMachineGuiSetup())
		);
		STEAM_TURBINES = ConductanceMachines.makeTieredGenericGeneratorMachine(event, "steam_turbine", NCRecipeTypes.STEAM_TURBINE);
	}

	private static void initRecipeMachines(final RegisterMachineEvent event) {
		BENDING_MACHINE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "bending_machine", NCRecipeTypes.BENDING_MACHINE);
		PULVERIZER = ConductanceMachines.makeTieredGenericRecipeMachine(event, "pulverizer", NCRecipeTypes.PULVERIZER);
		EXTRUDER = ConductanceMachines.makeTieredGenericRecipeMachine(event, "extruder", NCRecipeTypes.EXTRUDER);
		WIREMILL = ConductanceMachines.makeTieredGenericRecipeMachine(event, "wiremill", NCRecipeTypes.WIREMILL);
		LATHE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "lathe", NCRecipeTypes.LATHE);
		EXTRACTOR = ConductanceMachines.makeTieredGenericRecipeMachine(event, "extractor", NCRecipeTypes.EXTRACTOR);
		COMPRESSOR = ConductanceMachines.makeTieredGenericRecipeMachine(event, "compressor", NCRecipeTypes.COMPRESSOR);
		CUTTING_MACHINE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "cutting_machine", NCRecipeTypes.CUTTING_MACHINE);
		ASSEMBLING_MACHINE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "assembling_machine", NCRecipeTypes.ASSEMBLING_MACHINE);
		CENTRIFUGE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "centrifuge", NCRecipeTypes.CENTRIFUGE);
		ELECTROLYZER = ConductanceMachines.makeTieredGenericRecipeMachine(event, "electrolyzer", NCRecipeTypes.ELECTROLYZER);
		CIRCUIT_ASSEMBLER = ConductanceMachines.makeTieredGenericRecipeMachine(event, "circuit_assembler", NCRecipeTypes.CIRCUIT_ASSEMBLER);
	}

	private static void initMultiParts(final RegisterMachineEvent event) {
		INPUT_BUSES = CAPI.tiers().newMap(tier -> event.<MultiBlockItemBusPartMachine>register(tier.getId().getPath() + "_input_bus",
			(machineType, blockPos, blockState) -> new MultiBlockItemBusPartMachine(machineType, blockPos, blockState, IO.IN, Math.min(Mth.square(tier.getIndex() + 2), 100)),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("input_bus")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("input_bus", tier).guiSetup(new MultiBlockItemBusPartMachineGuiSetup()).blockFactory(MachineBlockWorkable::new)
		));
		OUTPUT_BUSES = CAPI.tiers().newMap(tier -> event.<MultiBlockItemBusPartMachine>register(tier.getId().getPath() + "_output_bus",
			(machineType, blockPos, blockState) -> new MultiBlockItemBusPartMachine(machineType, blockPos, blockState, IO.OUT, Math.min(Mth.square(tier.getIndex() + 2), 100)),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("output_bus")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("output_bus", tier).guiSetup(new MultiBlockItemBusPartMachineGuiSetup()).blockFactory(MachineBlockWorkable::new)
		));
		INPUT_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockFluidHatchPartMachine>register(tier.getId().getPath() + "_input_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockFluidHatchPartMachine(machineType, blockPos, blockState, IO.IN, 1, (int) Math.pow(2, 3 + tier.getIndex()) * CAPI.BUCKET),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("input_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("input_hatch", tier).guiSetup(new MultiBlockFluidHatchPartMachineGuiSetup()).blockFactory(MachineBlockWorkable::new)
		));
		OUTPUT_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockFluidHatchPartMachine>register(tier.getId().getPath() + "_output_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockFluidHatchPartMachine(machineType, blockPos, blockState, IO.OUT, 1, (int) Math.pow(2, 3 + tier.getIndex()) * CAPI.BUCKET),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("output_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("output_hatch", tier).guiSetup(new MultiBlockFluidHatchPartMachineGuiSetup()).blockFactory(MachineBlockWorkable::new)
		));
		ENERGY_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockEnergyHatchPartMachine>register(tier.getId().getPath() + "_energy_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockEnergyHatchPartMachine(machineType, blockPos, blockState, IO.IN, tier),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("energy_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("energy_hatch", tier).guiSetup(null)
		));
		DYNAMO_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockEnergyHatchPartMachine>register(tier.getId().getPath() + "_dynamo_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockEnergyHatchPartMachine(machineType, blockPos, blockState, IO.OUT, tier),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("dynamo_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("dynamo_hatch", tier).guiSetup(null)
		));
		OVERLOCKED_ENERGY_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockOverclockedEnergyHatchPartMachine>register(tier.getId().getPath() + "_overclocked_energy_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockOverclockedEnergyHatchPartMachine(machineType, blockPos, blockState, IO.IN, tier),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("overclocked_energy_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("overclocked_energy_hatch", tier).guiSetup(null)
		));
		OVERCLOCKED_DYNAMO_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockOverclockedEnergyHatchPartMachine>register(tier.getId().getPath() + "_overclocked_dynamo_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockOverclockedEnergyHatchPartMachine(machineType, blockPos, blockState, IO.OUT, tier),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("overclocked_dynamo_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("overclocked_dynamo_hatch", tier).guiSetup(null)
		));
	}

	private static void initMultiBlocks(final RegisterMachineEvent event) {
		LARGE_BRONZE_BOILER = event.multi("large_bronze_boiler", LargeBoilerMachine::new, b -> b
			.structure('x', c -> c
				.slice("aaa", "aaa", "aaa")
				.slice("aaa", "a a", "aaa")
				.slice("axa", "a a", "aaa")
				.slice("bbb", "bbb", "bbb")
				.key('a', StructurePredicate.isBlock(NCBlocks.CASING_BRONZE).or(
					StructurePredicate.isCapability(NCMultiBlockPartCapabilities.FLUIDS_IN).exact(1),
					StructurePredicate.isCapability(NCMultiBlockPartCapabilities.FLUIDS_OUT).exact(1),
					StructurePredicate.isCapability(NCMultiBlockPartCapabilities.ITEMS_IN).exact(1)
				))
				.key('b', StructurePredicate.isBlock(NCBlocks.CASING_BRONZE_FIREBOX))
			)
			.recipeModifier(NCRecipeModifiers.LARGE_BOILER)
			.guiSetup(new MultiBlockControllerGuiSetup(GuiTheme.THEME_BRONZE))
			.simpleModel(Conductance.id("block/casing/bronze"))
			.casingAppearance(() -> NCBlocks.CASING_BRONZE.value().defaultBlockState())
			.blockFactory(MachineBlockWorkable::new)
		);
		ELECTRIC_BLAST_FURNACE = event.multi("electric_blast_furnace", GenericRecipeMultiBlockMachine::new, b -> b
			.structure('x', c -> c
				.slice("aaa", "aaa", "aaa")
				.slice("bbb", "b b", "bbb")
				.slice("bbb", "b b", "bbb")
				.slice("axa", "aaa", "aaa")
				.key('a', StructurePredicate.isBlock(NCBlocks.CASING_INVAR).or(StructurePredicate.autoCapabilities(NCRecipeTypes.ELECTRIC_BLAST_FURNACE, true)))
				.key('b', StructurePredicate.isCoil())
			)
			.recipeType(NCRecipeTypes.ELECTRIC_BLAST_FURNACE)
			.simpleModel(Conductance.id("block/casing/invar"))
			.casingAppearance(() -> NCBlocks.CASING_INVAR.value().defaultBlockState())
			.blockFactory(MachineBlockWorkable::new)
			.rotationType(BlockRotationType.EXTENDED)
		);
		CRYSTALLIZING_ARC_FURNACE = event.multi("crystallizing_arc_furnace", GenericRecipeMultiBlockMachine::new, b -> b
			.structure('x', c -> c
				.slice("aaa", "aaa", "aaa")
				.slice("aaa", "a a", "aaa")
				.slice("axa", "a a", "aaa")
				.slice("aaa", "a a", "aaa")
				.key('a', StructurePredicate.isBlock(NCBlocks.CASING_STEEL).or(StructurePredicate.autoCapabilities(NCRecipeTypes.CRYSTALLIZING_ARC_FURNACE, true)))
			)
			.recipeType(NCRecipeTypes.CRYSTALLIZING_ARC_FURNACE)
			.simpleModel(Conductance.id("block/casing/steel"))
			.casingAppearance(() -> NCBlocks.CASING_STEEL.value().defaultBlockState())
			.blockFactory(MachineBlockWorkable::new)
			.rotationType(BlockRotationType.EXTENDED)
		);
	}

	private static Map<Tier, MachineType<?>> makeTieredGenericRecipeMachine(final RegisterMachineEvent event, final String name, final MachineRecipeType recipeType) {
		return CAPI.tiers().newMap(tier -> event.<GenericRecipeMachine>register(tier.getId().getPath() + "_" + name,
			(machineType, blockPos, blockState) -> new GenericRecipeMachine(machineType, tier, blockPos, blockState),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id(name)), tier.getName())
			).recipeType(recipeType).tieredModel(name, tier).guiSetup(new GenericRecipeMachineGuiSetup()).blockFactory(MachineBlockWorkable::new)
		));
	}

	private static Map<Tier, MachineType<?>> makeTieredGenericGeneratorMachine(final RegisterMachineEvent event, final String name, final MachineRecipeType recipeType) {
		return CAPI.tiers().newMap(tier -> event.<GenericRecipeMachine>register(tier.getId().getPath() + "_" + name,
			(machineType, blockPos, blockState) -> new GenericGeneratorMachine(machineType, tier, blockPos, blockState),
			b -> b.customName(ignored ->
					Component.translatable(Util.makeDescriptionId("machine", Conductance.id(name)), tier.getName())
				).recipeType(recipeType).recipeModifier(NCRecipeModifiers.steamTurbine(tier))
				.rotationType(BlockRotationType.ALL).tieredModel(name, tier).guiSetup(new GenericRecipeMachineGuiSetup()).blockFactory(MachineBlockWorkable::new)
		));
	}

	@EventListener(priority = -100)
	private static void addMachineModels(final AddRuntimeModelEvent event) {
		MachineCore.generateModels(event);
	}

	private ConductanceMachines() {
	}
}
