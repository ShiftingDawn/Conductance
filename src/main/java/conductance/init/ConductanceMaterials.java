package conductance.init;

import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTextureSets;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterials {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialEvent event) {
		event.register("iron", b -> b
				.dust().ingot()
				.style(0xd5d5d5, NCMaterialTextureSets.METALLIC)
		);
		event.register("coal", b -> b
				.dust().gem()
				.prop(NCMaterialProps.BURN_TIME, 1600)
				.style(0x2d2d2d, NCMaterialTextureSets.ROUGH)
		);
		event.register("diamond", b -> b
				.dust().gem()
				.style(0x49ead6, NCMaterialTextureSets.DIAMOND)
		);
	}

	private ConductanceMaterials() {
	}
}
