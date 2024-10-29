package conductance.init.material;

import conductance.api.plugin.MaterialRegister;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_FINE_WIRE;
import static conductance.api.NCMaterialFlags.METAL_ALL;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.RED_ALLOY;
import static conductance.api.NCTextureSets.METALLIC;

public final class MaterialLoaderHigherOrder {

	//@formatter:off
	public static void init(final MaterialRegister register) {
		RED_ALLOY = register.register("red_alloy")
				.ingot()
				.liquid(1400)
				.color(0xc55252).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(COPPER, REDSTONE, 3)
				.build();
	}
	//@formatter:on

	private MaterialLoaderHigherOrder() {
	}
}
