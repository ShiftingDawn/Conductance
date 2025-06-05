package conductance.init;

import java.util.Map;
import conductance.api.CAPI;
import conductance.api.NCMachines;
import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.machine.MachineModelType;
import conductance.api.machine.MachineType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMachineEvent;
import conductance.api.tier.Tier;
import conductance.api.util.TextHelper;
import conductance.Conductance;
import conductance.init.machine.GenericGeneratorMachine;
import conductance.init.machine.GenericRecipeMachine;
import conductance.init.machine.MachineHullMachine;
import conductance.init.machine.SteamSolidBoilerMachine;
import static conductance.Conductance.tooltip;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMachines {

	@EventListener(priority = -100)
	private static void init(final RegisterMachineEvent event) {
		NCMachines.STEAM_BOILER_SOLID_FUEL = event.register("steam_solid_fuel_boiler", SteamSolidBoilerMachine::new, builder -> builder
				.recipeType(NCRecipeTypes.STEAM_BOILER)
				.tooltip(tooltip("generic.produces_fluid", 64), tooltip("boiler.explode_on_water_fill"))
				.localized("Solid Fuel Steam Boiler")
				.modelType(MachineModelType.DEFAULT_WORKABLE, Conductance.id("block/machine_casing_bronze")));

		NCMachines.MACHINE_HULL = CAPI.tiers().newMap(tier -> event.register("%s_machine_hull".formatted(tier.getRegistryKey()), MachineHullMachine::new, builder -> builder
				.localized("%s Machine Hull".formatted(tier.getLocalizedNameUnformatted()))
				.modelType(MachineModelType.TIERED, new MachineModelType.TypeAndTier("machine_hull", tier))
		));

		NCMachines.LV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(event, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.LV);
		NCMachines.MV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(event, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.MV);
		NCMachines.HV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(event, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.HV);
		NCMachines.EV_STEAM_TURBINE = ConductanceMachines.tieredGenerator(event, "steam_turbine", NCRecipeTypes.STEAM_TURBINE, NCTiers.EV);

		NCMachines.WIREMILL = ConductanceMachines.tiered(event, "wiremill", NCRecipeTypes.WIREMILL);
		NCMachines.BENDING_MACHINE = ConductanceMachines.tiered(event, "bending_machine", NCRecipeTypes.BENDING_MACHINE);
		NCMachines.PULVERIZER = ConductanceMachines.tiered(event, "pulverizer", NCRecipeTypes.PULVERIZER);
		NCMachines.CUTTING_MACHINE = ConductanceMachines.tiered(event, "cutting_machine", NCRecipeTypes.PULVERIZER);
		NCMachines.LATHE = ConductanceMachines.tiered(event, "lathe", NCRecipeTypes.LATHE);
		NCMachines.COMPRESSOR = ConductanceMachines.tiered(event, "compressor", NCRecipeTypes.COMPRESSOR);
	}

	private static Map<Tier, MachineType<?>> tiered(final RegisterMachineEvent register, final String name, final NCRecipeType recipeType) {
		return CAPI.tiers().newMap(tier -> {
			final String realName = name.contains("%s") ? name.formatted(tier.getRegistryKey()) : "%s_%s".formatted(tier.getRegistryKey(), name);
			final String localizedName = name.contains("%s")
					? TextHelper.lowerUnderscoreToEnglish(name).formatted(tier.getLocalizedNameUnformatted())
					: "%s %s".formatted(tier.getLocalizedNameUnformatted(), TextHelper.lowerUnderscoreToEnglish(name));
			return register.<GenericRecipeMachine>register(realName, (type, pos, blockState) -> new GenericRecipeMachine(type, pos, blockState, tier), builder -> builder
					.recipeType(recipeType)
					.guiSupplier(GenericRecipeMachine.GUI_SUPPLIER.apply(recipeType))
					.localized(localizedName)
					.modelType(MachineModelType.TIERED_WORKABLE, new MachineModelType.TypeAndTier(name, tier))
			);
		});
	}

	private static MachineType<?> tieredGenerator(final RegisterMachineEvent register, final String name, final NCRecipeType recipeType, final Tier tier) {
		final String realName = name.contains("%s") ? name.formatted(tier.getRegistryKey()) : "%s_%s".formatted(tier.getRegistryKey(), name);
		final String localizedName = name.contains("%s")
				? TextHelper.lowerUnderscoreToEnglish(name).formatted(tier.getLocalizedNameUnformatted())
				: "%s %s".formatted(tier.getLocalizedNameUnformatted(), TextHelper.lowerUnderscoreToEnglish(name));
		return register.<GenericGeneratorMachine>register(realName, (type, pos, blockState) -> new GenericGeneratorMachine(type, pos, blockState, tier), builder -> builder
				.recipeType(recipeType)
				.recipeModifier(GenericGeneratorMachine::recipeModifier)
				.guiSupplier(GenericGeneratorMachine.GUI_SUPPLIER.apply(NCRecipeTypes.STEAM_TURBINE))
				.localized(localizedName)
				.modelType(MachineModelType.TIERED_WORKABLE, new MachineModelType.TypeAndTier(name, tier))
		);
	}

	private ConductanceMachines() {
	}
}
