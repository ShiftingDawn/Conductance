package conductance.init;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import conductance.api.material.Material;
import conductance.api.material.event.RegisterMaterialFlagEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.DUST;
import static conductance.api.NCMaterialFlags.GEM;
import static conductance.api.NCMaterialFlags.INGOT;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialFlags {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialFlagEvent event) {
		DUST = event.register("dust");
		INGOT = event.register("ingot", Set.of(DUST), ConductanceMaterialFlags::validateIngotOrGem);
		GEM = event.register("gem", Set.of(DUST));
	}

	@Nullable
	private static List<String> validateIngotOrGem(final Material material) {
		if (material.hasFlag(INGOT) && material.hasFlag(GEM)) {
			return List.of("Material cannot have flags for both ingot and gem");
		}
		return null;
	}

	private ConductanceMaterialFlags() {
	}
}
