package conductance.init;

import java.util.Map;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import conductance.api.CAPI;
import conductance.api.NCRecipeTypes;
import conductance.api.machine.MachineBlockWorkable;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.tier.Tier;
import conductance.Conductance;
import conductance.core.machine.MachineCore;
import conductance.init.machine.GenericRecipeMachine;
import conductance.init.machine.GenericRecipeMachineGuiSetup;
import static conductance.api.NCMachines.BENDING_MACHINE;
import static conductance.api.NCMachines.COMPRESSOR;
import static conductance.api.NCMachines.CUTTING_MACHINE;
import static conductance.api.NCMachines.EXTRACTOR;
import static conductance.api.NCMachines.EXTRUDER;
import static conductance.api.NCMachines.LATHE;
import static conductance.api.NCMachines.PULVERIZER;
import static conductance.api.NCMachines.WIREMILL;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMachines {

	@EventListener(priority = -100)
	private static void init(final RegisterMachineEvent event) {
		BENDING_MACHINE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "bending_machine", NCRecipeTypes.BENDING_MACHINE);
		PULVERIZER = ConductanceMachines.makeTieredGenericRecipeMachine(event, "pulverizer", NCRecipeTypes.PULVERIZER);
		EXTRUDER = ConductanceMachines.makeTieredGenericRecipeMachine(event, "extruder", NCRecipeTypes.EXTRUDER);
		WIREMILL = ConductanceMachines.makeTieredGenericRecipeMachine(event, "wiremill", NCRecipeTypes.WIREMILL);
		LATHE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "lathe", NCRecipeTypes.LATHE);
		EXTRACTOR = ConductanceMachines.makeTieredGenericRecipeMachine(event, "extractor", NCRecipeTypes.EXTRACTOR);
		COMPRESSOR = ConductanceMachines.makeTieredGenericRecipeMachine(event, "compressor", NCRecipeTypes.COMPRESSOR);
		CUTTING_MACHINE = ConductanceMachines.makeTieredGenericRecipeMachine(event, "cutting_machine", NCRecipeTypes.CUTTING_MACHINE);
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
