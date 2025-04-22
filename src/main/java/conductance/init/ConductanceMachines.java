package conductance.init;

import conductance.api.NCMachines;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.plugin.MachineRegister;
import conductance.Conductance;
import conductance.machine.GenericGeneratorMachine;
import conductance.machine.GenericRecipeMachine;
import conductance.machine.SteamSolidBoilerMachine;
import static conductance.Conductance.tooltip;

public final class ConductanceMachines {

	public static void init(final MachineRegister register) {
		NCMachines.STEAM_BOILER_SOLID_FUEL = register.register("steam_solid_fuel_boiler", SteamSolidBoilerMachine::new)
				.recipeType(NCRecipeTypes.STEAM_BOILER)
				.tooltip(tooltip("generic.produces_fluid", 64), tooltip("boiler.explode_on_water_fill"))
				.localized("Solid Fuel Steam Boiler")
				.workableModelRenderer(Conductance.id("block/machine_casing_bronze"))
				.build();

		register.<GenericGeneratorMachine>register("steam_turbine", (type, pos, blockState) -> new GenericGeneratorMachine(type, pos, blockState, NCTiers.LV))
				.recipeType(NCRecipeTypes.STEAM_TURBINE)
				.recipeModifier(GenericGeneratorMachine::recipeModifier)
				.guiSupplier(GenericGeneratorMachine.GUI_SUPPLIER.apply(NCRecipeTypes.STEAM_TURBINE))
				.workableModelRenderer(Conductance.id("block/machine_casing_tiered"))
				.build();

		register.<GenericRecipeMachine>register("bender", (type, pos, blockState) -> new GenericRecipeMachine(type, pos, blockState, NCTiers.LV))
				.recipeType(NCRecipeTypes.BENDER)
				.guiSupplier(GenericRecipeMachine.GUI_SUPPLIER.apply(NCRecipeTypes.BENDER))
				.workableModelRenderer(Conductance.id("block/machine_casing_tiered"))
				.build();
	}

	private ConductanceMachines() {
	}
}
