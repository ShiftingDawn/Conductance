package conductance.init;

import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.plugin.MaterialTraitRegister;
import static conductance.api.NCMaterialTraits.DUST;

public final class ConductanceMaterialTraits {

	public static void init(MaterialTraitRegister register) {
		DUST = register.register("dust", MaterialTraitDust.class);
	}
}
