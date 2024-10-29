package conductance.init.material;

import net.minecraft.tags.BlockTags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureSets;
import conductance.api.plugin.MaterialRegister;
import static net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL;
import static net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL;
import static net.minecraft.tags.BlockTags.NEEDS_STONE_TOOL;
import static conductance.api.NCMaterialFlags.CAN_CENTRIFUGE;
import static conductance.api.NCMaterialFlags.CAN_CRYSTALLIZE;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_BLOCK;
import static conductance.api.NCMaterialFlags.GENERATE_BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.GENERATE_FINE_WIRE;
import static conductance.api.NCMaterialFlags.GENERATE_FOIL;
import static conductance.api.NCMaterialFlags.GENERATE_FRAME;
import static conductance.api.NCMaterialFlags.GENERATE_GEAR;
import static conductance.api.NCMaterialFlags.GENERATE_LENS;
import static conductance.api.NCMaterialFlags.GENERATE_PLATE;
import static conductance.api.NCMaterialFlags.GENERATE_RING;
import static conductance.api.NCMaterialFlags.GENERATE_ROD;
import static conductance.api.NCMaterialFlags.METAL_ALL;
import static conductance.api.NCMaterialFlags.METAL_EXTRA;
import static conductance.api.NCMaterialFlags.METAL_EXTRA2;
import static conductance.api.NCMaterialFlags.NO_DECOMPOSE;
import static conductance.api.NCMaterialFlags.NO_SMELTING;
import static conductance.api.NCMaterials.ALBITE_FELDSPAR;
import static conductance.api.NCMaterials.ALMANDINE;
import static conductance.api.NCMaterials.ALUMINIUM;
import static conductance.api.NCMaterials.ANDRADITE;
import static conductance.api.NCMaterials.ANORTHITE_FELDSPAR;
import static conductance.api.NCMaterials.ANTIMONY;
import static conductance.api.NCMaterials.APATITE;
import static conductance.api.NCMaterials.ASBESTOS;
import static conductance.api.NCMaterials.BAUXITE;
import static conductance.api.NCMaterials.BERYLLIUM;
import static conductance.api.NCMaterials.BIOTITE;
import static conductance.api.NCMaterials.BISMUTH;
import static conductance.api.NCMaterials.BISMUTH_BRONZE;
import static conductance.api.NCMaterials.BLACK_BRONZE;
import static conductance.api.NCMaterials.BONE;
import static conductance.api.NCMaterials.BRASS;
import static conductance.api.NCMaterials.BRONZE;
import static conductance.api.NCMaterials.BROWN_LIMONITE;
import static conductance.api.NCMaterials.CALCITE;
import static conductance.api.NCMaterials.CALCIUM;
import static conductance.api.NCMaterials.CARBON;
import static conductance.api.NCMaterials.CASSITERITE;
import static conductance.api.NCMaterials.CASSITERITE_SAND;
import static conductance.api.NCMaterials.CERTUS_QUARTZ;
import static conductance.api.NCMaterials.CHALCOCITE;
import static conductance.api.NCMaterials.CHALCOPYRITE;
import static conductance.api.NCMaterials.CHARCOAL;
import static conductance.api.NCMaterials.CHARGED_CERTUS_QUARTZ;
import static conductance.api.NCMaterials.CHLORINE;
import static conductance.api.NCMaterials.CHROMIUM;
import static conductance.api.NCMaterials.CINNABAR;
import static conductance.api.NCMaterials.COAL;
import static conductance.api.NCMaterials.COAL_COKE;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.CUPRONICKEL;
import static conductance.api.NCMaterials.DIAMOND;
import static conductance.api.NCMaterials.DISTILLED_WATER;
import static conductance.api.NCMaterials.ELECTRUM;
import static conductance.api.NCMaterials.EMERALD;
import static conductance.api.NCMaterials.ENDER_PEARL;
import static conductance.api.NCMaterials.FLUORINE;
import static conductance.api.NCMaterials.GALENA;
import static conductance.api.NCMaterials.GARNIERITE;
import static conductance.api.NCMaterials.GLAUCONITE;
import static conductance.api.NCMaterials.GOLD;
import static conductance.api.NCMaterials.GOLD_LEACH;
import static conductance.api.NCMaterials.GRAPHENE;
import static conductance.api.NCMaterials.GRAPHITE;
import static conductance.api.NCMaterials.GREEN_SAPPHIRE;
import static conductance.api.NCMaterials.GROSSULAR;
import static conductance.api.NCMaterials.HEMATITE;
import static conductance.api.NCMaterials.HYDROGEN;
import static conductance.api.NCMaterials.ICE;
import static conductance.api.NCMaterials.ILMENITE;
import static conductance.api.NCMaterials.INVAR;
import static conductance.api.NCMaterials.IRIDIUM;
import static conductance.api.NCMaterials.IRON;
import static conductance.api.NCMaterials.KANTHAL;
import static conductance.api.NCMaterials.KYANITE;
import static conductance.api.NCMaterials.LAZURITE;
import static conductance.api.NCMaterials.LEAD;
import static conductance.api.NCMaterials.LEPIDOLITE;
import static conductance.api.NCMaterials.LIGNITE_COAL;
import static conductance.api.NCMaterials.LITHIUM;
import static conductance.api.NCMaterials.MAGNESITE;
import static conductance.api.NCMaterials.MAGNESIUM;
import static conductance.api.NCMaterials.MAGNETIC_IRON;
import static conductance.api.NCMaterials.MAGNETIC_NEODYMIUM;
import static conductance.api.NCMaterials.MAGNETIC_SAMARIUM;
import static conductance.api.NCMaterials.MAGNETIC_STEEL;
import static conductance.api.NCMaterials.MAGNETITE;
import static conductance.api.NCMaterials.MANGANESE;
import static conductance.api.NCMaterials.MERCURY;
import static conductance.api.NCMaterials.MICA;
import static conductance.api.NCMaterials.MOLYBDENITE;
import static conductance.api.NCMaterials.MOLYBDENUM;
import static conductance.api.NCMaterials.NEODYMIUM;
import static conductance.api.NCMaterials.NETHER_QUARTZ;
import static conductance.api.NCMaterials.NICHROME;
import static conductance.api.NCMaterials.NICKEL;
import static conductance.api.NCMaterials.NIOBIUM;
import static conductance.api.NCMaterials.NITROGEN;
import static conductance.api.NCMaterials.OBSIDIAN;
import static conductance.api.NCMaterials.OSMIRIDIUM;
import static conductance.api.NCMaterials.OSMIUM;
import static conductance.api.NCMaterials.OXYGEN;
import static conductance.api.NCMaterials.PENTLANDITE;
import static conductance.api.NCMaterials.PHOSPHATE;
import static conductance.api.NCMaterials.PHOSPHORUS;
import static conductance.api.NCMaterials.PLATINUM;
import static conductance.api.NCMaterials.POTASSIUM;
import static conductance.api.NCMaterials.POTASSIUM_FELDSPAR;
import static conductance.api.NCMaterials.PRECIOUS_METAL;
import static conductance.api.NCMaterials.PRECIOUS_METAL_RESIDUE;
import static conductance.api.NCMaterials.PYRITE;
import static conductance.api.NCMaterials.PYROCHLORE;
import static conductance.api.NCMaterials.PYROLUSITE;
import static conductance.api.NCMaterials.PYROPE;
import static conductance.api.NCMaterials.QUARTZITE;
import static conductance.api.NCMaterials.ROCK_SALT;
import static conductance.api.NCMaterials.ROSE_GOLD;
import static conductance.api.NCMaterials.RURIDIT;
import static conductance.api.NCMaterials.RUTHENIUM;
import static conductance.api.NCMaterials.RUTILE;
import static conductance.api.NCMaterials.SALT;
import static conductance.api.NCMaterials.SALTPETER;
import static conductance.api.NCMaterials.SAMARIUM;
import static conductance.api.NCMaterials.SAPPHIRE;
import static conductance.api.NCMaterials.SCHEELITE;
import static conductance.api.NCMaterials.SILICON;
import static conductance.api.NCMaterials.SILICON_DIOXIDE;
import static conductance.api.NCMaterials.SILVER;
import static conductance.api.NCMaterials.SOAPSTONE;
import static conductance.api.NCMaterials.SODALITE;
import static conductance.api.NCMaterials.SODIUM;
import static conductance.api.NCMaterials.SOLDERING_ALLOY;
import static conductance.api.NCMaterials.SPESSARTINE;
import static conductance.api.NCMaterials.SPHALERITE;
import static conductance.api.NCMaterials.SPODUMENE;
import static conductance.api.NCMaterials.STAINLESS_STEEL;
import static conductance.api.NCMaterials.STEAM;
import static conductance.api.NCMaterials.STEEL;
import static conductance.api.NCMaterials.STERLING_SILVER;
import static conductance.api.NCMaterials.STIBNITE;
import static conductance.api.NCMaterials.SULFUR;
import static conductance.api.NCMaterials.TALC;
import static conductance.api.NCMaterials.TANTALITE;
import static conductance.api.NCMaterials.TANTALUM;
import static conductance.api.NCMaterials.TETRAHEDRITE;
import static conductance.api.NCMaterials.TIN;
import static conductance.api.NCMaterials.TITANIUM;
import static conductance.api.NCMaterials.TOPAZ;
import static conductance.api.NCMaterials.TPV_ALLOY;
import static conductance.api.NCMaterials.TRICALCIUM_PHOSPHATE;
import static conductance.api.NCMaterials.TUNGSTATE;
import static conductance.api.NCMaterials.TUNGSTEN;
import static conductance.api.NCMaterials.TUNGSTIC_ACID;
import static conductance.api.NCMaterials.URANINITE;
import static conductance.api.NCMaterials.URANIUM_238;
import static conductance.api.NCMaterials.UVAROVITE;
import static conductance.api.NCMaterials.VANADIUM;
import static conductance.api.NCMaterials.WATER;
import static conductance.api.NCMaterials.WROUGHT_IRON;
import static conductance.api.NCMaterials.WULFENITE;
import static conductance.api.NCMaterials.YELLOW_LIMONITE;
import static conductance.api.NCMaterials.ZINC;
import static conductance.api.NCTextureSets.AMETHYST;
import static conductance.api.NCTextureSets.BRIGHT;
import static conductance.api.NCTextureSets.FINE;
import static conductance.api.NCTextureSets.FLINT;
import static conductance.api.NCTextureSets.GEM_HORIZONTAL;
import static conductance.api.NCTextureSets.LIGNITE;
import static conductance.api.NCTextureSets.MAGNETIC;
import static conductance.api.NCTextureSets.METALLIC;
import static conductance.api.NCTextureSets.QUARTZ;
import static conductance.api.NCTextureSets.ROUGH;
import static conductance.api.NCTextureSets.SAND;
import static conductance.api.NCTextureSets.SHINY;

