package conductance.init;

import conductance.api.plugin.MachineRegister;

public final class ConductanceMachines {

	public static void init(final MachineRegister register) {
		register.register("wiremill")
				.build();
	}

	private ConductanceMachines() {
	}
}
