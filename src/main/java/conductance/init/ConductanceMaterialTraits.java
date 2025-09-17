package conductance.init;

import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.event.RegisterMaterialTraitEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialTraits.GAS;
import static conductance.api.NCMaterialTraits.LIQUID;
import static conductance.api.NCMaterialTraits.PLASMA;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialTraits {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialTraitEvent event) {
		LIQUID = event.register("liquid", MaterialTraitFluid.Liquid.class);
		GAS = event.register("gas", MaterialTraitFluid.Gas.class);
		PLASMA = event.register("plasma", MaterialTraitFluid.Plasma.class);
	}

	private ConductanceMaterialTraits() {
	}
}
