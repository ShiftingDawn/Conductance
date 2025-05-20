package conductance.api;

import conductance.api.plugin.MachineRegister;

public interface IConductancePlugin {

	default void registerMachines(final MachineRegister register) {
	}
}
