package conductance.init;

import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.event.RegisterMaterialGenerationHandlerEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.util.TextHelper;
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
				.groupTag("c:dusts", (String) null) //translation handled by NeoForge
				.entryTag("c:dusts/%s", "%s Dusts")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.DUST)
		);
		INGOT = event.register("ingot", b -> b
				.groupTag("c:ingots", (String) null) //translation handled by NeoForge
				.entryTag("c:ingots/%s", "%s Ingots")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.INGOT)
		);
		GEM = event.register("gem", "%s", b -> b
				.groupTag("c:gems", (String) null) //translation handled by NeoForge
				.entryTag("c:gems/%s", "%s Gems")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.GEM)
		);

		BLOCK = event.register("block", b -> b
				.groupTag("c:storage_blocks", (String) null) //translation handled by NeoForge
				.entryTag("c:storage_blocks/%s", "%s Storage Blocks")
				.setHasBlock(true, true, true)
				.requiredFlag(NCMaterialFlags.BLOCK)
		);
		NUGGET = event.register("nugget", b -> b
				.groupTag("c:nuggets", (String) null) //translation handled by NeoForge
				.entryTag("c:nuggets/%s", "%s Nuggets")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.INGOT)
		);

		PLATE = event.register("plate", b -> b
				.groupTag("c:plates", "Plates")
				.entryTag("c:plates/%s", "%s Plates")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.PLATE)
		);
		ROD = event.register("rod", b -> b
				.groupTag("c:rods", (String) null) //translation handled by NeoForge
				.entryTag("c:rods/%s", "%s Rods")
				.setHasItem(true, true)
				.requiredFlag(NCMaterialFlags.ROD)
		);
	}

	@EventListener(priority = -100)
	private static void addMaterialGenerationHandlerTranslations(final AddTranslationEvent event) {
		for (final MaterialGenerationHandler handler : CAPI.regs().materialGenerationHandlers()) {
			event.add(handler.getDescriptionId(), TextHelper.lowerUnderscoreToEnglish(handler.getId().getPath()));
			if (handler.getDescriptionIdSuffixFactory() == null) {
				event.add(handler.getDescriptionId() + ".factory", "%s " + TextHelper.lowerUnderscoreToEnglish(handler.getId().getPath()));
			}
		}
	}

	private ConductanceMaterialGenerationHandlers() {
	}
}
