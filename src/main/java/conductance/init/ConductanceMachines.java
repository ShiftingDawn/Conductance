package conductance.init;

import conductance.api.NCRecipeTypes;
import conductance.api.NCTiers;
import conductance.api.plugin.MachineRegister;
import conductance.machine.GenericRecipeMachine;

public final class ConductanceMachines {

	public static void init(final MachineRegister register) {
		register.<GenericRecipeMachine>register("bender", (type, pos, blockState) -> new GenericRecipeMachine(type, pos, blockState, NCTiers.LV))
				.recipeType(NCRecipeTypes.BENDER)
				.guiSupplier(GenericRecipeMachine.GUI_SUPPLIER.apply(NCRecipeTypes.BENDER))
				.build();
	}

	private ConductanceMachines() {
	}
}