public final class MaterialLoaderFirstOrder {

	//@formatter:off
	public static void init(final MaterialRegister register) {
		MAGNETIC_IRON = register.register("magnetic_iron")
				.ingot()
				.color(200, 200, 200).textureSet(MAGNETIC)
				.addFlagAndPreset(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(IRON)
				.build();
		IRON.getTrait(NCMaterialTraits.INGOT).setMagneticForm(MAGNETIC_IRON);

		ALMANDINE = register.register("almandine")
				.gem()
				.ore(3, 1).color(255, 0, 0)
				.components(IRON, 3, ALUMINIUM, 2, SILICON, 3, OXYGEN, 12)
				.build();

		ANDRADITE = register.register("andradite")
				.gem()
				.color(150, 120, 0).textureSet(NCTextureSets.AMETHYST)
				.components(CALCIUM, 3, IRON, 2, SILICON, 3, OXYGEN, 12)
				.build();

		POTASSIUM_FELDSPAR = register.register("potassium_feldspar")
				.gem()
				.ore()
				.color(0xfa8128)
				.components(POTASSIUM, ALUMINIUM, SILICON, 3, OXYGEN, 8)
				.build();

		ALBITE_FELDSPAR = register.register("albite_feldspar")
				.gem()
				.ore()
				.color(0xb56727)
				.components(SODIUM, ALUMINIUM, SILICON, 3, OXYGEN, 8)
				.build();

		ANORTHITE_FELDSPAR = register.register("anorthite_feldspar")
				.gem()
				.ore()
				.color(0xfda172)
				.components(CALCIUM, ALUMINIUM, 2, SILICON, 2, OXYGEN, 8)
				.build();

		PYRITE = register.register("pyrite")
				.dust()
				.ore().color(150, 120, 40).textureSet(ROUGH)
				.components(IRON, SULFUR, 2)
				.build();
		//TODO ore
//		PYRITE.getTrait(NCMaterialflags.ORE).setOreSmeltResult(IRON);

		BRONZE = register.register("bronze")
				.ingot().liquid(1357)
				.color(255, 128, 0).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(COPPER, 3, TIN)
				.build();

		STEEL = register.register("steel")
				.ingot()
				.liquid(2046)
				.color(128, 128, 128).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR, NO_DECOMPOSE)
				.components(IRON)
				.build();

		MAGNETIC_STEEL = register.register("magnetic_steel")
				.ingot()
				.color(128, 128, 128).textureSet(MAGNETIC)
				.addFlagAndPreset(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(STEEL)
				.build();
		STEEL.getTrait(NCMaterialTraits.INGOT).setMagneticForm(MAGNETIC_STEEL);

		WROUGHT_IRON = register.register("wrought_iron")
				.ingot()
				.liquid(2011)
				.color(200, 180, 180).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR, NO_DECOMPOSE)
				.components(IRON)
				.build();

		STAINLESS_STEEL = register.register("stainless_steel")
				.ingot(NEEDS_IRON_TOOL)
				.liquid(2011)
				.color(200, 200, 220).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(IRON, 6, NICKEL, MANGANESE, CHROMIUM)
				.build();

		WATER = register.register("water")
				.liquid(300)
				.color(0x0000FF)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN)
				.build();

