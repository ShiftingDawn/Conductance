package conductance.init;

import conductance.api.NCRecipeTypes;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.resource.event.AddRuntimeModelEvent;
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
		BENDING_MACHINE = ConductanceMachines.makeGenericRecipeMachine(event, "bending_machine", NCRecipeTypes.BENDING_MACHINE);
		PULVERIZER = ConductanceMachines.makeGenericRecipeMachine(event, "pulverizer", NCRecipeTypes.PULVERIZER);
		EXTRUDER = ConductanceMachines.makeGenericRecipeMachine(event, "extruder", NCRecipeTypes.EXTRUDER);
		WIREMILL = ConductanceMachines.makeGenericRecipeMachine(event, "wiremill", NCRecipeTypes.WIREMILL);
		LATHE = ConductanceMachines.makeGenericRecipeMachine(event, "lathe", NCRecipeTypes.LATHE);
		EXTRACTOR = ConductanceMachines.makeGenericRecipeMachine(event, "extractor", NCRecipeTypes.EXTRACTOR);
		COMPRESSOR = ConductanceMachines.makeGenericRecipeMachine(event, "compressor", NCRecipeTypes.COMPRESSOR);
		CUTTING_MACHINE = ConductanceMachines.makeGenericRecipeMachine(event, "cutting_machine", NCRecipeTypes.CUTTING_MACHINE);
	}

	private static MachineType<GenericRecipeMachine> makeGenericRecipeMachine(final RegisterMachineEvent event, final String name, final MachineRecipeType recipeType) {
		return event.register(name, GenericRecipeMachine::new,
			b -> b.recipeType(recipeType).guiSetup(new GenericRecipeMachineGuiSetup()));
	}

	@EventListener(priority = -100)
	private static void addMachineModels(final AddRuntimeModelEvent event) {
		MachineCore.generateModels(event);
	}

	private ConductanceMachines() {
	}
}
