package conductance.init;

import conductance.api.NCMaterialFlags;
import conductance.api.material.event.RegisterMaterialFlagEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialFlags {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialFlagEvent event) {
		NCMaterialFlags.DUST = event.register("dust");
		NCMaterialFlags.INGOT = event.register("ingot");
		NCMaterialFlags.GEM = event.register("gem");
	}

	private ConductanceMaterialFlags() {
	}
}
