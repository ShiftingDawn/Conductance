package conductance.init.material;

import conductance.api.NCMaterialProps;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterials.CARBON;
import static conductance.api.NCMaterials.CREOSOTE_OIL;
import static conductance.api.NCMaterials.GLUE;
import static conductance.api.NCMaterials.HYDROGEN;
import static conductance.api.NCMaterials.LUBRICANT;
import static conductance.api.NCMaterials.RUBBER;

@ConductancePluginListener(modid = Conductance.MODID)
public final class MaterialLoader6 {

	@EventListener(priority = -88)
	private static void initialize(final RegisterMaterialEvent event) {
		RUBBER = event.register("rubber", b -> b
			.dust().ingot().plate().foil().rod().ring()
			.liquid()
			.color(0x54503D)
			.synthetic()
			.components(CARBON, 5, HYDROGEN, 8));
		GLUE = event.register("glue", b -> b
			.liquid()
			.color(0xC8C400));
		CREOSOTE_OIL = event.register("creosote_oil", b -> b
			.liquid()
			.color(0x804000)
			.prop(NCMaterialProps.BURN_TIME, 6400)
		);
		LUBRICANT = event.register("lubricant", b -> b
			.liquid()
			.color(0xFFC400)
		);
	}

	private MaterialLoader6() {
	}
}
