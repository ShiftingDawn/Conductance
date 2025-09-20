package conductance.init.material;

import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialTextureSets.METALLIC;
import static conductance.api.NCMaterialTextureSets.ROUGH;
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

@ConductancePluginListener(modid = Conductance.MODID)
public final class MaterialLoader3 {

	@EventListener(priority = -94)
	private static void initialize(final RegisterMaterialEvent event) {
		REDSTONE = event.register("redstone", b -> b
			.dust().plate()
			.ore(5, 1, true)
			.liquid(500)
			.style(0xC80000, ROUGH)
			.components(SILICON, PYRITE, 5, RUBY, MERCURY)
		);
		BRICK = event.register("brick", b -> b
			.dust().ingot()
			.style(0x9B5643, ROUGH)
			.components(CLAY, 1)
		);
		DIATOMITE = event.register("diatomite", b -> b
			.dust()
			.ore()
			.color(0x19E1E1)
			.components(FLINT, 8, HEMATITE, SAPPHIRE)
		);
		HSS_G = event.register("hss_g", b -> b
			.metalExtra().fineWire()
			.style(0x999900, METALLIC)
			.components(TUNGSTENSTEEL, 5, CHROMIUM, MOLYBDENUM, 2, VANADIUM)
		);
	}

	private MaterialLoader3() {
	}
}
