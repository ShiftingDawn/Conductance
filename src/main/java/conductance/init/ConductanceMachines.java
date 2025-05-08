package conductance.init;

import java.util.Map;
import conductance.api.CAPI;
import conductance.api.NCMachines;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.machine.MachineType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.plugin.MachineRegister;
import conductance.api.util.TextHelper;
import conductance.api.util.tier.Tier;
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

		NCMachines.LV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(register, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.LV);
		NCMachines.MV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(register, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.MV);
		NCMachines.HV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(register, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.HV);
		NCMachines.EV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(register, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.EV);

		NCMachines.WIREMILL = ConductanceMachines.tiered(register, "wiremill", NCRecipeTypes.WIREMILL);
		NCMachines.BENDING_MACHINE = ConductanceMachines.tiered(register, "bending_machine", NCRecipeTypes.BENDING_MACHINE);
		NCMachines.PULVERIZER = ConductanceMachines.tiered(register, "pulverizer", NCRecipeTypes.PULVERIZER);
		NCMachines.CUTTING_MACHINE = ConductanceMachines.tiered(register, "cutting_machine", NCRecipeTypes.PULVERIZER);
		NCMachines.LATHE = ConductanceMachines.tiered(register, "lathe", NCRecipeTypes.LATHE);
		NCMachines.COMPRESSOR = ConductanceMachines.tiered(register, "compressor", NCRecipeTypes.COMPRESSOR);
	}

	private static Map<Tier, MachineType<?>> tiered(final MachineRegister register, final String name, final NCRecipeType recipeType) {
		return CAPI.tiers().newMap(tier -> {
			final String realName = name.contains("%s") ? name.formatted(tier.getRegistryKey()) : "%s_%s".formatted(tier.getRegistryKey(), name);
			final String localizedName = name.contains("%s")
					? TextHelper.lowerUnderscoreToEnglish(name).formatted(tier.getLocalizedNameUnformatted())
					: "%s %s".formatted(tier.getLocalizedNameUnformatted(), TextHelper.lowerUnderscoreToEnglish(name));
			return register.<GenericRecipeMachine>register(realName, (type, pos, blockState) -> new GenericRecipeMachine(type, pos, blockState, tier))
					.recipeType(recipeType)
					.guiSupplier(GenericRecipeMachine.GUI_SUPPLIER.apply(recipeType))
					.localized(localizedName)
					.tieredWorkableModelRenderer(Conductance.id("block/machine_casing_tiered_%s".formatted(tier.getRegistryKey())), name)
					.build();
		});
	}

	private static MachineType<?> tieredGenerator(final MachineRegister register, final String name, final NCRecipeType recipeType, final Tier tier) {
		final String realName = name.contains("%s") ? name.formatted(tier.getRegistryKey()) : "%s_%s".formatted(tier.getRegistryKey(), name);
		final String localizedName = name.contains("%s")
				? TextHelper.lowerUnderscoreToEnglish(name).formatted(tier.getLocalizedNameUnformatted())
				: "%s %s".formatted(tier.getLocalizedNameUnformatted(), TextHelper.lowerUnderscoreToEnglish(name));
		return register.<GenericGeneratorMachine>register(realName, (type, pos, blockState) -> new GenericGeneratorMachine(type, pos, blockState, tier))
				.recipeType(recipeType)
				.recipeModifier(GenericGeneratorMachine::recipeModifier)
				.guiSupplier(GenericGeneratorMachine.GUI_SUPPLIER.apply(NCRecipeTypes.STEAM_TURBINE))
				.localized(localizedName)
				.tieredWorkableModelRenderer(Conductance.id("block/machine_casing_tiered_%s".formatted(tier.getRegistryKey())), name)
				.build();
	}

	private ConductanceMachines() {
	}
}
