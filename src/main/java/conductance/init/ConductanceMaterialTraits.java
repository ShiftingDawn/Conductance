package conductance.init;

import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.material.traits.MaterialTraitOre;
import conductance.api.material.traits.MaterialTraitWire;
import conductance.api.material.traits.MaterialTraitWood;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialTraitEvent;
import conductance.Conductance;
import static conductance.api.NCMaterialTraits.DUST;
import static conductance.api.NCMaterialTraits.GAS;
import static conductance.api.NCMaterialTraits.GEM;
import static conductance.api.NCMaterialTraits.INGOT;
import static conductance.api.NCMaterialTraits.LIQUID;
import static conductance.api.NCMaterialTraits.ORE;
import static conductance.api.NCMaterialTraits.PLASMA;
import static conductance.api.NCMaterialTraits.WIRE;
import static conductance.api.NCMaterialTraits.WOOD;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialTraits {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialTraitEvent event) {
		DUST = event.register("dust", MaterialTraitDust.class);
		INGOT = event.register("ingot", MaterialTraitIngot.class);
		GEM = event.register("gem", MaterialTraitGem.class);
		ORE = event.register("ore", MaterialTraitOre.class);
		WOOD = event.register("wood", MaterialTraitWood.class);

		LIQUID = event.register("liquid", MaterialTraitFluid.Liquid.class);
		GAS = event.register("gas", MaterialTraitFluid.Gas.class);
		PLASMA = event.register("plasma", MaterialTraitFluid.Plasma.class);

		WIRE = event.register("wire", MaterialTraitWire.class);
	}

	private ConductanceMaterialTraits() {
	}
}
