package conductance.init;

import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.plugin.MaterialTraitRegister;
import static conductance.api.NCMaterialTraits.*;

public final class ConductanceMaterialTraits {

	public static void init(final MaterialTraitRegister register) {
		DUST = register.register("dust", MaterialTraitDust.class);
		INGOT = register.register("ingot", MaterialTraitIngot.class);
		GEM = register.register("gem", MaterialTraitGem.class);

		LIQUID = register.register("liquid", MaterialTraitFluid.Liquid.class);
		GAS = register.register("gas", MaterialTraitFluid.Gas.class);
		PLASMA = register.register("plasma", MaterialTraitFluid.Plasma.class);
	}
}
