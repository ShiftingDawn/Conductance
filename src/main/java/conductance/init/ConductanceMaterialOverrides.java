package conductance.init;

import net.minecraft.world.item.Items;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.material.event.RegisterMaterialOverridesEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialOverrides {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialOverridesEvent event) {
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.INGOT, Items.IRON_INGOT);

		event.add(NCMaterials.COAL, NCMaterialGenerationHandlers.GEM, Items.COAL);

		event.add(NCMaterials.DIAMOND, NCMaterialGenerationHandlers.GEM, Items.DIAMOND);
	}

	private ConductanceMaterialOverrides() {
	}
}
