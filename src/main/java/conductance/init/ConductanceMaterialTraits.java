package conductance.init;

import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.plugin.MaterialTraitRegister;
import static conductance.api.NCMaterialTraits.DUST;
import static conductance.api.NCMaterialTraits.GEM;
import static conductance.api.NCMaterialTraits.INGOT;

public final class ConductanceMaterialTraits {

	public static void init(MaterialTraitRegister register) {
		DUST = register.register("dust", MaterialTraitDust.class);
		INGOT = register.register("ingot", MaterialTraitIngot.class);
		GEM = register.register("gem", MaterialTraitGem.class);
	}
}
