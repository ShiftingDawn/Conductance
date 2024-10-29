package conductance.init;

import java.util.List;
import java.util.Set;
import conductance.api.NCMaterialTraits;
import conductance.api.plugin.MaterialFlagRegister;
import static conductance.api.NCMaterialFlags.CAN_CENTRIFUGE;
import static conductance.api.NCMaterialFlags.CAN_CRYSTALLIZE;
import static conductance.api.NCMaterialFlags.CAN_ELECTROLYZE;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_BLOCK;
import static conductance.api.NCMaterialFlags.GENERATE_BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.GENERATE_FINE_WIRE;
import static conductance.api.NCMaterialFlags.GENERATE_FOIL;
import static conductance.api.NCMaterialFlags.GENERATE_FRAME;
import static conductance.api.NCMaterialFlags.GENERATE_GEAR;
import static conductance.api.NCMaterialFlags.GENERATE_LENS;
import static conductance.api.NCMaterialFlags.GENERATE_PLATE;
import static conductance.api.NCMaterialFlags.GENERATE_RING;
import static conductance.api.NCMaterialFlags.GENERATE_ROD;
import static conductance.api.NCMaterialFlags.GENERATE_ROTOR;
import static conductance.api.NCMaterialFlags.GENERATE_SMALL_GEAR;
import static conductance.api.NCMaterialFlags.IS_SYNTHETIC;
import static conductance.api.NCMaterialFlags.METAL_ALL;
import static conductance.api.NCMaterialFlags.METAL_DEFAULT;
import static conductance.api.NCMaterialFlags.METAL_EXTRA;
import static conductance.api.NCMaterialFlags.METAL_EXTRA2;
import static conductance.api.NCMaterialFlags.NO_DECOMPOSE;
import static conductance.api.NCMaterialFlags.NO_HANDLING;
import static conductance.api.NCMaterialFlags.NO_SMELTING;

public final class ConductanceMaterialFlags {

	public static void init(final MaterialFlagRegister register) {
		IS_SYNTHETIC = register.register("is_synthetic");

		GENERATE_PLATE = register.register("generate_plate", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_ROD = register.register("generate_rod", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_BOLT_AND_SCREW = register.register("generate_bolt_and_screw", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_GEAR = register.register("generate_gear", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_SMALL_GEAR = register.register("generate_small_gear", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_BLOCK = register.register("generate_block", Set.of(), Set.of(NCMaterialTraits.DUST));

		GENERATE_FOIL = register.register("generate_foil", Set.of(), Set.of(NCMaterialTraits.INGOT));
		GENERATE_RING = register.register("generate_ring", Set.of(), Set.of(NCMaterialTraits.INGOT));
		GENERATE_FINE_WIRE = register.register("generate_fine_wire", Set.of(), Set.of(NCMaterialTraits.INGOT));
		GENERATE_ROTOR = register.register("generate_rotor", Set.of(GENERATE_PLATE, GENERATE_BOLT_AND_SCREW, GENERATE_RING), Set.of(NCMaterialTraits.INGOT));
		GENERATE_FRAME = register.register("generate_frame", Set.of(GENERATE_ROD), Set.of(NCMaterialTraits.INGOT));

		CAN_CRYSTALLIZE = register.register("autoclave_processable", Set.of(), Set.of(NCMaterialTraits.GEM));
		GENERATE_LENS = register.register("generate_lens", Set.of(GENERATE_PLATE), Set.of(NCMaterialTraits.INGOT));

		CAN_MORTAR = register.register("mortar_processable", Set.of(), Set.of(NCMaterialTraits.DUST));
		NO_SMELTING = register.register("prevent_smelting", Set.of(), Set.of(NCMaterialTraits.DUST));
		NO_DECOMPOSE = register.register("prevent_decompose");
		NO_HANDLING = register.register("prevent_handling");
		CAN_ELECTROLYZE = register.register("decompose_electrolyze");
		CAN_CENTRIFUGE = register.register("decompose_centrifuge");

		METAL_DEFAULT.add(GENERATE_PLATE);
		METAL_EXTRA.addAll(METAL_DEFAULT);
		METAL_EXTRA.add(GENERATE_ROD);
		METAL_EXTRA2.addAll(METAL_EXTRA);
		METAL_EXTRA2.add(GENERATE_BOLT_AND_SCREW);
		METAL_ALL.addAll(METAL_EXTRA2);
		METAL_ALL.addAll(List.of(GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_FRAME, GENERATE_FOIL, GENERATE_RING, GENERATE_ROTOR));
	}

	private ConductanceMaterialFlags() {
	}
}