		DISTILLED_WATER = register.register("distilled_water")
				.liquid()
				.color(0x4A94FF)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN)
				.build();

		ICE = register.register("ice")
				.dust()
				.color(0xd2f5fe)
				.flags(GENERATE_BLOCK, NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN)
				.build();

		STEAM = register.register("steam")
				.gas(373)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN)
				.build();

		SILICON_DIOXIDE = register.register("silicon_dioxide")
				.dust()
				.color(0xf2f2f2).textureSet(QUARTZ)
				.components(SILICON, OXYGEN, 2)
				.build();

		DIAMOND = register.register("diamond")
				.gem(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(200, 255, 255).textureSet(NCTextureSets.DIAMOND)
				.flags(GENERATE_ROD, GENERATE_BOLT_AND_SCREW, GENERATE_PLATE, GENERATE_LENS, GENERATE_GEAR, NO_DECOMPOSE)
				.components(CARBON)
				.build();

		ELECTRUM = register.register("electrum")
				.ingot()
				.liquid(1285)
				.color(255, 255, 100).textureSet(SHINY)
				.addFlagAndPreset(METAL_EXTRA2, CAN_MORTAR, GENERATE_FINE_WIRE, GENERATE_RING)
				.components(SILVER, GOLD)
				.build();

		EMERALD = register.register("emerald")
				.gem()
				.ore(2, 1)
				.color(80, 255, 80).textureSet(NCTextureSets.EMERALD)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_LENS, NO_SMELTING)
				.components(BERYLLIUM, 3, ALUMINIUM, 2, SILICON, 6, OXYGEN, 18)
				.build();

		GALENA = register.register("galena")
				.dust()
				.ore().color(100, 60, 100).textureSet(METALLIC)
				.flags(NO_SMELTING)
				.components(LEAD, SULFUR)
				.build();

		GARNIERITE = register.register("garnierite")
				.dust()
				.ore().color(50, 200, 70).textureSet(METALLIC)
				.components(NICKEL, OXYGEN)
				.build();

		GREEN_SAPPHIRE = register.register("green_sapphire")
				.gem()
				.ore()
				.color(100, 200, 130).textureSet(GEM_HORIZONTAL)
				.addFlagAndPreset(METAL_EXTRA, NO_SMELTING)
				.components(ALUMINIUM, 2, OXYGEN, 3)
				.build();

		GROSSULAR = register.register("grossular")
				.gem().ore(3, 1)
				.color(200, 100, 0).textureSet(AMETHYST)
				.components(CALCIUM, 3, ALUMINIUM, 2, SILICON, 3, OXYGEN, 12)
				.build();

		ILMENITE = register.register("ilmenite")
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(70, 55, 50).textureSet(METALLIC)
				.flags(NO_DECOMPOSE)
				.components(IRON, TITANIUM, OXYGEN, 3)
				.build();

		RUTILE = register.register("rutile")
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(212, 13, 92).textureSet(GEM_HORIZONTAL)
				.flags(NO_SMELTING)
				.components(TITANIUM, OXYGEN, 2)
				.build();

		BAUXITE = register.register("bauxite")
				.dust()
				.ore()
				.color(200, 100, 0)
				.components(ALUMINIUM, 2, OXYGEN, 3)
				.build();

		INVAR = register.register("invar")
				.ingot()
				.liquid(1916)
				.color(180, 180, 120).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2, CAN_MORTAR, GENERATE_GEAR, GENERATE_FRAME)
				.components(IRON, 2, NICKEL)
				.build();

		LAZURITE = register.register("lazurite")
				.gem()
				.ore(6, 4)
				.color(100, 120, 255).textureSet(NCTextureSets.LAPIS)
				.flags(GENERATE_PLATE, GENERATE_ROD, CAN_CRYSTALLIZE)
				.components(ALUMINIUM, 6, SILICON, 6, CALCIUM, 8, SODIUM, 8)
				.build();

		MAGNETITE = register.register("magnetite")
				.dust()
				.ore()
				.color(30, 30, 30).textureSet(METALLIC)
				.components(IRON, 3, OXYGEN, 4)
				.build();

		MAGNESITE = register.register("magnesite")
				.dust()
				.ore()
				.color(250, 250, 180).textureSet(ROUGH)
				.components(MAGNESIUM, CARBON, OXYGEN, 3)
				.build();

		MOLYBDENITE = register.register("molybdenite")
				.dust()
				.ore()
				.color(25, 25, 25).textureSet(METALLIC)
				.components(MOLYBDENUM, SULFUR, 2)
				.build();

		OBSIDIAN = register.register("obsidian")
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.color(80, 50, 100).textureSet(SHINY)
				.flags(GENERATE_PLATE)
				.flags(NO_DECOMPOSE)
				.components(MAGNESIUM, IRON, SILICON, 2, OXYGEN, 4)
				.build();

		PHOSPHATE = register.register("phosphate")
				.dust()
				.color(55, 255, 0)
				.flags(NO_SMELTING)
				.components(PHOSPHORUS, OXYGEN, 4)
				.build();

		STERLING_SILVER = register.register("sterling_silver")
				.ingot()
				.liquid(1258)
				.color(250, 220, 225).textureSet(SHINY)
				.addFlagAndPreset(METAL_EXTRA2)
				.components(COPPER, SILVER, 4)
				.build();

		ROSE_GOLD = register.register("rose_gold")
				.ingot()
				.liquid(1341)
				.color(255, 230, 30).textureSet(SHINY)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_RING)
				.components(COPPER, GOLD, 4)
				.build();

		BISMUTH_BRONZE = register.register("bismuth_bronze")
				.ingot()
				.liquid(1036)
				.color(100, 125, 125).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2)
				.components(BISMUTH, ZINC, COPPER, 3)
				.build();

		BLACK_BRONZE = register.register("black_bronze")
				.ingot()
				.liquid(1328)
				.color(100, 50, 125).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_GEAR)
				.components(GOLD, SILVER, COPPER, 3)
				.build();

		PYROLUSITE = register.register("pyrolusite")
				.dust()
				.ore()
				.color(150, 150, 170)
				.components(MANGANESE, OXYGEN, 2)
				.build();

		PYROPE = register.register("pyrope")
				.gem()
				.ore(3, 1)
				.color(120, 50, 100).textureSet(AMETHYST)
				.components(ALUMINIUM, 2, MAGNESIUM, 3, SILICON, 3, OXYGEN, 12)
				.build();

		ROCK_SALT = register.register("rock_salt")
				.gem()
				.ore(2, 1)
				.color(240, 200, 200).textureSet(FINE)
				.components(POTASSIUM, CHLORINE)
				.build();

		SALT = register.register("salt")
				.gem()
				.ore(2, 1)
				.color(250, 250, 250).textureSet(FINE)
				.components(SODIUM, CHLORINE)
				.build();

		RURIDIT = register.register("ruridit")
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.color(140, 140, 140).textureSet(BRIGHT)
				.flags(GENERATE_FINE_WIRE, GENERATE_GEAR, GENERATE_FRAME, GENERATE_BOLT_AND_SCREW)
				.components(RUTHENIUM, 2, IRIDIUM)
				.build();

		SALTPETER = register.register("saltpeter")
				.gem()
				.ore(2, 1)
				.color(230, 230, 230).textureSet(FINE)
				.components(POTASSIUM, NITROGEN, OXYGEN, 3)
				.build();

		SAPPHIRE = register.register("sapphire")
				.gem()
				.ore()
				.color(100, 100, 200).textureSet(NCTextureSets.EMERALD)
				.addFlagAndPreset(METAL_EXTRA2, NO_SMELTING, GENERATE_LENS)
				.components(ALUMINIUM, 2, OXYGEN, 3)
				.build();

		SODALITE = register.register("sodalite")
				.gem()
				.ore(6, 4)
				.color(20, 20, 255).textureSet(NCTextureSets.LAPIS)
				.flags(GENERATE_PLATE, GENERATE_ROD, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(ALUMINIUM, 3, SILICON, 3, SODIUM, 4, CHLORINE)
				.build();

		SCHEELITE = register.register("scheelite")
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(200, 140, 20)
				.flags(NO_DECOMPOSE)
				.components(CALCIUM, TUNGSTEN, OXYGEN, 4)
				.formula("Ca(WO3)O")
				.build();

		TANTALITE = register.register("tantalite")
				.dust(NEEDS_IRON_TOOL)
				.ore()
				.color(145, 80, 40).textureSet(METALLIC)
				.components(MANGANESE, TANTALUM, 2, OXYGEN, 6)
				.build();

		COAL_COKE = register.register("coal_coke")
				.gem(NEEDS_IRON_TOOL, 3200)
				.color(0x575e5b).textureSet(NCTextureSets.LIGNITE)
				.flags(NO_SMELTING, CAN_MORTAR)
				.components(CARBON)
				.build();

		SOLDERING_ALLOY = register.register("soldering_alloy")
				.ingot()
				.liquid(544)
				.color(220, 220, 230)
				.components(TIN, 6, LEAD, 3, ANTIMONY)
				.build();

		SPESSARTINE = register.register("spessartine")
				.gem()
				.ore(3, 1)
				.color(255, 100, 100).textureSet(AMETHYST)
				.components(ALUMINIUM, 2, MANGANESE, 3, SILICON, 3, OXYGEN, 12)
				.build();

		SPHALERITE = register.register("sphalerite")
				.dust()
				.ore()
				.color(255, 255, 255)
				.components(ZINC, SULFUR)
				.build();

		STIBNITE = register.register("stibnite")
				.dust().ore()
				.color(70, 70, 70).textureSet(METALLIC)
				.components(ANTIMONY, 2, SULFUR, 3)
				.build();

		TETRAHEDRITE = register.register("tetrahedrite")
				.dust()
				.ore()
				.color(200, 32, 0)
				.components(COPPER, 3, ANTIMONY, SULFUR, 3, IRON)
				.build();

		TOPAZ = register.register("topaz")
				.gem()
				.ore()
				.color(255, 128, 0).textureSet(GEM_HORIZONTAL)
				.addFlagAndPreset(METAL_EXTRA2, NO_SMELTING)
				.components(ALUMINIUM, 2, SILICON, FLUORINE, HYDROGEN, 2)
				.build();

		TUNGSTATE = register.register("tungstate")
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(55, 50, 35)
				.flags(NO_DECOMPOSE)
				.components(TUNGSTEN, LITHIUM, 2, OXYGEN, 4)
				.formula("Li2(WO3)O")
				.build();

		URANINITE = register.register("uraninite")
				.dust(NEEDS_IRON_TOOL)
				.ore(true)
				.color(35, 35, 35).textureSet(METALLIC)
				.components(URANIUM_238, OXYGEN, 2)
				.formula("UO2")
				.build();

		WULFENITE = register.register("wulfenite")
				.dust(NEEDS_IRON_TOOL)
				.ore()
				.color(255, 128, 0)
				.components(LEAD, MOLYBDENUM, OXYGEN, 4)
				.build();

		NETHER_QUARTZ = register.register("nether_quartz")
				.gem()
				.ore(2, 1)
				.color(230, 210, 210).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(SILICON, OXYGEN, 2)
				.build();

		CERTUS_QUARTZ = register.register("certus_quartz")
				.gem()
				.ore(2, 1)
				.color(210, 210, 230).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_AND_SCREW, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(SILICON, OXYGEN, 2)
				.build();

		CHARGED_CERTUS_QUARTZ = register.register("charged_certus_quartz")
				.gem()
				.color(184, 184, 255).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_AND_SCREW, NO_SMELTING)
				.components(SILICON, OXYGEN, 2)
				.build();

		QUARTZITE = register.register("quartzite")
				.gem()
				.ore(2, 1)
				.color(210, 230, 210).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(SILICON, OXYGEN, 2)
				.build();

		GRAPHITE = register.register("graphite")
				.ore()
				.color(128, 128, 128)
				.flags(NO_SMELTING, NO_DECOMPOSE)
				.components(CARBON)
				.build();

		GRAPHENE = register.register("graphene")
				.dust()
				.color(128, 128, 128).textureSet(SHINY)
				.flags(GENERATE_FOIL, NO_DECOMPOSE)
				.components(CARBON)
				.build();

		TUNGSTIC_ACID = register.register("tungstic_acid")
				.dust()
				.color(0xfffc03).textureSet(SHINY)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, TUNGSTEN, OXYGEN, 4)
				.build();

		OSMIRIDIUM = register.register("osmiridium")
				.ingot(NEEDS_DIAMOND_TOOL)
				.liquid(3012)
				.color(100, 100, 255).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(IRIDIUM, 2, OSMIUM)
				.build();

		CHALCOCITE = register.register("chalcocite")
				.dust()
				.ore()
				.color(0x657882).textureSet(NCTextureSets.EMERALD)
				.components(COPPER, 2, SULFUR)
				.build();

		CHALCOPYRITE = register.register("chalcopyrite")
				.dust()
				.ore()
				.color(160, 120, 40)
				.components(COPPER, IRON, SULFUR, 2)
				.build();
		//TODO ore
