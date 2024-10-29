package conductance.init;

import conductance.api.NCMaterialTraits;
import conductance.api.NCMaterials;
import conductance.api.NCPeriodicElements;
import conductance.api.plugin.MaterialRegister;

public final class ConductanceMaterials {

	public static void init(MaterialRegister register) {
		NCMaterials.IRON = register.register("iron").ingot().dust()// TODO remove
//				.liquid(builder -> builder.temperature(1811)).plasma()
				.ore().color(200, 200, 200)
//				.textureSet(METALLIC)
//				.addTraitAndPreset(METAL_ALL, MORTAR_PROCESSABLE)
				.periodicElement(NCPeriodicElements.IRON)
//				.cable(CTiers.TIER_MV, 2, 3)
//				.tool(2.0F, 2.0F, 256, 2, b -> b.enchantability(14))
				.build();
	}
}
