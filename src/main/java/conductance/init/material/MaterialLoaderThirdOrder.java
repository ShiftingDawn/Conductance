package conductance.init.material;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialEvent;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_FRAME_BOX;
import static conductance.api.NCMaterialFlags.GENERATE_PLATE;
import static conductance.api.NCMaterialFlags.METAL_EXTRA2;
import static conductance.api.NCMaterialFlags.NO_DECOMPOSE;
import static conductance.api.NCMaterials.BRICK;
import static conductance.api.NCMaterials.CHROMIUM;
import static conductance.api.NCMaterials.CLAY;
import static conductance.api.NCMaterials.DIATOMITE;
import static conductance.api.NCMaterials.FLINT;
import static conductance.api.NCMaterials.HEMATITE;
import static conductance.api.NCMaterials.HSS_G;
import static conductance.api.NCMaterials.MERCURY;
import static conductance.api.NCMaterials.MOLYBDENUM;
import static conductance.api.NCMaterials.PYRITE;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.RUBY;
import static conductance.api.NCMaterials.SAPPHIRE;
import static conductance.api.NCMaterials.SILICON;
import static conductance.api.NCMaterials.TUNGSTENSTEEL;
import static conductance.api.NCMaterials.VANADIUM;
import static conductance.api.NCTextureSets.METALLIC;
import static conductance.api.NCTextureSets.ROUGH;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialLoaderThirdOrder {

	@EventListener(priority = -94)
	private static void init(final RegisterMaterialEvent register) {
		REDSTONE = register.register("redstone", builder -> builder
				.dust()
				.ore(5, 1, true)
				.liquid(500)
				.color(200, 0, 0).textureSet(ROUGH)
				.flags(GENERATE_PLATE)
				.components(SILICON, PYRITE, 5, RUBY, MERCURY));

		BRICK = register.register("brick", builder -> builder
				.dust()
				.color(155, 86, 67).textureSet(ROUGH)
				.components(CLAY, 1)
				.flags(CAN_MORTAR, NO_DECOMPOSE));

		DIATOMITE = register.register("diatomite", builder -> builder
				.dust()
				.ore()
				.color(25, 225, 225)
				.components(FLINT, 8, HEMATITE, SAPPHIRE));

		HSS_G = register.register("hss_g", builder -> builder
				.ingot()
				.color(153, 153, 0).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_FRAME_BOX)
				.components(TUNGSTENSTEEL, 5, CHROMIUM, MOLYBDENUM, 2, VANADIUM));
	}

	private MaterialLoaderThirdOrder() {
	}
}