//		CHALCOCITE.getType(MaterialTypes.ORE).setOreSmeltResult(COPPER);

		CUPRONICKEL = register.register("cupronickel")
				.ingot()
				.liquid(1542)
				.color(227, 150, 128).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL)
				.components(COPPER, NICKEL)
				.build();

		COAL = register.register("coal")
				.gem(NEEDS_STONE_TOOL, 1600)
				.ore(2, 1)
				.color(0x393e41).textureSet(LIGNITE)
				.flags(NO_SMELTING, CAN_MORTAR)
				.components(CARBON)
				.build();

		CHARCOAL = register.register("charcoal")
				.gem(NEEDS_STONE_TOOL, 1600)
				.color(0x7d6f58).textureSet(FINE)
				.flags(NO_SMELTING, CAN_MORTAR)
				.components(CARBON)
				.build();

		YELLOW_LIMONITE = register.register("yellow_limonite")
				.dust()
				.ore()
				.color(200, 200, 0).textureSet(METALLIC)
				.flags(CAN_CENTRIFUGE)
				.components(IRON, OXYGEN)
				.build();

		BROWN_LIMONITE = register.register("brown_limonite")
				.dust()
				.ore()
				.color(200, 100, 0).textureSet(METALLIC)
				.flags(CAN_CENTRIFUGE)
				.components(IRON, HYDROGEN, OXYGEN)
				.build();

		MICA = register.register("mica")
				.dust()
				.ore()
				.color(195, 195, 205).textureSet(FINE)
				.components(POTASSIUM, ALUMINIUM, 3, SILICON, 3, FLUORINE, 2, OXYGEN, 10)
				.build();

		KYANITE = register.register("kyanite")
				.dust()
				.ore()
				.color(110, 110, 250).textureSet(FLINT)
				.components(ALUMINIUM, 2, SILICON, OXYGEN, 5)
				.build();

		SOAPSTONE = register.register("soapstone")
				.dust()
				.ore()
				.color(95, 145, 95)
				.components(MAGNESIUM, 3, SILICON, 4, HYDROGEN, 2, OXYGEN, 12)
				.build();

		TALC = register.register("talc")
				.dust()
				.ore()
				.color(90, 180, 90)
				.components(MAGNESIUM, 3, SILICON, 4, HYDROGEN, 2, OXYGEN, 12)
				.build();

		CASSITERITE = register.register("cassiterite")
				.dust()
				.ore()
				.color(220, 220, 220).textureSet(METALLIC)
				.components(TIN, OXYGEN, 2)
				.build();

		CASSITERITE_SAND = register.register("cassiterite_sand")
				.dust()
				.ore()
				.color(220, 220, 220).textureSet(SAND)
				.components(TIN, OXYGEN, 2)
				.build();

		UVAROVITE = register.register("uvarovite")
				.gem()
				.color(180, 255, 180).textureSet(NCTextureSets.DIAMOND)
				.components(CALCIUM, 3, CHROMIUM, 2, SILICON, 3, OXYGEN, 12)
				.build();

		ASBESTOS = register.register("asbestos")
				.dust()
				.ore(3, 1)
				.color(180, 255, 180)
				.components(MAGNESIUM, 3, SILICON, 2, HYDROGEN, 4, OXYGEN, 9)
				.build();

		HEMATITE = register.register("hematite")
				.dust()
				.ore()
				.color(0x330817)
				.components(IRON, 2, OXYGEN, 3)
				.build();

		SPODUMENE = register.register("spodumene")
				.dust()
				.ore()
				.color(190, 170, 170)
				.components(LITHIUM, ALUMINIUM, SILICON, 2, OXYGEN, 6)
				.build();

		LEPIDOLITE = register.register("lepidolite")
				.dust()
				.ore()
				.color(190, 170, 170)
				.components(POTASSIUM, LITHIUM, 3, ALUMINIUM, 4, FLUORINE, 2, OXYGEN, 10)
				.build();

		CALCITE = register.register("calcite")
				.dust()
				.ore()
				.color(250, 230, 220)
				.flags(GENERATE_BLOCK)
				.components(CALCIUM, CARBON, OXYGEN, 3)
				.build();

		KANTHAL = register.register("kanthal")
				.ingot()
				.liquid(1708)
				.color(194, 210, 223).textureSet(SHINY)
				.addFlagAndPreset(METAL_EXTRA)
				.components(IRON, ALUMINIUM, CHROMIUM)
				.build();

		BRASS = register.register("brass")
				.ingot()
				.liquid(1160)
				.color(255, 180, 0).textureSet(SHINY)
				.addFlagAndPreset(METAL_EXTRA2, CAN_MORTAR)
				.components(ZINC, COPPER, 3)
				.build();

		ENDER_PEARL = register.register("ender_pearl")
				.gem()
				.color(108, 220, 200).textureSet(SHINY)
				.flags(NO_SMELTING, GENERATE_PLATE)
				.components(BERYLLIUM, POTASSIUM, 4, NITROGEN, 5)
				.build();

		PRECIOUS_METAL = register.register("precious_metal")
				.dust()
				.ore()
				.color(120, 120, 5).textureSet(SHINY)
				.flags(NO_DECOMPOSE, NO_SMELTING)
				.formula("ag?au?")
				.build();

		GOLD_LEACH = register.register("gold_leach")
				.liquid()
				.color(120, 120, 5).textureSet(METALLIC)
				.build();

		PRECIOUS_METAL_RESIDUE = register.register("precious_metal_residue")
				.dust()
				.color(40, 40, 5).textureSet(ROUGH)
				.flags(NO_DECOMPOSE)
				.components(LEAD, COPPER, SILVER, NICKEL)
				.build();

		MAGNETIC_NEODYMIUM = register.register("magnetic_neodymium")
				.ingot()
				.color(100, 100, 100).textureSet(MAGNETIC)
				.addFlagAndPreset(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(NEODYMIUM)
				.build();
		NEODYMIUM.getTrait(NCMaterialTraits.INGOT).setMagneticForm(MAGNETIC_NEODYMIUM);

		MAGNETIC_SAMARIUM = register.register("magnetic_samarium")
				.ingot()
				.color(255, 255, 204).textureSet(MAGNETIC)
				.addFlagAndPreset(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(SAMARIUM)
				.build();
		SAMARIUM.getTrait(NCMaterialTraits.INGOT).setMagneticForm(MAGNETIC_SAMARIUM);

		NICHROME = register.register("nichrome")
				.ingot()
				.color(205, 206, 246).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2)
				.components(NICKEL, 5, CHROMIUM)
				.build();

		TPV_ALLOY = register.register("tpv_alloy")
				.ingot()
				.color(250, 170, 250).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_FRAME)
				.components(TITANIUM, 3, PLATINUM, 3, VANADIUM)
				.build();

		CINNABAR = register.register("cinnabar")
				.gem()
				.ore()
				.color(150, 0, 0).textureSet(NCTextureSets.EMERALD)
				.flags(CAN_CENTRIFUGE, CAN_CRYSTALLIZE)
				.components(MERCURY, SULFUR)
				.build();

		PENTLANDITE = register.register("pentlandite")
				.dust()
				.ore()
				.color(165, 150, 5)
				.build();

		GLAUCONITE = register.register("glauconite")
				.dust()
				.ore()
				.color(130, 180, 60)
				.build();

		LIGNITE_COAL = register.register("lignite_coal")
				.gem(NEEDS_STONE_TOOL, 1200)
				.ore(2, 1)
				.color(100, 70, 70).textureSet(LIGNITE)
				.flags(NO_SMELTING, CAN_MORTAR, NO_DECOMPOSE)
				.components(CARBON)
				.build();

		APATITE = register.register("apatite")
				.gem()
				.ore(4, 2)
				.color(200, 200, 255).textureSet(NCTextureSets.DIAMOND)
				.flags(NO_SMELTING, CAN_CRYSTALLIZE, GENERATE_ROD, GENERATE_BOLT_AND_SCREW, NO_DECOMPOSE)
				.components(CALCIUM, 5, PHOSPHATE, 3, CHLORINE)
				.build();

		TRICALCIUM_PHOSPHATE = register.register("tricalcium_phosphate")
				.gem()
				.ore(3, 1)
				.color(255, 255, 0).textureSet(FLINT)
				.flags(CAN_CENTRIFUGE, NO_SMELTING)
				.components(CALCIUM, 3, PHOSPHATE, 2)
				.build();

		PYROCHLORE = register.register("pyrochlore")
				.dust()
				.ore()
				.color(43, 17, 0).textureSet(METALLIC)
				.components(CALCIUM, 2, NIOBIUM, 2, OXYGEN, 7)
				.build();

		BIOTITE = register.register("biotite")
				.dust()
				.color(20, 30, 20).textureSet(METALLIC)
				.components(POTASSIUM, MAGNESIUM, 3, ALUMINIUM, 3, FLUORINE, 3, SILICON, 4, OXYGEN, 8)
				.build();

		BONE = register.register("bone")
				.dust()
				.color(250, 250, 250)
				.components(CALCIUM)
				.build();
	}
	//@formatter:on

	private MaterialLoaderFirstOrder() {
	}
}
