package conductance.init;

import conductance.api.NCMaterialFlags;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialGenerationHandlers.BLOCK;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.INGOT;
import static conductance.api.NCMaterialGenerationHandlers.NUGGET;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.ROD;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialGenerationHandlers {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialGenerationHandlerEvent event) {
		DUST = event.register("dust", b -> b
				.groupTag("c:dusts")
				.entryTag("c:dusts/%s")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.DUST)
		);
		INGOT = event.register("ingot", b -> b
				.groupTag("c:ingots")
				.entryTag("c:ingots/%s")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.INGOT)
		);
		GEM = event.register("gem", "%s", b -> b
				.groupTag("c:gems")
				.entryTag("c:gems/%s")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEM)
		);

		BLOCK = event.register("block", "%s_block", b -> b
				.groupTag("c:storage_blocks")
				.entryTag("c:storage_blocks/%s")
				.setHasBlock(true, true, true)
				.requiredFlag(NCMaterialFlags.BLOCK)
		);
		NUGGET = event.register("nugget", "%s_nugget", b -> b
				.groupTag("c:nuggets")
				.entryTag("c:nuggets/%s")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.INGOT)
		);

		PLATE = event.register("plate", "%s_plate", b -> b
				.groupTag("c:plates")
				.entryTag("c:plates/%s")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.PLATE)
		);
		ROD = event.register("rod", "%s_rod", b -> b
				.groupTag("c:rods")
				.entryTag("c:rods/%s")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.ROD)
		);
	}

	private ConductanceMaterialGenerationHandlers() {
	}
}
