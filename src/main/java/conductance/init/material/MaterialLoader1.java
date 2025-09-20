package conductance.init.material;

import net.minecraft.world.item.Items;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterialTextureSets;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.api.resource.event.RegisterTagEvent;
import conductance.Conductance;
import static net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL;
import static net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL;
import static conductance.api.NCMaterialFlags.BLOCK;
import static conductance.api.NCMaterialProps.BURN_TIME;
import static conductance.api.NCMaterialProps.DEMAGNETIZED_FORM;
import static conductance.api.NCMaterialProps.MAGNETIZED_FORM;
import static conductance.api.NCMaterialProps.REQUIRED_TOOL_LEVEL;
import static conductance.api.NCMaterialTextureSets.AMETHYST;
import static conductance.api.NCMaterialTextureSets.BRIGHT;
import static conductance.api.NCMaterialTextureSets.FINE;
import static conductance.api.NCMaterialTextureSets.FLINT;
import static conductance.api.NCMaterialTextureSets.GEM_HORIZONTAL;
import static conductance.api.NCMaterialTextureSets.LIGNITE;
import static conductance.api.NCMaterialTextureSets.MAGNETIC;
import static conductance.api.NCMaterialTextureSets.METALLIC;
import static conductance.api.NCMaterialTextureSets.QUARTZ;
import static conductance.api.NCMaterialTextureSets.ROUGH;
import static conductance.api.NCMaterialTextureSets.SAND;
import static conductance.api.NCMaterialTextureSets.SHINY;
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
import static conductance.api.NCMaterials.EYE_OF_ENDER;
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

@ConductancePluginListener(modid = Conductance.MODID)
public final class MaterialLoader1 {

