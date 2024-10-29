package conductance.init;

import conductance.api.plugin.MaterialRegister;
import conductance.init.material.MaterialLoaderPeriodicTable;

public final class ConductanceMaterials {

	public static void init(final MaterialRegister register) {
		MaterialLoaderPeriodicTable.init(register);
	}
}
