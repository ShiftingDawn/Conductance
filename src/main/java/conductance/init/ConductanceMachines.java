package conductance.init;

import conductance.api.NCRecipeTypes;
import conductance.api.plugin.MachineRegister;
import conductance.machine.RecipeMachine;

public final class ConductanceMachines {

	public static void init(final MachineRegister register) {
		register.register("bender", RecipeMachine::new)
				.recipeType(NCRecipeTypes.BENDER)
				.build();
	}

	private ConductanceMachines() {
	}
}
