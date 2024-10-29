package conductance.init;

import conductance.api.plugin.MaterialRegister;
import conductance.init.material.MaterialLoaderFirstOrder;
import conductance.init.material.MaterialLoaderHigherOrder;
import conductance.init.material.MaterialLoaderMisc;
import conductance.init.material.MaterialLoaderPeriodicTable;
import conductance.init.material.MaterialLoaderSecondOrder;
import conductance.init.material.MaterialLoaderSimpleChemistry;
import conductance.init.material.MaterialLoaderThirdOrder;

public final class ConductanceMaterials {

	public static void init(final MaterialRegister register) {
		MaterialLoaderPeriodicTable.init(register);
		MaterialLoaderFirstOrder.init(register);
		MaterialLoaderSecondOrder.init(register);
		MaterialLoaderThirdOrder.init(register);
		MaterialLoaderHigherOrder.init(register);
		MaterialLoaderMisc.init(register);
		MaterialLoaderSimpleChemistry.init(register);
	}

	private ConductanceMaterials() {
	}
}
