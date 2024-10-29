package conductance.init.material;

import conductance.api.plugin.MaterialRegister;
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

public final class MaterialLoaderSimpleChemistry {

	//@formatter:off
	public static void init(final MaterialRegister register) {
		RUBBER = register.register("rubber")
				.ingot()
				.liquid()
				.color(0x54503D)
				.flags(IS_SYNTHETIC, GENERATE_PLATE, GENERATE_FOIL, GENERATE_RING)
				.components(CARBON, 5, HYDROGEN, 8)
				.build();

		HYDROCHLORIC_ACID = register.register("hydrochloric_acid")
				.liquid()
				.components(HYDROGEN, CHLORINE)
				.build();

		GLUE = register.register("glue")
				.liquid()
				.color(200, 196, 0)
				.build();

		OIL = register.register("oil")
				.liquid()
				.color(0x222222)
				.build();

		SULFURIC_NAPHTHA = register.register("sulfuric_naphtha")
				.liquid()
				.color(255, 255, 0).textureSet(METALLIC)
				.build();

		SULFURIC_HEAVY_FUEL = register.register("sulfuric_heavy_fuel")
				.liquid()
				.color(255, 255, 125).textureSet(METALLIC)
				.build();

		SULFURIC_LIGHT_FUEL = register.register("sulfuric_light_fuel")
				.liquid()
				.color(255, 255, 200).textureSet(METALLIC)
				.build();

		SULFURIC_GAS = register.register("sulfuric_gas")
				.liquid()
				.color(200, 200, 200).textureSet(METALLIC)
				.build();

		NAPHTHA = register.register("naphtha")
				.liquid()
				.color(255, 255, 0)
				.build();

		HEAVY_FUEL = register.register("heavy_fuel")
				.liquid()
				.color(255, 255, 125)
				.build();

		LIGHT_FUEL = register.register("light_fuel")
				.liquid()
				.color(255, 255, 200)
				.build();

		REFINERY_GAS = register.register("refinery_gas")
				.liquid()
				.color(200, 200, 200)
				.build();

		HYDROGEN_SULFIDE = register.register("hydrogen_sulfide")
				.liquid()
				.color(200, 128, 0)
				.build();
	}
	//@formatter:on

	private MaterialLoaderSimpleChemistry() {
	}
}
