package conductance.init;

import java.util.Map;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import conductance.api.CAPI;
import conductance.api.NCBlocks;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.NCRecipeTypes;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlockWorkable;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.multi.StructurePredicate;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.tier.Tier;
import conductance.api.util.IO;
import conductance.Conductance;
import conductance.core.machine.MachineCore;
import conductance.init.machine.GenericRecipeMachine;
import conductance.init.machine.GenericRecipeMachineGuiSetup;
import conductance.init.machine.LargeBoilerMachine;
import conductance.init.machine.MultiBlockControllerGuiSetup;
import conductance.init.machine.MultiBlockFluidHatchPartMachine;
import conductance.init.machine.MultiBlockFluidHatchPartMachineGuiSetup;
import conductance.init.machine.MultiBlockItemBusPartMachine;
import conductance.init.machine.MultiBlockItemBusPartMachineGuiSetup;
import conductance.init.machine.SteamSolidFuelBoilerMachine;
import conductance.init.machine.SteamSolidFuelBoilerMachineGuiSetup;
import static conductance.api.NCMachines.BENDING_MACHINE;
import static conductance.api.NCMachines.COMPRESSOR;
import static conductance.api.NCMachines.CUTTING_MACHINE;
import static conductance.api.NCMachines.EXTRACTOR;
import static conductance.api.NCMachines.EXTRUDER;
import static conductance.api.NCMachines.INPUT_BUSES;
import static conductance.api.NCMachines.INPUT_HATCHES;
import static conductance.api.NCMachines.LARGE_BRONZE_BOILER;
import static conductance.api.NCMachines.LATHE;
import static conductance.api.NCMachines.OUTPUT_BUSES;
import static conductance.api.NCMachines.OUTPUT_HATCHES;
import static conductance.api.NCMachines.PULVERIZER;
import static conductance.api.NCMachines.STEAM_SOLID_FUEL_BOILER;
import static conductance.api.NCMachines.WIREMILL;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMachines {

	@EventListener(priority = -100)
	private static void init(final RegisterMachineEvent event) {
		STEAM_SOLID_FUEL_BOILER = event.register("steam_solid_fuel_boiler", SteamSolidFuelBoilerMachine::new, b -> b
			.recipeType(NCRecipeTypes.STEAM_BOILER)
			.simpleModel(Conductance.id("block/casing/machine_bronze"))
			.guiSetup(new SteamSolidFuelBoilerMachineGuiSetup())
		);
		ConductanceMachines.initRecipeMachines(event);
		ConductanceMachines.initMultiBlocks(event);
		ConductanceMachines.initMultiParts(event);
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
	}

	private static void initMultiParts(final RegisterMachineEvent event) {
		INPUT_BUSES = CAPI.tiers().newMap(tier -> event.<MultiBlockItemBusPartMachine>register(tier.getId().getPath() + "_input_bus",
			(machineType, blockPos, blockState) -> new MultiBlockItemBusPartMachine(machineType, blockPos, blockState, IO.IN, Math.min(Mth.square(tier.getIndex() + 2), 100)),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("input_bus")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("input_bus", tier).guiSetup(new MultiBlockItemBusPartMachineGuiSetup())
		));
		OUTPUT_BUSES = CAPI.tiers().newMap(tier -> event.<MultiBlockItemBusPartMachine>register(tier.getId().getPath() + "_output_bus",
			(machineType, blockPos, blockState) -> new MultiBlockItemBusPartMachine(machineType, blockPos, blockState, IO.OUT, Math.min(Mth.square(tier.getIndex() + 2), 100)),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("output_bus")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("output_bus", tier).guiSetup(new MultiBlockItemBusPartMachineGuiSetup())
		));
		INPUT_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockFluidHatchPartMachine>register(tier.getId().getPath() + "_input_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockFluidHatchPartMachine(machineType, blockPos, blockState, IO.IN, 1, (int) Math.pow(2, 3 + tier.getIndex()) * CAPI.BUCKET),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("input_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("input_hatch", tier).guiSetup(new MultiBlockFluidHatchPartMachineGuiSetup())
		));
		OUTPUT_HATCHES = CAPI.tiers().newMap(tier -> event.<MultiBlockFluidHatchPartMachine>register(tier.getId().getPath() + "_output_hatch",
			(machineType, blockPos, blockState) -> new MultiBlockFluidHatchPartMachine(machineType, blockPos, blockState, IO.OUT, 1, (int) Math.pow(2, 3 + tier.getIndex()) * CAPI.BUCKET),
			b -> b.customName(ignored ->
				Component.translatable(Util.makeDescriptionId("machine", Conductance.id("output_hatch")), tier.getName())
			).rotationType(BlockRotationType.ALL).tieredModel("output_hatch", tier).guiSetup(new MultiBlockFluidHatchPartMachineGuiSetup())
		));
	}

	private static void initMultiBlocks(final RegisterMachineEvent event) {
		LARGE_BRONZE_BOILER = event.multi("large_bronze_boiler", LargeBoilerMachine::new, b -> b
			.structure('x', c -> c
				.slice("aaa", "aaa", "aaa")
				.slice("axa", "a a", "aaa")
				.slice("aaa", "aaa", "aaa")
				.key('a', StructurePredicate.isBlock(NCBlocks.CASING_BRONZE).or(StructurePredicate.isCapability(NCMultiBlockPartCapabilities.ITEMS_IN)))
			)
			.guiSetup(new MultiBlockControllerGuiSetup(GuiTheme.THEME_BRONZE))
			.casingAppearance(() -> NCBlocks.CASING_BRONZE.value().defaultBlockState())
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

	@EventListener(priority = -100)
	private static void addMachineModels(final AddRuntimeModelEvent event) {
		MachineCore.generateModels(event);
	}

	private ConductanceMachines() {
	}
}
