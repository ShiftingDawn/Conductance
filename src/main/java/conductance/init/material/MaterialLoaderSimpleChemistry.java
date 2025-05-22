package conductance.init.material;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialEvent;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.GENERATE_FOIL;
import static conductance.api.NCMaterialFlags.GENERATE_PLATE;
import static conductance.api.NCMaterialFlags.GENERATE_RING;
import static conductance.api.NCMaterialFlags.IS_SYNTHETIC;
import static conductance.api.NCMaterials.CARBON;
import static conductance.api.NCMaterials.CHLORINE;
import static conductance.api.NCMaterials.GLUE;
import static conductance.api.NCMaterials.HEAVY_FUEL;
import static conductance.api.NCMaterials.HYDROCHLORIC_ACID;
import static conductance.api.NCMaterials.HYDROGEN;
import static conductance.api.NCMaterials.HYDROGEN_SULFIDE;
import static conductance.api.NCMaterials.LIGHT_FUEL;
import static conductance.api.NCMaterials.NAPHTHA;
import static conductance.api.NCMaterials.OIL;
import static conductance.api.NCMaterials.REFINERY_GAS;
import static conductance.api.NCMaterials.RUBBER;
import static conductance.api.NCMaterials.SULFURIC_GAS;
import static conductance.api.NCMaterials.SULFURIC_HEAVY_FUEL;
import static conductance.api.NCMaterials.SULFURIC_LIGHT_FUEL;
import static conductance.api.NCMaterials.SULFURIC_NAPHTHA;
import static conductance.api.NCTextureSets.METALLIC;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialLoaderSimpleChemistry {

	@EventListener(priority = -88)
	private static void init(final RegisterMaterialEvent register) {
		RUBBER = register.register("rubber", builder -> builder
				.ingot()
				.liquid()
				.color(0x54503D)
				.flags(IS_SYNTHETIC, GENERATE_PLATE, GENERATE_FOIL, GENERATE_RING)
				.components(CARBON, 5, HYDROGEN, 8));

		HYDROCHLORIC_ACID = register.register("hydrochloric_acid", builder -> builder
				.liquid()
				.components(HYDROGEN, CHLORINE));

		GLUE = register.register("glue", builder -> builder
				.liquid()
				.color(200, 196, 0));

		OIL = register.register("oil", builder -> builder
				.liquid()
				.color(0x222222));

		SULFURIC_NAPHTHA = register.register("sulfuric_naphtha", builder -> builder
				.liquid()
				.color(255, 255, 0).textureSet(METALLIC));

		SULFURIC_HEAVY_FUEL = register.register("sulfuric_heavy_fuel", builder -> builder
				.liquid()
				.color(255, 255, 125).textureSet(METALLIC));

		SULFURIC_LIGHT_FUEL = register.register("sulfuric_light_fuel", builder -> builder
				.liquid()
				.color(255, 255, 200).textureSet(METALLIC));

		SULFURIC_GAS = register.register("sulfuric_gas", builder -> builder
				.liquid()
				.color(200, 200, 200).textureSet(METALLIC));

		NAPHTHA = register.register("naphtha", builder -> builder
				.liquid()
				.color(255, 255, 0));

		HEAVY_FUEL = register.register("heavy_fuel", builder -> builder
				.liquid()
				.color(255, 255, 125));

		LIGHT_FUEL = register.register("light_fuel", builder -> builder
				.liquid()
				.color(255, 255, 200));

		REFINERY_GAS = register.register("refinery_gas", builder -> builder
				.liquid()
				.color(200, 200, 200));

		HYDROGEN_SULFIDE = register.register("hydrogen_sulfide", builder -> builder
				.liquid()
				.color(200, 128, 0));
	}

	private MaterialLoaderSimpleChemistry() {
	}
}
