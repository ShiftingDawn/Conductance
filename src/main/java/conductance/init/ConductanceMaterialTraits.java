package conductance.init;

import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.material.traits.MaterialTraitOre;
import conductance.api.plugin.MaterialTraitRegister;
import static conductance.api.NCMaterialTraits.DUST;
import static conductance.api.NCMaterialTraits.GAS;
import static conductance.api.NCMaterialTraits.GEM;
import static conductance.api.NCMaterialTraits.INGOT;
import static conductance.api.NCMaterialTraits.LIQUID;
import static conductance.api.NCMaterialTraits.ORE;
import static conductance.api.NCMaterialTraits.PLASMA;

public final class ConductanceMaterialTraits {

	public static void init(final MaterialTraitRegister register) {
		DUST = register.register("dust", MaterialTraitDust.class);
		INGOT = register.register("ingot", MaterialTraitIngot.class);
		GEM = register.register("gem", MaterialTraitGem.class);
		ORE = register.register("ore", MaterialTraitOre.class);

		LIQUID = register.register("liquid", MaterialTraitFluid.Liquid.class);
		GAS = register.register("gas", MaterialTraitFluid.Gas.class);
		PLASMA = register.register("plasma", MaterialTraitFluid.Plasma.class);
	}

	private ConductanceMaterialTraits() {
	}
}
