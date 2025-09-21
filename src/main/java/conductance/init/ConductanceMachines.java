package conductance.init;

import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import conductance.init.machine.TestMachine;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMachines {

	@EventListener(priority = -100)
	private static void init(final RegisterMachineEvent event) {
		event.register("test", TestMachine::new, b -> {
		});
	}

	private ConductanceMachines() {
	}
}
