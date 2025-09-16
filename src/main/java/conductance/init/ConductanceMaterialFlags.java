package conductance.init;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import conductance.api.material.Material;
import conductance.api.material.event.RegisterMaterialFlagEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.BLOCK;
import static conductance.api.NCMaterialFlags.DUST;
import static conductance.api.NCMaterialFlags.GEM;
import static conductance.api.NCMaterialFlags.INGOT;
import static conductance.api.NCMaterialFlags.PLATE;
import static conductance.api.NCMaterialFlags.ROD;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialFlags {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialFlagEvent event) {
		DUST = event.register("dust");
		INGOT = event.register("ingot", Set.of(DUST), ConductanceMaterialFlags::validateIngotOrGem);
		GEM = event.register("gem", Set.of(DUST));
		BLOCK = event.register("storage_block", ConductanceMaterialFlags::validateBlock);

		PLATE = event.register("plate", ConductanceMaterialFlags::validatePlate);
		ROD = event.register("rod", ConductanceMaterialFlags::validateRod);
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
	private static List<String> validateBlock(final Material material) {
		if (!material.hasFlag(INGOT) && !material.hasFlag(GEM) && !material.hasFlag(DUST)) {
			return List.of("Block flag requires either ingot, gem or dust flag to be present");
		}
		return null;
	}

	@Nullable
	private static List<String> validateRod(final Material material) {
		if (!material.hasFlag(INGOT) && !material.hasFlag(GEM)) {
			return List.of("Rod flag requires either ingot or gem flag to be present");
		}
		return null;
	}

	private ConductanceMaterialFlags() {
	}
}
