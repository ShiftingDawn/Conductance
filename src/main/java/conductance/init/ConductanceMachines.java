package conductance.init;

import conductance.api.NCRecipeTypes;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;
import conductance.core.machine.MachineCore;
import conductance.init.machine.GenericRecipeMachine;
import conductance.init.machine.GenericRecipeMachineGuiSetup;
import static conductance.api.NCMachines.BENDING_MACHINE;
import static conductance.api.NCMachines.PULVERIZER;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMachines {

	@EventListener(priority = -100)
	private static void init(final RegisterMachineEvent event) {
		BENDING_MACHINE = event.register("bending_machine", GenericRecipeMachine::new, b -> b
			.recipeType(NCRecipeTypes.BENDING_MACHINE).guiSetup(new GenericRecipeMachineGuiSetup()));
		PULVERIZER = event.register("pulverizer", GenericRecipeMachine::new, b -> b
			.recipeType(NCRecipeTypes.PULVERIZER).guiSetup(new GenericRecipeMachineGuiSetup()));
	}

	@EventListener(priority = -100)
	private static void addMachineModels(final AddRuntimeModelEvent event) {
		MachineCore.generateModels(event);
	}

	private ConductanceMachines() {
	}
}
