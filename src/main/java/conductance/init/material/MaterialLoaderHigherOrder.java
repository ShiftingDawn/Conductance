package conductance.init.material;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialEvent;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_FINE_WIRE;
import static conductance.api.NCMaterialFlags.METAL_ALL;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.RED_ALLOY;
import static conductance.api.NCTextureSets.METALLIC;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialLoaderHigherOrder {

	@EventListener(priority = -92)
	private static void init(final RegisterMaterialEvent register) {
		RED_ALLOY = register.register("red_alloy", builder -> builder
				.ingot()
				.liquid(1400)
				.color(0xc55252).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(COPPER, REDSTONE, 3));
	}

	private MaterialLoaderHigherOrder() {
	}
}
