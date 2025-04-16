package conductance.init;

import conductance.api.NCMachines;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.plugin.MachineRegister;
import conductance.core.machine.BronzeDirectionalMachineRuntimeModelProvider;
import conductance.machine.GenericGeneratorMachine;
import conductance.machine.GenericRecipeMachine;
import conductance.machine.SteamSolidBoilerMachine;

public final class ConductanceMachines {

	public static void init(final MachineRegister register) {
		NCMachines.STEAM_BOILER_SOLID_FUEL = register.register("steam_solid_fuel_boiler", SteamSolidBoilerMachine::new)
				.recipeType(NCRecipeTypes.STEAM_BOILER)
//				.tooltip(
//						Component.translatable("tooltip.conductance.generic.produces_fluid", 64),
//						Component.translatable("tooltip.conductance.boiler.explode_on_water_fill")
//				)
//				.localized("Solid Fuel Steam Boiler")
				.setModelProvider(BronzeDirectionalMachineRuntimeModelProvider::new)
				.build();

		register.<GenericGeneratorMachine>register("steam_turbine", (type, pos, blockState) -> new GenericGeneratorMachine(type, pos, blockState, NCTiers.LV))
				.recipeType(NCRecipeTypes.STEAM_TURBINE)
				.recipeModifier(GenericGeneratorMachine::recipeModifier)
				.guiSupplier(GenericGeneratorMachine.GUI_SUPPLIER.apply(NCRecipeTypes.STEAM_TURBINE))
				.build();

		register.<GenericRecipeMachine>register("bender", (type, pos, blockState) -> new GenericRecipeMachine(type, pos, blockState, NCTiers.LV))
				.recipeType(NCRecipeTypes.BENDER)
				.guiSupplier(GenericRecipeMachine.GUI_SUPPLIER.apply(NCRecipeTypes.BENDER))
				.build();
	}

	private ConductanceMachines() {
	}
}
