package conductance.init.material;

import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialTextureSets.METALLIC;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.RED_ALLOY;

@ConductancePluginListener(modid = Conductance.MODID)
public final class MaterialLoader4 {

	@EventListener(priority = -92)
	private static void initialize(final RegisterMaterialEvent event) {
		RED_ALLOY = event.register("red_alloy", b -> b
			.metalAll().fineWire()
			.liquid(1400)
			.style(0xC55252, METALLIC)
			.components(COPPER, REDSTONE, 3)
		);
	}

	private MaterialLoader4() {
	}
}
