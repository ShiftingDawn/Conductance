package conductance;

import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.plugin.MachineRegister;
import conductance.init.ConductanceMachines;

@ConductancePlugin(modid = CAPI.MOD_ID)
public final class ConductanceRootPlugin implements IConductancePlugin {

	@Override
	public void registerMachines(final MachineRegister register) {
		ConductanceMachines.init(register);
	}
}
