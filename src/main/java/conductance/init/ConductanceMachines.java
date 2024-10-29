package conductance.init;

import conductance.api.plugin.MachineRegister;
import conductance.content.machine.RecipeMachine;

public final class ConductanceMachines {

	public static void init(final MachineRegister register) {
		register.register("wiremill", RecipeMachine::new)
				.build();
	}

	private ConductanceMachines() {
	}
}
