package conductance;

import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.plugin.MachineRegister;
import conductance.api.plugin.MaterialFlagRegister;
import conductance.api.plugin.MaterialRegister;
import conductance.api.plugin.MaterialTaggedSetRegister;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.init.ConductanceMachines;
import conductance.init.ConductanceMaterialFlags;
import conductance.init.ConductanceMaterialTaggedSets;
import conductance.init.ConductanceMaterialTraits;
import conductance.init.ConductanceMaterials;

@ConductancePlugin(modid = CAPI.MOD_ID)
public final class ConductanceRootPlugin implements IConductancePlugin {

	@Override
	public void registerMaterialTraits(final MaterialTraitRegister register) {
		ConductanceMaterialTraits.init(register);
	}

	@Override
	public void registerMaterialFlags(final MaterialFlagRegister register) {
		ConductanceMaterialFlags.init(register);
	}

	@Override
	public void registerMaterialTaggedSets(final MaterialTaggedSetRegister register) {
		ConductanceMaterialTaggedSets.init(register);
	}

	@Override
	public void registerMaterials(final MaterialRegister register) {
		ConductanceMaterials.init(register);
	}

	@Override
	public void registerMachines(final MachineRegister register) {
		ConductanceMachines.init(register);
	}
}
