package conductance.init.material;

import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTextureSets;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL;
import static conductance.api.NCMaterialTextureSets.FINE;
import static conductance.api.NCMaterialTextureSets.ROUGH;
import static conductance.api.NCMaterials.GLASS;
import static conductance.api.NCMaterials.NETHER_STAR;
import static conductance.api.NCMaterials.STONE;
import static conductance.api.NCMaterials.WOOD;

@ConductancePluginListener(modid = Conductance.MODID)
public final class MaterialLoader5 {

	@EventListener(priority = -90)
	private static void initialize(final RegisterMaterialEvent event) {
		WOOD = event.register("wood", b -> b
			// .wood() TODO implement wood types
			.dust().plate().rod().boltAndScrew().gear()
			.style(0x643200, NCMaterialTextureSets.WOOD)
		);
		STONE = event.register("stone", b -> b
			.dust().plate().rod()
			.style(0x8F8F8F, ROUGH)
		);
		GLASS = event.register("glass", b -> b
			.dust().plate().rod().gear()
			.style(0xFAFAFA, FINE)
		);
		NETHER_STAR = event.register("nether_star", b -> b
			.dust().gem().plate().rod().boltAndScrew().gear().gearSmall()
			.ore()
			.style(0xFFFFFF, NCMaterialTextureSets.NETHER_STAR)
			.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
		);
	}

	private MaterialLoader5() {
	}
}
