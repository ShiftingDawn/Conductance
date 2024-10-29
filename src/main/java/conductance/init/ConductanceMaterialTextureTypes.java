package conductance.init;

import conductance.api.plugin.MaterialTextureTypeRegister;
import static conductance.api.NCTextureTypes.BOLT;
import static conductance.api.NCTextureTypes.DUST;
import static conductance.api.NCTextureTypes.FINE_WIRE;
import static conductance.api.NCTextureTypes.FOIL;
import static conductance.api.NCTextureTypes.FRAME_BOX;
import static conductance.api.NCTextureTypes.GAS;
import static conductance.api.NCTextureTypes.GEAR;
import static conductance.api.NCTextureTypes.GEAR_SMALL;
import static conductance.api.NCTextureTypes.GEM;
import static conductance.api.NCTextureTypes.GEM_EXQUISITE;
import static conductance.api.NCTextureTypes.GEM_FLAWED;
import static conductance.api.NCTextureTypes.GEM_FLAWLESS;
import static conductance.api.NCTextureTypes.INGOT;
import static conductance.api.NCTextureTypes.LENS;
import static conductance.api.NCTextureTypes.LIQUID;
import static conductance.api.NCTextureTypes.NUGGET;
import static conductance.api.NCTextureTypes.ORE;
import static conductance.api.NCTextureTypes.PLASMA;
import static conductance.api.NCTextureTypes.PLATE;
import static conductance.api.NCTextureTypes.PLATE_DENSE;
import static conductance.api.NCTextureTypes.PLATE_DOUBLE;
import static conductance.api.NCTextureTypes.RAW_ORE;
import static conductance.api.NCTextureTypes.RAW_ORE_BLOCK;
import static conductance.api.NCTextureTypes.RING;
import static conductance.api.NCTextureTypes.ROD;
import static conductance.api.NCTextureTypes.ROTOR;
import static conductance.api.NCTextureTypes.SCREW;
import static conductance.api.NCTextureTypes.STORAGE_BLOCK;

public final class ConductanceMaterialTextureTypes {

	public static void init(final MaterialTextureTypeRegister register) {
		DUST = register.register("dust");

		INGOT = register.register("ingot");
		NUGGET = register.register("nugget");

		GEM = register.register("gem");
		GEM_FLAWED = register.register("flawed_gem");
		GEM_FLAWLESS = register.register("flawless_gem");
		GEM_EXQUISITE = register.register("exquisite_gem");

		STORAGE_BLOCK = register.register("block");
		ORE = register.register("ore");
		RAW_ORE = register.register("raw_ore");
		RAW_ORE_BLOCK = register.register("raw_ore_block");

		LIQUID = register.register("liquid");
		GAS = register.register("gas");
		PLASMA = register.register("plasma");

		PLATE = register.register("plate");
		PLATE_DOUBLE = register.register("double_plate");
		PLATE_DENSE = register.register("dense_plate");
		FOIL = register.register("foil");

		GEAR = register.register("gear");
		GEAR_SMALL = register.register("small_gear");

		LENS = register.register("lens");

		ROD = register.register("rod");
		BOLT = register.register("bolt");
		SCREW = register.register("screw");
		RING = register.register("ring");

		FINE_WIRE = register.register("fine_wire");
		ROTOR = register.register("rotor");

		FRAME_BOX = register.register("frame_box");
	}

	private ConductanceMaterialTextureTypes() {
	}
}
