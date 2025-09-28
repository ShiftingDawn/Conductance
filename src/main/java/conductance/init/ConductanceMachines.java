package conductance.init;

import conductance.api.NCRecipeTypes;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;
import conductance.core.machine.MachineCore;
import conductance.init.machine.PulverizerGuiSetup;
import conductance.init.machine.PulverizerMachine;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMachines {

	@EventListener(priority = -100)
	private static void init(final RegisterMachineEvent event) {
		event.register("pulverizer", PulverizerMachine::new, b -> b
			.recipeType(NCRecipeTypes.PULVERIZER).guiSetup(new PulverizerGuiSetup()));
	}

	@EventListener(priority = -100)
	private static void addMachineModels(final AddRuntimeModelEvent event) {
		MachineCore.generateModels(event);
	}

	private ConductanceMachines() {
	}
}
