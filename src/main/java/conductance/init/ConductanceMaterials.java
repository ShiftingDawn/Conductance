package conductance.init;

import conductance.api.NCMaterials;
import conductance.api.NCPeriodicElements;
import conductance.api.plugin.MaterialRegister;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.METAL_ALL;

public final class ConductanceMaterials {

	public static void init(final MaterialRegister register) {
		NCMaterials.IRON = register.register("iron").ingot().liquid(1811).plasma().ore().color(255, 0, 0)// .color(200, 200, 200)
//				.textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, CAN_MORTAR).periodicElement(NCPeriodicElements.IRON)
//				.cable(CTiers.TIER_MV, 2, 3)
//				.tool(2.0F, 2.0F, 256, 2, b -> b.enchantability(14))
				.build();
	}
}