	@EventListener(priority = -98)
	private static void initialize(final RegisterMaterialEvent event) {
		MAGNETIC_IRON = event.register("magnetic_iron", b -> b
			.metalDefault().rod()
			.color(0xC8C8C8).textureSet(MAGNETIC)
			.prop(DEMAGNETIZED_FORM, () -> IRON)
			.components(IRON)
		);
		ALMANDINE = event.register("almandine", b -> b
			.dust().gem()
			.ore(3, 1)
			.color(0xFF0000)
			.components(IRON, 3, ALUMINIUM, 2, SILICON, 3, OXYGEN, 12)
		);
		ANDRADITE = event.register("andradite", b -> b
			.dust().gem()
			.style(0x967800, AMETHYST)
			.components(CALCIUM, 3, IRON, 2, SILICON, 3, OXYGEN, 12)
		);
		POTASSIUM_FELDSPAR = event.register("potassium_feldspar", b -> b
			.dust().gem()
			.ore()
			.color(0xFA8128)
			.components(POTASSIUM, ALUMINIUM, SILICON, 3, OXYGEN, 8)
		);
		ALBITE_FELDSPAR = event.register("albite_feldspar", b -> b
			.dust().gem()
			.ore()
			.color(0xB56727)
			.components(SODIUM, ALUMINIUM, SILICON, 3, OXYGEN, 8)
		);
		ANORTHITE_FELDSPAR = event.register("anorthite_feldspar", b -> b
			.dust().gem()
			.ore()
			.color(0xFDA172)
			.components(CALCIUM, ALUMINIUM, 2, SILICON, 2, OXYGEN, 8)
		);
		PYRITE = event.register("pyrite", b -> b
			.dust()
			.ore(() -> IRON, null)
			.style(0x967828, ROUGH)
			.components(IRON, SULFUR, 2)
		);
		BRONZE = event.register("bronze", b -> b
			.metalAll().fineWire()
			.liquid(1357)
			.style(0xFF8000, METALLIC)
			.components(COPPER, 3, TIN)
		);
		STEEL = event.register("steel", b -> b
			.metalAll().fineWire()
			.liquid(2046)
			.style(0x808080, METALLIC)
			.prop(MAGNETIZED_FORM, () -> MAGNETIC_STEEL)
			.components(IRON)
		);
		MAGNETIC_STEEL = event.register("magnetic_steel", b -> b
			.metalDefault().rod()
			.style(0x808080, MAGNETIC)
			.prop(DEMAGNETIZED_FORM, () -> STEEL)
			.components(STEEL)
		);
		WROUGHT_IRON = event.register("wrought_iron", b -> b
			.metalAll().fineWire()
			.liquid(2011)
			.style(0xC8B4B4, METALLIC)
			.components(IRON)
		);
		STAINLESS_STEEL = event.register("stainless_steel", b -> b
			.metalAll()
			.liquid(2011)
			.color(200, 200, 220).textureSet(METALLIC)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_IRON_TOOL)
			.components(IRON, 6, NICKEL, MANGANESE, CHROMIUM)
		);
		WATER = event.register("water", b -> b
			.liquid(300)
			.color(0x0000FF)
			.components(HYDROGEN, 2, OXYGEN)
		);
		DISTILLED_WATER = event.register("distilled_water", b -> b
			.liquid()
			.color(0x4A94FF)
			.components(HYDROGEN, 2, OXYGEN)
		);
		ICE = event.register("ice", b -> b
			.dust().flag(BLOCK)
			.color(0xD2F5FE)
			.components(HYDROGEN, 2, OXYGEN)
		);
		STEAM = event.register("steam", b -> b
			.gas(373)
			.components(HYDROGEN, 2, OXYGEN)
		);
		SILICON_DIOXIDE = event.register("silicon_dioxide", b -> b
			.dust()
			.style(0xF2F2F2, QUARTZ)
			.components(SILICON, OXYGEN, 2)
		);
		DIAMOND = event.register("diamond", b -> b
			.dust().gem()
			.plate().rod().gear().boltAndScrew()
			.ore()
			.style(0xC8FFFF, NCMaterialTextureSets.DIAMOND)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_IRON_TOOL)
			.components(CARBON)
		);
		ELECTRUM = event.register("electrum", b -> b
			.metalExtra().fineWire()
			.liquid(1285)
			.style(0xFFFF64, SHINY)
			.components(SILVER, GOLD)
		);
		EMERALD = event.register("emerald", b -> b
			.dust().gem()
			.plate().rod().gear()
			.ore(2, 1)
			.style(0x50FF50, NCMaterialTextureSets.EMERALD)
			.components(BERYLLIUM, 3, ALUMINIUM, 2, SILICON, 6, OXYGEN, 18)
		);
		GALENA = event.register("galena", b -> b
			.dust()
			.ore()
			.style(0x643C64, METALLIC)
			.components(LEAD, SULFUR)
		);
		GARNIERITE = event.register("garnierite", b -> b
			.dust()
			.ore()
			.style(0x32C846, METALLIC)
			.components(NICKEL, OXYGEN)
		);
		GREEN_SAPPHIRE = event.register("green_sapphire", b -> b
			.gemDefault()
			.ore()
			.style(0x64C882, GEM_HORIZONTAL)
			.components(ALUMINIUM, 2, OXYGEN, 3)
		);
		GROSSULAR = event.register("grossular", b -> b
			.dust().gem()
			.ore(3, 1)
			.style(0xC86400, AMETHYST)
			.components(CALCIUM, 3, ALUMINIUM, 2, SILICON, 3, OXYGEN, 12)
		);
		ILMENITE = event.register("ilmenite", b -> b
			.dust()
			.ore()
			.style(0x463732, METALLIC)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
			.components(IRON, TITANIUM, OXYGEN, 3)
		);
		RUTILE = event.register("rutile", b -> b
			.dust()
			.ore()
			.style(0xD40D5C, GEM_HORIZONTAL)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
			.components(TITANIUM, OXYGEN, 2)
		);
		BAUXITE = event.register("bauxite", b -> b
			.dust()
			.ore()
			.color(0xC86400)
			.components(ALUMINIUM, 2, OXYGEN, 3)
		);
		INVAR = event.register("invar", b -> b
			.metalExtra()
			.liquid(1916)
			.style(0x78B446, METALLIC)
			.components(IRON, 2, NICKEL)
		);
		LAZURITE = event.register("lazurite", b -> b
			.gemExtra()
			.ore(6, 4)
			.style(0x6478FF, NCMaterialTextureSets.LAPIS)
			.components(ALUMINIUM, 6, SILICON, 6, CALCIUM, 8, SODIUM, 8)
		);
		MAGNETITE = event.register("magnetite", b -> b
			.dust()
			.ore(() -> IRON, null)
			.style(0x1E1E1E, METALLIC)
			.components(IRON, 3, OXYGEN, 4)
		);
		MAGNESITE = event.register("magnesite", b -> b
			.dust()
			.ore(() -> MAGNESIUM, null)
			.style(0xFAFAB4, ROUGH)
			.components(MAGNESIUM, CARBON, OXYGEN, 3)
		);
		MOLYBDENITE = event.register("molybdenite", b -> b
			.dust()
			.ore()
			.style(0x191919, METALLIC)
			.components(MOLYBDENUM, SULFUR, 2)
		);
		OBSIDIAN = event.register("obsidian", b -> b
			.dust().flag(BLOCK).plate()
			.style(0x503264, SHINY)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
			.components(MAGNESIUM, IRON, SILICON, 2, OXYGEN, 4)
		);
		PHOSPHATE = event.register("phosphate", b -> b
			.dust()
			.color(0x37FF00)
			.components(PHOSPHORUS, OXYGEN, 4)
		);
		STERLING_SILVER = event.register("sterling_silver", b -> b
			.metalExtra()
			.liquid(1258)
			.style(0xFADCE1, SHINY)
			.components(COPPER, SILVER, 4)
		);
		ROSE_GOLD = event.register("rose_gold", b -> b
			.metalExtra()
			.liquid(1341)
			.style(0xFFE61E, SHINY)
			.components(COPPER, GOLD, 4)
		);
		BISMUTH_BRONZE = event.register("bismuth_bronze", b -> b
			.metalExtra()
			.liquid(1036)
			.style(0x647D7D, METALLIC)
			.components(BISMUTH, ZINC, COPPER, 3)
		);
		BLACK_BRONZE = event.register("black_bronze", b -> b
			.metalExtra()
			.liquid(1328)
			.style(0x64327D, METALLIC)
			.components(GOLD, SILVER, COPPER, 3)
		);
		PYROLUSITE = event.register("pyrolusite", b -> b
			.dust()
			.ore()
			.color(0x9696AA)
			.components(MANGANESE, OXYGEN, 2)
		);
		PYROPE = event.register("pyrope", b -> b
			.dust().gem()
			.ore(3, 1)
			.style(0x783264, AMETHYST)
			.components(ALUMINIUM, 2, MAGNESIUM, 3, SILICON, 3, OXYGEN, 12)
		);
		ROCK_SALT = event.register("rock_salt", b -> b
			.dust().gem()
			.ore(2, 1)
			.style(0xF0C8C8, FINE)
			.components(POTASSIUM, CHLORINE)
		);
		SALT = event.register("salt", b -> b
			.dust().gem()
			.ore(2, 1)
			.style(0xFAFAFA, FINE)
			.components(SODIUM, CHLORINE)
		);
		RURIDIT = event.register("ruridit", b -> b
			.metalExtra().fineWire()
			.style(0x8C8C8C, BRIGHT)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
			.components(RUTHENIUM, 2, IRIDIUM)
		);
		SALTPETER = event.register("saltpeter", b -> b
			.dust().gem()
			.ore(2, 1)
			.style(0xE6E6E6, FINE)
			.components(POTASSIUM, NITROGEN, OXYGEN, 3)
		);
		SAPPHIRE = event.register("sapphire", b -> b
			.gemExtra()
			.ore()
			.style(0x6464C8, NCMaterialTextureSets.EMERALD)
			.components(ALUMINIUM, 2, OXYGEN, 3)
		);
		SODALITE = event.register("sodalite", b -> b
			.gemExtra()
			.ore(6, 4)
			.style(0x1414FF, NCMaterialTextureSets.LAPIS)
			.components(ALUMINIUM, 3, SILICON, 3, SODIUM, 4, CHLORINE)
		);
		SCHEELITE = event.register("scheelite", b -> b
			.dust()
			.ore()
			.color(0xC88C14)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
			.components(CALCIUM, TUNGSTEN, OXYGEN, 4)
			.chemicalFormula("Ca(WO3)O")
		);
		TANTALITE = event.register("tantalite", b -> b
			.dust()
			.ore()
			.style(0x915028, METALLIC)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_IRON_TOOL)
			.components(MANGANESE, TANTALUM, 2, OXYGEN, 6)
		);
		COAL = event.register("coal", b -> b
			.dust().gem()
			.ore(2, 1)
			.style(0x393E41, LIGNITE)
			.prop(BURN_TIME, 1600)
			.components(CARBON)
		);
		CHARCOAL = event.register("charcoal", b -> b
			.dust().gem(true, false, false)
			.style(0x7D6F58, FINE)
			.prop(BURN_TIME, 1600)
			.components(CARBON)
		);
		COAL_COKE = event.register("coal_coke", b -> b
			.dust().gem()
			.style(0x575E5B, LIGNITE)
			.prop(BURN_TIME, COAL.getProp(BURN_TIME) * 2)
			.components(CARBON)
		);
		SOLDERING_ALLOY = event.register("soldering_alloy", b -> b
			.dust().ingot()
			.liquid(544)
			.color(0xDCDCE6)
			.components(TIN, 6, LEAD, 3, ANTIMONY)
		);
		SPESSARTINE = event.register("spessartine", b -> b
			.dust().gem()
			.ore(3, 1)
			.style(0xFF6464, AMETHYST)
			.components(ALUMINIUM, 2, MANGANESE, 3, SILICON, 3, OXYGEN, 12)
		);
		SPHALERITE = event.register("sphalerite", b -> b
			.dust()
			.ore()
			.color(0xFFFFFF)
			.components(ZINC, SULFUR)
		);
		STIBNITE = event.register("stibnite", b -> b
			.dust()
			.ore()
			.style(0x464646, METALLIC)
			.components(ANTIMONY, 2, SULFUR, 3)
		);
		TETRAHEDRITE = event.register("tetrahedrite", b -> b
			.dust()
			.ore()
			.color(0xC82000)
			.components(COPPER, 3, ANTIMONY, SULFUR, 3, IRON)
		);
		TOPAZ = event.register("topaz", b -> b
			.gemDefault()
			.ore()
			.style(0xFF8000, GEM_HORIZONTAL)
			.components(ALUMINIUM, 2, SILICON, FLUORINE, HYDROGEN, 2)
		);
		TUNGSTATE = event.register("tungstate", b -> b
			.dust()
			.ore()
			.color(0x373223)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
			.components(TUNGSTEN, LITHIUM, 2, OXYGEN, 4)
			.chemicalFormula("Li2(WO3)O")
		);
		URANINITE = event.register("uraninite", b -> b
			.dust()
			.ore(true)
			.style(0x232323, METALLIC)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_IRON_TOOL)
			.components(URANIUM_238, OXYGEN, 2)
			.chemicalFormula("UO2")
		);
		WULFENITE = event.register("wulfenite", b -> b
			.dust()
			.ore()
			.color(0xFF8000)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_IRON_TOOL)
			.components(LEAD, MOLYBDENUM, OXYGEN, 4)
		);
		NETHER_QUARTZ = event.register("nether_quartz", b -> b
			.gemDefault()
			.ore(2, 1)
			.style(0xE6D2D2, QUARTZ)
			.components(SILICON, OXYGEN, 2)
		);
		CERTUS_QUARTZ = event.register("certus_quartz", b -> b
			.gemExtra().boltAndScrew()
			.ore(2, 1)
			.style(0xD2D2E6, QUARTZ)
			.components(SILICON, OXYGEN, 2)
		);
		CHARGED_CERTUS_QUARTZ = event.register("charged_certus_quartz", b -> b
			.gemExtra().boltAndScrew()
			.style(0xB8B8FF, QUARTZ)
			.components(SILICON, OXYGEN, 2)
		);
		QUARTZITE = event.register("quartzite", b -> b
			.gemExtra().boltAndScrew()
			.ore(2, 1)
			.style(0xD2E6D2, QUARTZ)
			.components(SILICON, OXYGEN, 2)
		);
		GRAPHITE = event.register("graphite", b -> b
			.dust().ore()
			.color(0x808080)
			.components(CARBON)
		);
		GRAPHENE = event.register("graphene", b -> b
			.dust().fineWire()
			.style(0x808080, SHINY)
			.components(CARBON)
		);
		TUNGSTIC_ACID = event.register("tungstic_acid", b -> b
			.dust()
			.style(0xFFFC03, SHINY)
			.components(HYDROGEN, 2, TUNGSTEN, OXYGEN, 4)
		);
		OSMIRIDIUM = event.register("osmiridium", b -> b
			.metalAll().fineWire()
			.liquid(3012)
			.style(0x6464FF, METALLIC)
			.prop(REQUIRED_TOOL_LEVEL, NEEDS_DIAMOND_TOOL)
			.components(IRIDIUM, 2, OSMIUM)
		);
		CHALCOCITE = event.register("chalcocite", b -> b
			.dust()
			.ore(() -> COPPER, null)
			.style(0x657882, NCMaterialTextureSets.EMERALD)
			.components(COPPER, 2, SULFUR)
		);
		CHALCOPYRITE = event.register("chalcopyrite", b -> b
			.dust()
			.ore(() -> COPPER, null)
			.color(0xA07828)
			.components(COPPER, IRON, SULFUR, 2)
		);
		CUPRONICKEL = event.register("cupronickel", b -> b
			.metalAll().fineWire()
			.liquid(1542)
			.style(0xE39680, METALLIC)
			.components(COPPER, NICKEL)
		);
		YELLOW_LIMONITE = event.register("yellow_limonite", b -> b
			.dust()
			.ore()
			.style(0xC8C800, METALLIC)
			.components(IRON, OXYGEN)
		);
		BROWN_LIMONITE = event.register("brown_limonite", b -> b
			.dust()
			.ore()
			.style(0xC86400, METALLIC)
			.components(IRON, HYDROGEN, OXYGEN)
		);
		MICA = event.register("mica", b -> b
			.dust()
			.ore()
			.style(0xC3C3CD, FINE)
			.components(POTASSIUM, ALUMINIUM, 3, SILICON, 3, FLUORINE, 2, OXYGEN, 10)
		);
		KYANITE = event.register("kyanite", b -> b
			.dust()
			.ore()
			.style(0x6E6EFA, FLINT)
			.components(ALUMINIUM, 2, SILICON, OXYGEN, 5)
		);
		SOAPSTONE = event.register("soapstone", b -> b
			.dust()
			.ore()
			.color(0x5F915F)
			.components(MAGNESIUM, 3, SILICON, 4, HYDROGEN, 2, OXYGEN, 12)
		);
		TALC = event.register("talc", b -> b
			.dust()
			.ore()
			.color(0x5AB45A)
			.components(MAGNESIUM, 3, SILICON, 4, HYDROGEN, 2, OXYGEN, 12)
		);
		CASSITERITE = event.register("cassiterite", b -> b
			.dust()
			.ore()
			.style(0xDCDCDC, METALLIC)
			.components(TIN, OXYGEN, 2)
		);
		CASSITERITE_SAND = event.register("cassiterite_sand", b -> b
			.dust()
			.ore(2, 1)
			.style(0xDCDCDC, SAND)
			.components(TIN, OXYGEN, 2)
		);
		UVAROVITE = event.register("uvarovite", b -> b
			.dust().gem()
			.style(0xB4FFB4, NCMaterialTextureSets.DIAMOND)
			.components(CALCIUM, 3, CHROMIUM, 2, SILICON, 3, OXYGEN, 12)
		);
		ASBESTOS = event.register("asbestos", b -> b
			.dust()
			.ore(3, 1)
			.color(0xC8FFC8)
			.components(MAGNESIUM, 3, SILICON, 2, HYDROGEN, 4, OXYGEN, 9)
		);
		HEMATITE = event.register("hematite", b -> b
			.dust()
			.ore(() -> IRON, null)
			.color(0x330817)
			.components(IRON, 2, OXYGEN, 3)
		);
		SPODUMENE = event.register("spodumene", b -> b
			.dust()
			.ore()
			.color(0xBEAAAA)
			.components(LITHIUM, ALUMINIUM, SILICON, 2, OXYGEN, 6)
		);
		LEPIDOLITE = event.register("lepidolite", b -> b
			.dust()
			.ore()
			.color(0xBEAAAA)
			.components(POTASSIUM, LITHIUM, 3, ALUMINIUM, 4, FLUORINE, 2, OXYGEN, 10)
		);
		CALCITE = event.register("calcite", b -> b
			.dust().flag(BLOCK)
			.ore()
			.color(0xFAE6DC)
			.components(CALCIUM, CARBON, OXYGEN, 3)
		);
		KANTHAL = event.register("kanthal", b -> b
			.metalExtra().fineWire()
			.liquid(1708)
			.style(0xC2D2DF, SHINY)
			.components(IRON, ALUMINIUM, CHROMIUM)
		);
		BRASS = event.register("brass", b -> b
			.metalExtra()
			.liquid(1160)
			.style(0xFFB400, SHINY)
			.components(ZINC, COPPER, 3)
		);
		ENDER_PEARL = event.register("ender_pearl", b -> b
			.gemExtra()
			.removeFlag(NCMaterialFlags.GEM_FLAWED, NCMaterialFlags.GEM_FLAWLESS, NCMaterialFlags.GEM_EXQUISITE)
			.color(0x6CDCC8).textureSet(SHINY)
			.components(BERYLLIUM, POTASSIUM, 4, NITROGEN, 5)
		);
		PRECIOUS_METAL = event.register("precious_metal", b -> b
			.dust()
			.ore()
			.style(0x787805, SHINY)
			.chemicalFormula("ag?au?")
		);
		GOLD_LEACH = event.register("gold_leach", b -> b
			.liquid()
			.style(0x787805, METALLIC)
		);
		PRECIOUS_METAL_RESIDUE = event.register("precious_metal_residue", b -> b
			.dust()
			.style(0x282805, ROUGH)
			.components(LEAD, COPPER, SILVER, NICKEL)
		);
		MAGNETIC_NEODYMIUM = event.register("magnetic_neodymium", b -> b
			.metalDefault().rod()
			.style(0x646464, MAGNETIC)
			.prop(DEMAGNETIZED_FORM, () -> NEODYMIUM)
			.components(NEODYMIUM)
		);
		MAGNETIC_SAMARIUM = event.register("magnetic_samarium", b -> b
			.metalDefault().rod()
			.style(0xFFFFCC, MAGNETIC)
			.prop(DEMAGNETIZED_FORM, () -> SAMARIUM)
			.components(SAMARIUM)
		);
		NICHROME = event.register("nichrome", b -> b
			.metalExtra().fineWire()
			.style(0xCDCEF6, METALLIC)
			.components(NICKEL, 5, CHROMIUM)
		);
		TPV_ALLOY = event.register("tpv_alloy", b -> b
			.metalDefault().fineWire()
			.style(0xFAAAFA, METALLIC)
			.components(TITANIUM, 3, PLATINUM, 3, VANADIUM)
		);
		CINNABAR = event.register("cinnabar", b -> b
			.dust().gem()
			.ore()
			.style(0x960000, NCMaterialTextureSets.EMERALD)
			.components(MERCURY, SULFUR)
		);
		PENTLANDITE = event.register("pentlandite", b -> b
			.dust()
			.ore()
			.color(0xA59605)
		);
		GLAUCONITE = event.register("glauconite", b -> b
			.dust()
			.ore()
			.color(0x82B43C)
		);
		LIGNITE_COAL = event.register("lignite_coal", b -> b
			.dust().gem()
			.ore(2, 1)
			.style(0x644646, LIGNITE)
			.prop(BURN_TIME, (int) (COAL.getProp(BURN_TIME) * 0.75))
			.components(CARBON)
		);
		APATITE = event.register("apatite", b -> b
			.gemDefault().rod()
			.ore(4, 2)
			.style(0xC8C8FF, NCMaterialTextureSets.DIAMOND)
			.components(CALCIUM, 5, PHOSPHATE, 3, CHLORINE)
		);
		TRICALCIUM_PHOSPHATE = event.register("tricalcium_phosphate", b -> b
			.dust().gem()
			.ore(3, 1)
			.style(0xFFFF00, FLINT)
			.components(CALCIUM, 3, PHOSPHATE, 2)
		);
		PYROCHLORE = event.register("pyrochlore", b -> b
			.dust()
			.ore()
			.style(0x2B1100, METALLIC)
			.components(CALCIUM, 2, NIOBIUM, 2, OXYGEN, 7)
		);
		BIOTITE = event.register("biotite", b -> b
			.dust()
			.style(0x141E14, METALLIC)
			.components(POTASSIUM, MAGNESIUM, 3, ALUMINIUM, 3, FLUORINE, 3, SILICON, 4, OXYGEN, 8)
		);
		BONE = event.register("bone", b -> b
			.dust().rod().flag(BLOCK)
			.color(0xFAFAFA)
			.components(CALCIUM)
		);
	}

	@EventListener(priority = -99)
	private static void addPeriodicTableMaterialTranslations(final AddTranslationEvent event) {
		event.add(TPV_ALLOY, "TPV-Alloy");
	}

	@EventListener
	private static void addCustomTags(final RegisterTagEvent event) {
		event.item(CAPI.materials().getItemTag(CHARCOAL, NCMaterialGenerationHandlers.GEM), Items.CHARCOAL);
		event.item(CAPI.materials().getItemTag(ENDER_PEARL, NCMaterialGenerationHandlers.GEM), Items.ENDER_PEARL);
		event.item(CAPI.materials().getItemTag(EYE_OF_ENDER, NCMaterialGenerationHandlers.GEM), Items.ENDER_EYE);
	}

	private MaterialLoader1() {
	}
}
