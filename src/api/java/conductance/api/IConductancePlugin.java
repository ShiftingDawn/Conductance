package conductance.api;

import conductance.api.plugin.MachineRegister;
import conductance.api.plugin.MaterialRegister;

public interface IConductancePlugin {

	default void registerMaterials(final MaterialRegister register) {
	}

	default void registerMachines(final MachineRegister register) {
	}
}
