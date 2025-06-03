package conductance.init;

import conductance.api.material.event.RegisterMaterialTextureTypeEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
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
import static conductance.api.NCTextureTypes.WIRE_BASE;
import static conductance.api.NCTextureTypes.WIRE_SIDE;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialTextureTypes {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialTextureTypeEvent event) {
		DUST = event.register("dust");

		INGOT = event.register("ingot");
		NUGGET = event.register("nugget");

		GEM = event.register("gem");
		GEM_FLAWED = event.register("flawed_gem");
		GEM_FLAWLESS = event.register("flawless_gem");
		GEM_EXQUISITE = event.register("exquisite_gem");

		STORAGE_BLOCK = event.register("block");
		ORE = event.register("ore");
		RAW_ORE = event.register("raw_ore");
		RAW_ORE_BLOCK = event.register("raw_ore_block");

		LIQUID = event.register("liquid");
		GAS = event.register("gas");
		PLASMA = event.register("plasma");

		PLATE = event.register("plate");
		PLATE_DOUBLE = event.register("double_plate");
		PLATE_DENSE = event.register("dense_plate");
		FOIL = event.register("foil");

		GEAR = event.register("gear");
		GEAR_SMALL = event.register("small_gear");

		LENS = event.register("lens");

		ROD = event.register("rod");
		BOLT = event.register("bolt");
		SCREW = event.register("screw");
		RING = event.register("ring");

		FINE_WIRE = event.register("fine_wire");
		ROTOR = event.register("rotor");

		FRAME_BOX = event.register("frame_box");

		WIRE_BASE = event.register("wire_base");
		WIRE_SIDE = event.register("wire_side");
	}

	private ConductanceMaterialTextureTypes() {
	}
}
