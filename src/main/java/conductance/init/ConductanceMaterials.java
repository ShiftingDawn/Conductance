package conductance.init;

import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterials {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialEvent event) {
		event.register("iron", b -> b.dust().ingot());
		event.register("diamond", b -> b.dust().gem());
	}

	private ConductanceMaterials() {
	}
}
