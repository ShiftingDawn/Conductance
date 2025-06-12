package conductance.init.material;

import net.minecraft.tags.BlockTags;
import conductance.api.NCTextureSets;
import conductance.api.NCTiers;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
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
import static conductance.api.NCMaterialFlags.GENERATE_FRAME_BOX;
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

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialLoaderFirstOrder {

	@EventListener(priority = -98)
	private static void init(final RegisterMaterialEvent register) {
		MAGNETIC_IRON = register.register("magnetic_iron", builder -> builder
				.ingot(() -> new MaterialTraitIngot(null, () -> IRON))
				.color(200, 200, 200).textureSet(MAGNETIC)
				.flags(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(IRON));

		ALMANDINE = register.register("almandine", builder -> builder
				.gem()
				.ore(3, 1).color(255, 0, 0)
				.components(IRON, 3, ALUMINIUM, 2, SILICON, 3, OXYGEN, 12));

		ANDRADITE = register.register("andradite", builder -> builder
				.gem()
				.color(150, 120, 0).textureSet(NCTextureSets.AMETHYST)
				.components(CALCIUM, 3, IRON, 2, SILICON, 3, OXYGEN, 12));

		POTASSIUM_FELDSPAR = register.register("potassium_feldspar", builder -> builder
				.gem()
				.ore()
				.color(0xfa8128)
				.components(POTASSIUM, ALUMINIUM, SILICON, 3, OXYGEN, 8));

		ALBITE_FELDSPAR = register.register("albite_feldspar", builder -> builder
				.gem()
				.ore()
				.color(0xb56727)
				.components(SODIUM, ALUMINIUM, SILICON, 3, OXYGEN, 8));

		ANORTHITE_FELDSPAR = register.register("anorthite_feldspar", builder -> builder
				.gem()
				.ore()
				.color(0xfda172)
				.components(CALCIUM, ALUMINIUM, 2, SILICON, 2, OXYGEN, 8));

		PYRITE = register.register("pyrite", builder -> builder
				.dust()
				.ore(() -> IRON, null)
				.color(150, 120, 40).textureSet(ROUGH)
				.components(IRON, SULFUR, 2));

		BRONZE = register.register("bronze", builder -> builder
				.ingot().liquid(1357)
				.color(255, 128, 0).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(COPPER, 3, TIN));

		STEEL = register.register("steel", builder -> builder
				.ingot(() -> new MaterialTraitIngot(() -> MAGNETIC_STEEL, null))
				.liquid(2046)
				.color(128, 128, 128).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR, NO_DECOMPOSE)
				.components(IRON));

		MAGNETIC_STEEL = register.register("magnetic_steel", builder -> builder
				.ingot(() -> new MaterialTraitIngot(null, () -> STEEL))
				.color(128, 128, 128).textureSet(MAGNETIC)
				.flags(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(STEEL));

		WROUGHT_IRON = register.register("wrought_iron", builder -> builder
				.ingot()
				.liquid(2011)
				.color(200, 180, 180).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR, NO_DECOMPOSE)
				.components(IRON));

		STAINLESS_STEEL = register.register("stainless_steel", builder -> builder
				.ingot(NEEDS_IRON_TOOL)
				.liquid(2011)
				.color(200, 200, 220).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(IRON, 6, NICKEL, MANGANESE, CHROMIUM));

		WATER = register.register("water", builder -> builder
				.liquid(300)
				.color(0x0000FF)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN));

		DISTILLED_WATER = register.register("distilled_water", builder -> builder
				.liquid()
				.color(0x4A94FF)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN));

		ICE = register.register("ice", builder -> builder
				.dust()
				.color(0xd2f5fe)
				.flags(GENERATE_BLOCK, NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN));

		STEAM = register.register("steam", builder -> builder
				.gas(373)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, OXYGEN));

		SILICON_DIOXIDE = register.register("silicon_dioxide", builder -> builder
				.dust()
				.color(0xf2f2f2).textureSet(QUARTZ)
				.components(SILICON, OXYGEN, 2));

		DIAMOND = register.register("diamond", builder -> builder
				.gem(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(200, 255, 255).textureSet(NCTextureSets.DIAMOND)
				.flags(GENERATE_ROD, GENERATE_BOLT_AND_SCREW, GENERATE_PLATE, GENERATE_LENS, GENERATE_GEAR, NO_DECOMPOSE)
				.components(CARBON));

		ELECTRUM = register.register("electrum", builder -> builder
				.ingot()
				.liquid(1285)
				.wire(NCTiers.HV, 1)
				.color(255, 255, 100).textureSet(SHINY)
				.flags(METAL_EXTRA2, CAN_MORTAR, GENERATE_FINE_WIRE, GENERATE_RING)
				.components(SILVER, GOLD));

		EMERALD = register.register("emerald", builder -> builder
				.gem()
				.ore(2, 1)
				.color(80, 255, 80).textureSet(NCTextureSets.EMERALD)
				.flags(METAL_EXTRA2, GENERATE_LENS, NO_SMELTING)
				.components(BERYLLIUM, 3, ALUMINIUM, 2, SILICON, 6, OXYGEN, 18));

		GALENA = register.register("galena", builder -> builder
				.dust()
				.ore().color(100, 60, 100).textureSet(METALLIC)
				.flags(NO_SMELTING)
				.components(LEAD, SULFUR));

		GARNIERITE = register.register("garnierite", builder -> builder
				.dust()
				.ore().color(50, 200, 70).textureSet(METALLIC)
				.components(NICKEL, OXYGEN));

		GREEN_SAPPHIRE = register.register("green_sapphire", builder -> builder
				.gem()
				.ore()
				.color(100, 200, 130).textureSet(GEM_HORIZONTAL)
				.flags(METAL_EXTRA, NO_SMELTING)
				.components(ALUMINIUM, 2, OXYGEN, 3));

		GROSSULAR = register.register("grossular", builder -> builder
				.gem().ore(3, 1)
				.color(200, 100, 0).textureSet(AMETHYST)
				.components(CALCIUM, 3, ALUMINIUM, 2, SILICON, 3, OXYGEN, 12));

		ILMENITE = register.register("ilmenite", builder -> builder
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(70, 55, 50).textureSet(METALLIC)
				.flags(NO_DECOMPOSE)
				.components(IRON, TITANIUM, OXYGEN, 3));

		RUTILE = register.register("rutile", builder -> builder
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(212, 13, 92).textureSet(GEM_HORIZONTAL)
				.flags(NO_SMELTING)
				.components(TITANIUM, OXYGEN, 2));

		BAUXITE = register.register("bauxite", builder -> builder
				.dust()
				.ore()
				.color(200, 100, 0)
				.components(ALUMINIUM, 2, OXYGEN, 3));

		INVAR = register.register("invar", builder -> builder
				.ingot()
				.liquid(1916)
				.color(0x78b446).textureSet(METALLIC)
				.flags(METAL_EXTRA2, CAN_MORTAR, GENERATE_GEAR, GENERATE_FRAME_BOX)
				.components(IRON, 2, NICKEL));

		LAZURITE = register.register("lazurite", builder -> builder
				.gem()
				.ore(6, 4)
				.color(100, 120, 255).textureSet(NCTextureSets.LAPIS)
				.flags(GENERATE_PLATE, GENERATE_ROD, CAN_CRYSTALLIZE)
				.components(ALUMINIUM, 6, SILICON, 6, CALCIUM, 8, SODIUM, 8));

		MAGNETITE = register.register("magnetite", builder -> builder
				.dust()
				.ore()
				.color(30, 30, 30).textureSet(METALLIC)
				.components(IRON, 3, OXYGEN, 4));

		MAGNESITE = register.register("magnesite", builder -> builder
				.dust()
				.ore()
				.color(250, 250, 180).textureSet(ROUGH)
				.components(MAGNESIUM, CARBON, OXYGEN, 3));

		MOLYBDENITE = register.register("molybdenite", builder -> builder
				.dust()
				.ore()
				.color(25, 25, 25).textureSet(METALLIC)
				.components(MOLYBDENUM, SULFUR, 2));

		OBSIDIAN = register.register("obsidian", builder -> builder
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.color(80, 50, 100).textureSet(SHINY)
				.flags(GENERATE_PLATE)
				.flags(NO_DECOMPOSE)
				.components(MAGNESIUM, IRON, SILICON, 2, OXYGEN, 4));

		PHOSPHATE = register.register("phosphate", builder -> builder
				.dust()
				.color(55, 255, 0)
				.flags(NO_SMELTING)
				.components(PHOSPHORUS, OXYGEN, 4));

		STERLING_SILVER = register.register("sterling_silver", builder -> builder
				.ingot()
				.liquid(1258)
				.color(250, 220, 225).textureSet(SHINY)
				.flags(METAL_EXTRA2)
				.components(COPPER, SILVER, 4));

		ROSE_GOLD = register.register("rose_gold", builder -> builder
				.ingot()
				.liquid(1341)
				.color(255, 230, 30).textureSet(SHINY)
				.flags(METAL_EXTRA2, GENERATE_RING)
				.components(COPPER, GOLD, 4));

		BISMUTH_BRONZE = register.register("bismuth_bronze", builder -> builder
				.ingot()
				.liquid(1036)
				.color(100, 125, 125).textureSet(METALLIC)
				.flags(METAL_EXTRA2)
				.components(BISMUTH, ZINC, COPPER, 3));

		BLACK_BRONZE = register.register("black_bronze", builder -> builder
				.ingot()
				.liquid(1328)
				.color(100, 50, 125).textureSet(METALLIC)
				.flags(METAL_EXTRA2, GENERATE_GEAR)
				.components(GOLD, SILVER, COPPER, 3));

		PYROLUSITE = register.register("pyrolusite", builder -> builder
				.dust()
				.ore()
				.color(150, 150, 170)
				.components(MANGANESE, OXYGEN, 2));

		PYROPE = register.register("pyrope", builder -> builder
				.gem()
				.ore(3, 1)
				.color(120, 50, 100).textureSet(AMETHYST)
				.components(ALUMINIUM, 2, MAGNESIUM, 3, SILICON, 3, OXYGEN, 12));

		ROCK_SALT = register.register("rock_salt", builder -> builder
				.gem()
				.ore(2, 1)
				.color(240, 200, 200).textureSet(FINE)
				.components(POTASSIUM, CHLORINE));

		SALT = register.register("salt", builder -> builder
				.gem()
				.ore(2, 1)
				.color(250, 250, 250).textureSet(FINE)
				.components(SODIUM, CHLORINE));

		RURIDIT = register.register("ruridit", builder -> builder
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.color(140, 140, 140).textureSet(BRIGHT)
				.flags(GENERATE_ROD, GENERATE_FINE_WIRE, GENERATE_GEAR, GENERATE_FRAME_BOX, GENERATE_BOLT_AND_SCREW)
				.components(RUTHENIUM, 2, IRIDIUM));

		SALTPETER = register.register("saltpeter", builder -> builder
				.gem()
				.ore(2, 1)
				.color(230, 230, 230).textureSet(FINE)
				.components(POTASSIUM, NITROGEN, OXYGEN, 3));

		SAPPHIRE = register.register("sapphire", builder -> builder
				.gem()
				.ore()
				.color(100, 100, 200).textureSet(NCTextureSets.EMERALD)
				.flags(METAL_EXTRA2, NO_SMELTING, GENERATE_LENS)
				.components(ALUMINIUM, 2, OXYGEN, 3));

		SODALITE = register.register("sodalite", builder -> builder
				.gem()
				.ore(6, 4)
				.color(20, 20, 255).textureSet(NCTextureSets.LAPIS)
				.flags(GENERATE_PLATE, GENERATE_ROD, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(ALUMINIUM, 3, SILICON, 3, SODIUM, 4, CHLORINE));

		SCHEELITE = register.register("scheelite", builder -> builder
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(200, 140, 20)
				.flags(NO_DECOMPOSE)
				.components(CALCIUM, TUNGSTEN, OXYGEN, 4)
				.formula("Ca(WO3)O"));

		TANTALITE = register.register("tantalite", builder -> builder
				.dust(NEEDS_IRON_TOOL)
				.ore()
				.color(145, 80, 40).textureSet(METALLIC)
				.components(MANGANESE, TANTALUM, 2, OXYGEN, 6));

		COAL_COKE = register.register("coal_coke", builder -> builder
				.gem(NEEDS_IRON_TOOL, 3200)
				.color(0x575e5b).textureSet(NCTextureSets.LIGNITE)
				.flags(NO_SMELTING, CAN_MORTAR)
				.components(CARBON));

		SOLDERING_ALLOY = register.register("soldering_alloy", builder -> builder
				.ingot()
				.liquid(544)
				.color(220, 220, 230)
				.components(TIN, 6, LEAD, 3, ANTIMONY));

		SPESSARTINE = register.register("spessartine", builder -> builder
				.gem()
				.ore(3, 1)
				.color(255, 100, 100).textureSet(AMETHYST)
				.components(ALUMINIUM, 2, MANGANESE, 3, SILICON, 3, OXYGEN, 12));

		SPHALERITE = register.register("sphalerite", builder -> builder
				.dust()
				.ore()
				.color(255, 255, 255)
				.components(ZINC, SULFUR));

		STIBNITE = register.register("stibnite", builder -> builder
				.dust().ore()
				.color(70, 70, 70).textureSet(METALLIC)
				.components(ANTIMONY, 2, SULFUR, 3));

		TETRAHEDRITE = register.register("tetrahedrite", builder -> builder
				.dust()
				.ore()
				.color(200, 32, 0)
				.components(COPPER, 3, ANTIMONY, SULFUR, 3, IRON));

		TOPAZ = register.register("topaz", builder -> builder
				.gem()
				.ore()
				.color(255, 128, 0).textureSet(GEM_HORIZONTAL)
				.flags(METAL_EXTRA2, NO_SMELTING)
				.components(ALUMINIUM, 2, SILICON, FLUORINE, HYDROGEN, 2));

		TUNGSTATE = register.register("tungstate", builder -> builder
				.dust(BlockTags.NEEDS_DIAMOND_TOOL)
				.ore()
				.color(55, 50, 35)
				.flags(NO_DECOMPOSE)
				.components(TUNGSTEN, LITHIUM, 2, OXYGEN, 4)
				.formula("Li2(WO3)O"));

		URANINITE = register.register("uraninite", builder -> builder
				.dust(NEEDS_IRON_TOOL)
				.ore(true)
				.color(35, 35, 35).textureSet(METALLIC)
				.components(URANIUM_238, OXYGEN, 2)
				.formula("UO2"));

		WULFENITE = register.register("wulfenite", builder -> builder
				.dust(NEEDS_IRON_TOOL)
				.ore()
				.color(255, 128, 0)
				.components(LEAD, MOLYBDENUM, OXYGEN, 4));

		NETHER_QUARTZ = register.register("nether_quartz", builder -> builder
				.gem()
				.ore(2, 1)
				.color(230, 210, 210).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(SILICON, OXYGEN, 2));

		CERTUS_QUARTZ = register.register("certus_quartz", builder -> builder
				.gem()
				.ore(2, 1)
				.color(210, 210, 230).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_AND_SCREW, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(SILICON, OXYGEN, 2));

		CHARGED_CERTUS_QUARTZ = register.register("charged_certus_quartz", builder -> builder
				.gem()
				.color(184, 184, 255).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_AND_SCREW, NO_SMELTING)
				.components(SILICON, OXYGEN, 2));

		QUARTZITE = register.register("quartzite", builder -> builder
				.gem()
				.ore(2, 1)
				.color(210, 230, 210).textureSet(QUARTZ)
				.flags(GENERATE_PLATE, NO_SMELTING, CAN_CRYSTALLIZE)
				.components(SILICON, OXYGEN, 2));

		GRAPHITE = register.register("graphite", builder -> builder
				.ore()
				.color(128, 128, 128)
				.flags(NO_SMELTING, NO_DECOMPOSE)
				.components(CARBON));

		GRAPHENE = register.register("graphene", builder -> builder
				.dust()
				.color(128, 128, 128).textureSet(SHINY)
				.flags(GENERATE_FOIL, NO_DECOMPOSE)
				.components(CARBON));

		TUNGSTIC_ACID = register.register("tungstic_acid", builder -> builder
				.dust()
				.color(0xfffc03).textureSet(SHINY)
				.flags(NO_DECOMPOSE)
				.components(HYDROGEN, 2, TUNGSTEN, OXYGEN, 4));

		OSMIRIDIUM = register.register("osmiridium", builder -> builder
				.ingot(NEEDS_DIAMOND_TOOL)
				.liquid(3012)
				.color(100, 100, 255).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.components(IRIDIUM, 2, OSMIUM));

		CHALCOCITE = register.register("chalcocite", builder -> builder
				.dust()
				.ore()
				.color(0x657882).textureSet(NCTextureSets.EMERALD)
				.components(COPPER, 2, SULFUR));

		CHALCOPYRITE = register.register("chalcopyrite", builder -> builder
				.dust()
				.ore(() -> COPPER, null)
				.color(160, 120, 40)
				.components(COPPER, IRON, SULFUR, 2));

		CUPRONICKEL = register.register("cupronickel", builder -> builder
				.ingot()
				.liquid(1542)
				.color(227, 150, 128).textureSet(METALLIC)
				.flags(METAL_ALL)
				.components(COPPER, NICKEL));

		COAL = register.register("coal", builder -> builder
				.gem(NEEDS_STONE_TOOL, 1600)
				.ore(2, 1)
				.color(0x393e41).textureSet(LIGNITE)
				.flags(NO_SMELTING, CAN_MORTAR)
				.components(CARBON));

		CHARCOAL = register.register("charcoal", builder -> builder
				.gem(NEEDS_STONE_TOOL, 1600)
				.color(0x7d6f58).textureSet(FINE)
				.flags(NO_SMELTING, CAN_MORTAR)
				.components(CARBON));

		YELLOW_LIMONITE = register.register("yellow_limonite", builder -> builder
				.dust()
				.ore()
				.color(200, 200, 0).textureSet(METALLIC)
				.flags(CAN_CENTRIFUGE)
				.components(IRON, OXYGEN));

		BROWN_LIMONITE = register.register("brown_limonite", builder -> builder
				.dust()
				.ore()
				.color(200, 100, 0).textureSet(METALLIC)
				.flags(CAN_CENTRIFUGE)
				.components(IRON, HYDROGEN, OXYGEN));

		MICA = register.register("mica", builder -> builder
				.dust()
				.ore()
				.color(195, 195, 205).textureSet(FINE)
				.components(POTASSIUM, ALUMINIUM, 3, SILICON, 3, FLUORINE, 2, OXYGEN, 10));

		KYANITE = register.register("kyanite", builder -> builder
				.dust()
				.ore()
				.color(110, 110, 250).textureSet(FLINT)
				.components(ALUMINIUM, 2, SILICON, OXYGEN, 5));

		SOAPSTONE = register.register("soapstone", builder -> builder
				.dust()
				.ore()
				.color(95, 145, 95)
				.components(MAGNESIUM, 3, SILICON, 4, HYDROGEN, 2, OXYGEN, 12));

		TALC = register.register("talc", builder -> builder
				.dust()
				.ore()
				.color(90, 180, 90)
				.components(MAGNESIUM, 3, SILICON, 4, HYDROGEN, 2, OXYGEN, 12));

		CASSITERITE = register.register("cassiterite", builder -> builder
				.dust()
				.ore()
				.color(220, 220, 220).textureSet(METALLIC)
				.components(TIN, OXYGEN, 2));

		CASSITERITE_SAND = register.register("cassiterite_sand", builder -> builder
				.dust()
				.ore()
				.color(220, 220, 220).textureSet(SAND)
				.components(TIN, OXYGEN, 2));

		UVAROVITE = register.register("uvarovite", builder -> builder
				.gem()
				.color(180, 255, 180).textureSet(NCTextureSets.DIAMOND)
				.components(CALCIUM, 3, CHROMIUM, 2, SILICON, 3, OXYGEN, 12));

		ASBESTOS = register.register("asbestos", builder -> builder
				.dust()
				.ore(3, 1)
				.color(180, 255, 180)
				.components(MAGNESIUM, 3, SILICON, 2, HYDROGEN, 4, OXYGEN, 9));

		HEMATITE = register.register("hematite", builder -> builder
				.dust()
				.ore()
				.color(0x330817)
				.components(IRON, 2, OXYGEN, 3));

		SPODUMENE = register.register("spodumene", builder -> builder
				.dust()
				.ore()
				.color(190, 170, 170)
				.components(LITHIUM, ALUMINIUM, SILICON, 2, OXYGEN, 6));

		LEPIDOLITE = register.register("lepidolite", builder -> builder
				.dust()
				.ore()
				.color(190, 170, 170)
				.components(POTASSIUM, LITHIUM, 3, ALUMINIUM, 4, FLUORINE, 2, OXYGEN, 10));

		CALCITE = register.register("calcite", builder -> builder
				.dust()
				.ore()
				.color(250, 230, 220)
				.flags(GENERATE_BLOCK)
				.components(CALCIUM, CARBON, OXYGEN, 3));

		KANTHAL = register.register("kanthal", builder -> builder
				.ingot()
				.liquid(1708)
				.color(194, 210, 223).textureSet(SHINY)
				.flags(METAL_EXTRA)
				.components(IRON, ALUMINIUM, CHROMIUM));

		BRASS = register.register("brass", builder -> builder
				.ingot()
				.liquid(1160)
				.color(255, 180, 0).textureSet(SHINY)
				.flags(METAL_EXTRA2, CAN_MORTAR)
				.components(ZINC, COPPER, 3));

		ENDER_PEARL = register.register("ender_pearl", builder -> builder
				.gem()
				.color(108, 220, 200).textureSet(SHINY)
				.flags(NO_SMELTING, GENERATE_PLATE)
				.components(BERYLLIUM, POTASSIUM, 4, NITROGEN, 5));

		PRECIOUS_METAL = register.register("precious_metal", builder -> builder
				.dust()
				.ore()
				.color(120, 120, 5).textureSet(SHINY)
				.flags(NO_DECOMPOSE, NO_SMELTING)
				.formula("ag?au?"));

		GOLD_LEACH = register.register("gold_leach", builder -> builder
				.liquid()
				.color(120, 120, 5).textureSet(METALLIC));

		PRECIOUS_METAL_RESIDUE = register.register("precious_metal_residue", builder -> builder
				.dust()
				.color(40, 40, 5).textureSet(ROUGH)
				.flags(NO_DECOMPOSE)
				.components(LEAD, COPPER, SILVER, NICKEL));

		MAGNETIC_NEODYMIUM = register.register("magnetic_neodymium", builder -> builder
				.ingot(() -> new MaterialTraitIngot(null, () -> NEODYMIUM))
				.color(100, 100, 100).textureSet(MAGNETIC)
				.flags(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(NEODYMIUM));

		MAGNETIC_SAMARIUM = register.register("magnetic_samarium", builder -> builder
				.ingot(() -> new MaterialTraitIngot(null, () -> SAMARIUM))
				.color(255, 255, 204).textureSet(MAGNETIC)
				.flags(METAL_EXTRA2, NO_DECOMPOSE, NO_SMELTING)
				.components(SAMARIUM));

		NICHROME = register.register("nichrome", builder -> builder
				.ingot()
				.color(205, 206, 246).textureSet(METALLIC)
				.flags(METAL_EXTRA2)
				.components(NICKEL, 5, CHROMIUM));

		TPV_ALLOY = register.register("tpv_alloy", builder -> builder
				.ingot()
				.color(250, 170, 250).textureSet(METALLIC)
				.flags(METAL_EXTRA2, GENERATE_FRAME_BOX)
				.components(TITANIUM, 3, PLATINUM, 3, VANADIUM));

		CINNABAR = register.register("cinnabar", builder -> builder
				.gem()
				.ore()
				.color(150, 0, 0).textureSet(NCTextureSets.EMERALD)
				.flags(CAN_CENTRIFUGE, CAN_CRYSTALLIZE)
				.components(MERCURY, SULFUR));

		PENTLANDITE = register.register("pentlandite", builder -> builder
				.dust()
				.ore()
				.color(165, 150, 5));

		GLAUCONITE = register.register("glauconite", builder -> builder
				.dust()
				.ore()
				.color(130, 180, 60));

		LIGNITE_COAL = register.register("lignite_coal", builder -> builder
				.gem(NEEDS_STONE_TOOL, 1200)
				.ore(2, 1)
				.color(100, 70, 70).textureSet(LIGNITE)
				.flags(NO_SMELTING, CAN_MORTAR, NO_DECOMPOSE)
				.components(CARBON));

		APATITE = register.register("apatite", builder -> builder
				.gem()
				.ore(4, 2)
				.color(200, 200, 255).textureSet(NCTextureSets.DIAMOND)
				.flags(NO_SMELTING, CAN_CRYSTALLIZE, GENERATE_ROD, GENERATE_BOLT_AND_SCREW, NO_DECOMPOSE)
				.components(CALCIUM, 5, PHOSPHATE, 3, CHLORINE));

		TRICALCIUM_PHOSPHATE = register.register("tricalcium_phosphate", builder -> builder
				.gem()
				.ore(3, 1)
				.color(255, 255, 0).textureSet(FLINT)
				.flags(CAN_CENTRIFUGE, NO_SMELTING)
				.components(CALCIUM, 3, PHOSPHATE, 2));

		PYROCHLORE = register.register("pyrochlore", builder -> builder
				.dust()
				.ore()
				.color(43, 17, 0).textureSet(METALLIC)
				.components(CALCIUM, 2, NIOBIUM, 2, OXYGEN, 7));

		BIOTITE = register.register("biotite", builder -> builder
				.dust()
				.color(20, 30, 20).textureSet(METALLIC)
				.components(POTASSIUM, MAGNESIUM, 3, ALUMINIUM, 3, FLUORINE, 3, SILICON, 4, OXYGEN, 8));

		BONE = register.register("bone", builder -> builder
				.dust()
				.color(250, 250, 250)
				.components(CALCIUM));
	}

	private MaterialLoaderFirstOrder() {
	}
}
