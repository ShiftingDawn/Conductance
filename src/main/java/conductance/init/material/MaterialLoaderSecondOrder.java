package conductance.init.material;

import conductance.api.NCTextureSets;
import conductance.api.plugin.MaterialRegister;
import static net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL;
import static net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL;
import static conductance.api.NCMaterialFlags.CAN_CENTRIFUGE;
import static conductance.api.NCMaterialFlags.CAN_CRYSTALLIZE;
import static conductance.api.NCMaterialFlags.CAN_ELECTROLYZE;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.GENERATE_GEAR;
import static conductance.api.NCMaterialFlags.GENERATE_LENS;
import static conductance.api.NCMaterialFlags.GENERATE_PLATE;
import static conductance.api.NCMaterialFlags.GENERATE_ROD;
import static conductance.api.NCMaterialFlags.METAL_ALL;
import static conductance.api.NCMaterialFlags.METAL_EXTRA;
import static conductance.api.NCMaterialFlags.METAL_EXTRA2;
import static conductance.api.NCMaterialFlags.NO_SMELTING;
import static conductance.api.NCMaterials.ALMANDINE;
import static conductance.api.NCMaterials.ALUMINIUM;
import static conductance.api.NCMaterials.AMETHYST;
import static conductance.api.NCMaterials.ANDRADITE;
import static conductance.api.NCMaterials.BIOTITE;
import static conductance.api.NCMaterials.BLAZE;
import static conductance.api.NCMaterials.CALCIUM;
import static conductance.api.NCMaterials.CHROMIUM;
import static conductance.api.NCMaterials.CLAY;
import static conductance.api.NCMaterials.COBALT_BRASS;
import static conductance.api.NCMaterials.DEEPSLATE;
import static conductance.api.NCMaterials.ENDER_EYE;
import static conductance.api.NCMaterials.ENDER_PEARL;
import static conductance.api.NCMaterials.FLINT;
import static conductance.api.NCMaterials.GARNET_SAND;
import static conductance.api.NCMaterials.GROSSULAR;
import static conductance.api.NCMaterials.IRON;
import static conductance.api.NCMaterials.LAPIS;
import static conductance.api.NCMaterials.LAZURITE;
import static conductance.api.NCMaterials.LITHIUM;
import static conductance.api.NCMaterials.OXYGEN;
import static conductance.api.NCMaterials.PYRITE;
import static conductance.api.NCMaterials.PYROPE;
import static conductance.api.NCMaterials.RUBY;
import static conductance.api.NCMaterials.SILICON;
import static conductance.api.NCMaterials.SILICON_DIOXIDE;
import static conductance.api.NCMaterials.SODALITE;
import static conductance.api.NCMaterials.SODIUM;
import static conductance.api.NCMaterials.SPESSARTINE;
import static conductance.api.NCMaterials.STEEL;
import static conductance.api.NCMaterials.SULFUR;
import static conductance.api.NCMaterials.TUNGSTEN;
import static conductance.api.NCMaterials.TUNGSTENSTEEL;
import static conductance.api.NCMaterials.UVAROVITE;
import static conductance.api.NCMaterials.WATER;
import static conductance.api.NCTextureSets.FINE;
import static conductance.api.NCTextureSets.METALLIC;
import static conductance.api.NCTextureSets.ROUGH;
import static conductance.api.NCTextureSets.SAND;
import static conductance.api.NCTextureSets.SHINY;

public final class MaterialLoaderSecondOrder {

	//@formatter:off
	public static void init(final MaterialRegister register) {
		TUNGSTENSTEEL = register.register("tungstensteel")
				.ingot(NEEDS_DIAMOND_TOOL)
				.liquid(2011)
				.color(100, 100, 160).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL)
				.components(TUNGSTEN, STEEL)
				.build();

		RUBY = register.register("ruby")
				.gem()
				.ore()
				.color(255, 100, 100).textureSet(NCTextureSets.AMETHYST)
				.addFlagAndPreset(METAL_EXTRA, GENERATE_LENS)
				.components(CHROMIUM, ALUMINIUM, 2, OXYGEN, 3)
				.build();

		FLINT = register.register("flint")
				.gem()
				.color(0, 32, 64).textureSet(NCTextureSets.FLINT)
				.flags(CAN_MORTAR)
				.components(SILICON_DIOXIDE)
				.build();

		CLAY = register.register("clay")
				.dust()
				.color(200, 200, 220).textureSet(ROUGH)
				.flags(CAN_MORTAR)
				.components(SODIUM, 2, LITHIUM, ALUMINIUM, 2, SILICON, 2, WATER, 6)
				.build();

		GARNET_SAND = register.register("garnet_sand")
				.dust()
				.ore()
				.color(200, 100, 0).textureSet(SAND)
				.components(ALMANDINE, ANDRADITE, GROSSULAR, PYROPE, SPESSARTINE, UVAROVITE)
				.build();

		LAPIS = register.register("lapis")
				.gem()
				.ore(6, 4)
				.color(70, 70, 220).textureSet(NCTextureSets.LAPIS)
				.flags(CAN_ELECTROLYZE, NO_SMELTING, GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_AND_SCREW, CAN_CRYSTALLIZE)
				.components(LAZURITE, 12, SODALITE, 2, PYRITE, CALCIUM)
				.build();

		COBALT_BRASS = register.register("cobalt_brass")
				.ingot()
				.liquid(1202)
				.color(180, 180, 160).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_GEAR)
				.build();

		BLAZE = register.register("blaze")
				.dust()
				.liquid(4000)
				.color(255, 200, 0).textureSet(FINE)
				.flags(NO_SMELTING, CAN_MORTAR, CAN_CENTRIFUGE)
				.components(SULFUR)
				.build();

		ENDER_EYE = register.register("ender_eye")
				.gem()
				.color(160, 250, 230).textureSet(SHINY)
				.flags(NO_SMELTING, GENERATE_PLATE, CAN_CENTRIFUGE)
				.components(ENDER_PEARL, BLAZE)
				.build();

		AMETHYST = register.register("amethyst")
				.gem(NEEDS_IRON_TOOL)
				.ore()
				.color(0x734fbc).textureSet(NCTextureSets.AMETHYST)
				.addFlagAndPreset(METAL_EXTRA, NO_SMELTING, GENERATE_PLATE, GENERATE_LENS)
				.components(SILICON_DIOXIDE, 4, IRON)
				.build();

		DEEPSLATE = register.register("deepslate")
				.dust()
				.color(0x2f2f37).textureSet(ROUGH)
				.flags(CAN_CENTRIFUGE)
				.components(SILICON_DIOXIDE, 4, BIOTITE)
				.build();
	}
	//@formatter:on

	private MaterialLoaderSecondOrder() {
	}
}
