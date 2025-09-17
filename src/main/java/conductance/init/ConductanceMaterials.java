package conductance.init;

import conductance.api.CAPI;
import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTextureSets;
import conductance.api.NCMaterials;
import conductance.api.NCPeriodicElements;
import conductance.api.material.Material;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.util.TextHelper;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterials {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialEvent event) {
		NCMaterials.IRON = event.register("iron", NCPeriodicElements.IRON, b -> b
				.dust().ingot().block()
				.plate().rod()
				.style(0xd5d5d5, NCMaterialTextureSets.METALLIC)
		);
		NCMaterials.COPPER = event.register("copper", NCPeriodicElements.COPPER, b -> b
				.dust().ingot().block()
				.plate().rod()
				.style(0xe47b55, NCMaterialTextureSets.BRIGHT)
		);
		NCMaterials.GOLD = event.register("gold", NCPeriodicElements.GOLD, b -> b
				.dust().ingot().block()
				.plate().rod()
				.style(0xfaf25e, NCMaterialTextureSets.SHINY)
		);

		NCMaterials.COAL = event.register("coal", b -> b
				.dust().gem().block()
				.prop(NCMaterialProps.BURN_TIME, 1600)
				.style(0x2d2d2d, NCMaterialTextureSets.ROUGH)
		);
		NCMaterials.DIAMOND = event.register("diamond", b -> b
				.dust().gem().block()
				.plate().rod()
				.style(0x49ead6, NCMaterialTextureSets.DIAMOND)
		);
	}

	@EventListener(priority = -100)
	private static void addMaterialTranslations(final AddTranslationEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			event.add(material, TextHelper.lowerUnderscoreToEnglish(material.getName()));
		}
	}

	private ConductanceMaterials() {
	}
}
