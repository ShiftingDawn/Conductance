package conductance.api;

import conductance.api.plugin.MachineRegister;
import conductance.api.plugin.MaterialFlagRegister;
import conductance.api.plugin.MaterialOreTypeRegister;
import conductance.api.plugin.MaterialRegister;
import conductance.api.plugin.MaterialTaggedSetRegister;
import conductance.api.plugin.MaterialTraitRegister;

public interface IConductancePlugin {

	default void registerMaterialTraits(final MaterialTraitRegister register) {
	}

	default void registerMaterialFlags(final MaterialFlagRegister register) {
	}

	default void registerMaterialOreTypes(final MaterialOreTypeRegister register) {
	}

	default void registerMaterialTaggedSets(final MaterialTaggedSetRegister register) {
	}

	default void registerMaterials(final MaterialRegister register) {
	}

	default void registerMachines(final MachineRegister register) {
	}
}
