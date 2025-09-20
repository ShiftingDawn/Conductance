package conductance.init;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import conductance.api.material.Material;
import conductance.api.material.event.RegisterMaterialFlagEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.DUST;
import static conductance.api.NCMaterialFlags.FINE_WIRE;
import static conductance.api.NCMaterialFlags.FOIL;
import static conductance.api.NCMaterialFlags.FORCE_BLOCK;
import static conductance.api.NCMaterialFlags.FRAME_BOX;
import static conductance.api.NCMaterialFlags.GEAR;
import static conductance.api.NCMaterialFlags.GEAR_SMALL;
import static conductance.api.NCMaterialFlags.GEM;
import static conductance.api.NCMaterialFlags.INGOT;
import static conductance.api.NCMaterialFlags.PLATE;
import static conductance.api.NCMaterialFlags.RING;
import static conductance.api.NCMaterialFlags.ROD;
import static conductance.api.NCMaterialFlags.ROTOR;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialFlags {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialFlagEvent event) {
		DUST = event.register("dust");
		INGOT = event.register("ingot", Set.of(DUST), ConductanceMaterialFlags::validateIngotOrGem);
		GEM = event.register("gem", Set.of(DUST));

		PLATE = event.register("plate", ConductanceMaterialFlags::validatePlate);
		ROD = event.register("rod", Set.of(DUST));
		GEAR = event.register("gear", ConductanceMaterialFlags::validateGear);
		GEAR_SMALL = event.register("small_gear", ConductanceMaterialFlags::validateSmallGear);
		FOIL = event.register("foil", Set.of(PLATE));
		BOLT_AND_SCREW = event.register("bolt_and_screw", Set.of(ROD));
		RING = event.register("ring", Set.of(ROD));
		ROTOR = event.register("rotor", Set.of(INGOT));
		FINE_WIRE = event.register("fine_wire", Set.of(DUST));
		FRAME_BOX = event.register("frame_box", Set.of(ROD));

		FORCE_BLOCK = event.register("force_block");
	}

	@Nullable
	private static List<String> validateIngotOrGem(final Material material) {
		if (material.hasFlag(INGOT) && material.hasFlag(GEM)) {
			return List.of("Material cannot have flags for both ingot and gem");
		}
		return null;
	}

	@Nullable
	private static List<String> validatePlate(final Material material) {
		if (!material.hasFlag(INGOT) && !material.hasFlag(DUST)) {
			return List.of("Plate flag requires either ingot or dust flag to be present");
		}
		return null;
	}

	@Nullable
	private static List<String> validateGear(final Material material) {
		if (!material.hasFlag(PLATE) && !material.hasFlag(ROD)) {
			return List.of("Gear flag requires plate and rod flags to be present");
		}
		return null;
	}

	@Nullable
	private static List<String> validateSmallGear(final Material material) {
		if (!material.hasFlag(PLATE) && !material.hasFlag(ROD)) {
			return List.of("Small gear flag requires plate and rod flags to be present");
		}
		return null;
	}

	private ConductanceMaterialFlags() {
	}
}
