package conductance.init.material;

import conductance.api.NCTextureSets;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialEvent;
import conductance.Conductance;
import static net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_BLOCK;
import static conductance.api.NCMaterialFlags.GENERATE_BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.GENERATE_GEAR;
import static conductance.api.NCMaterialFlags.GENERATE_LENS;
import static conductance.api.NCMaterialFlags.GENERATE_PLATE;
import static conductance.api.NCMaterialFlags.GENERATE_ROD;
import static conductance.api.NCMaterialFlags.METAL_ALL;
import static conductance.api.NCMaterialFlags.NO_SMELTING;
import static conductance.api.NCMaterials.CREOSOTE_OIL;
import static conductance.api.NCMaterials.GLASS;
import static conductance.api.NCMaterials.LUBRICANT;
import static conductance.api.NCMaterials.NETHER_STAR;
import static conductance.api.NCMaterials.STONE;
import static conductance.api.NCMaterials.WOOD;
import static conductance.api.NCTextureSets.FINE;
import static conductance.api.NCTextureSets.ROUGH;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialLoaderMisc {

	@EventListener(priority = -90)
	private static void init(final RegisterMaterialEvent register) {
		WOOD = register.register("wood", builder -> builder
				.wood()
				.dust()
				.color(100, 50, 0).textureSet(NCTextureSets.WOOD)
				.flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_BOLT_AND_SCREW));

		STONE = register.register("stone", builder -> builder
				.dust()
				.color(0x8f8f8f).textureSet(ROUGH)
				.flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, CAN_MORTAR));

		GLASS = register.register("glass", builder -> builder
				.dust()
				.color(250, 250, 250).textureSet(FINE)
				.flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, CAN_MORTAR, GENERATE_BLOCK));

		CREOSOTE_OIL = register.register("creosote_oil", builder -> builder
				.burnTime(6400)
				.liquid()
				.color(128, 64, 0));

		LUBRICANT = register.register("lubricant", builder -> builder
				.liquid()
				.color(255, 196, 0));

		NETHER_STAR = register.register("nether_star", builder -> builder
				.gem(NEEDS_DIAMOND_TOOL)
				.ore()
				.color(255, 255, 255).textureSet(NCTextureSets.NETHER_STAR)
				.addFlagAndPreset(METAL_ALL, GENERATE_BLOCK, GENERATE_LENS, NO_SMELTING));
	}

	private MaterialLoaderMisc() {
	}
}
