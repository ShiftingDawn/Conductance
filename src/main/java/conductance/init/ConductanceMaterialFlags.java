package conductance.init;

import java.util.List;
import java.util.Set;
import conductance.api.NCMaterialTraits;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialFlagEvent;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.CAN_CENTRIFUGE;
import static conductance.api.NCMaterialFlags.CAN_CRYSTALLIZE;
import static conductance.api.NCMaterialFlags.CAN_ELECTROLYZE;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_BLOCK;
import static conductance.api.NCMaterialFlags.GENERATE_BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.GENERATE_FINE_WIRE;
import static conductance.api.NCMaterialFlags.GENERATE_FOIL;
import static conductance.api.NCMaterialFlags.GENERATE_FRAME_BOX;
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

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialFlags {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialFlagEvent event) {
		IS_SYNTHETIC = event.register("is_synthetic");

		GENERATE_PLATE = event.register("generate_plate", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_ROD = event.register("generate_rod", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_BOLT_AND_SCREW = event.register("generate_bolt_and_screw", Set.of(GENERATE_ROD), Set.of());
		GENERATE_GEAR = event.register("generate_gear", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_SMALL_GEAR = event.register("generate_small_gear", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_BLOCK = event.register("generate_block", Set.of(), Set.of(NCMaterialTraits.DUST));

		GENERATE_FOIL = event.register("generate_foil", Set.of(), Set.of(NCMaterialTraits.DUST));
		GENERATE_RING = event.register("generate_ring", Set.of(), Set.of(NCMaterialTraits.INGOT));
		GENERATE_FINE_WIRE = event.register("generate_fine_wire", Set.of(), Set.of(NCMaterialTraits.INGOT));
		GENERATE_ROTOR = event.register("generate_rotor", Set.of(GENERATE_PLATE, GENERATE_BOLT_AND_SCREW, GENERATE_RING), Set.of(NCMaterialTraits.INGOT));
		GENERATE_FRAME_BOX = event.register("generate_frame", Set.of(GENERATE_ROD), Set.of());

		CAN_CRYSTALLIZE = event.register("can_crystallize", Set.of(), Set.of(NCMaterialTraits.GEM));
		GENERATE_LENS = event.register("generate_lens", Set.of(GENERATE_PLATE), Set.of());

		CAN_MORTAR = event.register("can_mortar", Set.of(), Set.of(NCMaterialTraits.DUST));
		NO_SMELTING = event.register("no_smelting", Set.of(), Set.of(NCMaterialTraits.DUST));
		NO_DECOMPOSE = event.register("no_decompose");
		NO_HANDLING = event.register("no_handling");
		CAN_ELECTROLYZE = event.register("can_electrolyze");
		CAN_CENTRIFUGE = event.register("can_centrifuge");

		METAL_DEFAULT.add(GENERATE_PLATE);
		METAL_EXTRA.addAll(METAL_DEFAULT);
		METAL_EXTRA.add(GENERATE_ROD);
		METAL_EXTRA2.addAll(METAL_EXTRA);
		METAL_EXTRA2.add(GENERATE_BOLT_AND_SCREW);
		METAL_ALL.addAll(METAL_EXTRA2);
		METAL_ALL.addAll(List.of(GENERATE_FRAME_BOX, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_FRAME_BOX, GENERATE_FOIL, GENERATE_RING, GENERATE_ROTOR));
	}

	private ConductanceMaterialFlags() {
	}
}
