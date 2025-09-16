package conductance.init;

import conductance.api.NCMaterialProps;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterials {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialEvent event) {
		event.register("iron", b -> b.dust().ingot().color(0xff0000));
		event.register("coal", b -> b.dust().gem().color(0x00ff00).prop(NCMaterialProps.BURN_TIME, 20 * 600));
		event.register("diamond", b -> b.dust().gem().color(0x0000ff));
	}

	private ConductanceMaterials() {
	}
}
