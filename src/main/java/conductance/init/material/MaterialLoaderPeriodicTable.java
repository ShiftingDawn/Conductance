package conductance.init.material;

import net.minecraft.tags.BlockTags;
import conductance.api.NCPeriodicElements;
import conductance.api.plugin.MaterialRegister;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.GENERATE_FINE_WIRE;
import static conductance.api.NCMaterialFlags.GENERATE_FOIL;
import static conductance.api.NCMaterialFlags.GENERATE_GEAR;
import static conductance.api.NCMaterialFlags.GENERATE_RING;
import static conductance.api.NCMaterialFlags.GENERATE_ROD;
import static conductance.api.NCMaterialFlags.GENERATE_ROTOR;
import static conductance.api.NCMaterialFlags.GENERATE_SMALL_GEAR;
import static conductance.api.NCMaterialFlags.METAL_ALL;
import static conductance.api.NCMaterialFlags.METAL_DEFAULT;
import static conductance.api.NCMaterialFlags.METAL_EXTRA;
import static conductance.api.NCMaterialFlags.METAL_EXTRA2;
import static conductance.api.NCMaterials.ACTINIUM;
import static conductance.api.NCMaterials.ALUMINIUM;
import static conductance.api.NCMaterials.AMERICIUM;
import static conductance.api.NCMaterials.ANTIMONY;
import static conductance.api.NCMaterials.ARGON;
import static conductance.api.NCMaterials.ARSENIC;
import static conductance.api.NCMaterials.ASTATINE;
import static conductance.api.NCMaterials.BARIUM;
import static conductance.api.NCMaterials.BERKELIUM;
import static conductance.api.NCMaterials.BERYLLIUM;
import static conductance.api.NCMaterials.BISMUTH;
import static conductance.api.NCMaterials.BOHRIUM;
import static conductance.api.NCMaterials.BORON;
import static conductance.api.NCMaterials.BROMINE;
import static conductance.api.NCMaterials.CADMIUM;
import static conductance.api.NCMaterials.CAESIUM;
import static conductance.api.NCMaterials.CALCIUM;
import static conductance.api.NCMaterials.CALIFORNIUM;
import static conductance.api.NCMaterials.CARBON;
import static conductance.api.NCMaterials.CERIUM;
import static conductance.api.NCMaterials.CHLORINE;
import static conductance.api.NCMaterials.CHROMIUM;
import static conductance.api.NCMaterials.COBALT;
import static conductance.api.NCMaterials.COPERNICIUM;
import static conductance.api.NCMaterials.COPPER;
import static conductance.api.NCMaterials.COSMIC_NEUTRONIUM;
import static conductance.api.NCMaterials.CURIUM;
import static conductance.api.NCMaterials.DARMSTADTIUM;
import static conductance.api.NCMaterials.DEUTERIUM;
import static conductance.api.NCMaterials.DUBNIUM;
import static conductance.api.NCMaterials.DYSPROSIUM;
import static conductance.api.NCMaterials.EINSTEINIUM;
import static conductance.api.NCMaterials.ERBIUM;
import static conductance.api.NCMaterials.EUROPIUM;
import static conductance.api.NCMaterials.FERMIUM;
import static conductance.api.NCMaterials.FLEROVIUM;
import static conductance.api.NCMaterials.FLUORINE;
import static conductance.api.NCMaterials.FRANCIUM;
import static conductance.api.NCMaterials.GADOLINIUM;
import static conductance.api.NCMaterials.GALLIUM;
import static conductance.api.NCMaterials.GERMANIUM;
import static conductance.api.NCMaterials.GOLD;
import static conductance.api.NCMaterials.HAFNIUM;
import static conductance.api.NCMaterials.HASSIUM;
import static conductance.api.NCMaterials.HELIUM;
import static conductance.api.NCMaterials.HELIUM_3;
import static conductance.api.NCMaterials.HOLMIUM;
import static conductance.api.NCMaterials.HYDROGEN;
import static conductance.api.NCMaterials.INDIUM;
import static conductance.api.NCMaterials.INFINITY;
import static conductance.api.NCMaterials.IODINE;
import static conductance.api.NCMaterials.IRIDIUM;
import static conductance.api.NCMaterials.IRON;
import static conductance.api.NCMaterials.KRYPTON;
import static conductance.api.NCMaterials.LANTHANUM;
import static conductance.api.NCMaterials.LAWRENCIUM;
import static conductance.api.NCMaterials.LEAD;
import static conductance.api.NCMaterials.LITHIUM;
import static conductance.api.NCMaterials.LIVERMORIUM;
import static conductance.api.NCMaterials.LUTETIUM;
import static conductance.api.NCMaterials.MAGNESIUM;
import static conductance.api.NCMaterials.MANGANESE;
import static conductance.api.NCMaterials.MEITNERIUM;
import static conductance.api.NCMaterials.MENDELEVIUM;
import static conductance.api.NCMaterials.MERCURY;
import static conductance.api.NCMaterials.MOLYBDENUM;
import static conductance.api.NCMaterials.MOSCOVIUM;
import static conductance.api.NCMaterials.NEODYMIUM;
import static conductance.api.NCMaterials.NEON;
import static conductance.api.NCMaterials.NEPTUNIUM;
import static conductance.api.NCMaterials.NEUTRONIUM;
import static conductance.api.NCMaterials.NICKEL;
import static conductance.api.NCMaterials.NIHONIUM;
import static conductance.api.NCMaterials.NIOBIUM;
import static conductance.api.NCMaterials.NITROGEN;
import static conductance.api.NCMaterials.NOBELIUM;
import static conductance.api.NCMaterials.OGANESSON;
import static conductance.api.NCMaterials.OSMIUM;
import static conductance.api.NCMaterials.OXYGEN;
import static conductance.api.NCMaterials.PALLADIUM;
import static conductance.api.NCMaterials.PHOSPHORUS;
import static conductance.api.NCMaterials.PLATINUM;
import static conductance.api.NCMaterials.PLUTONIUM_239;
import static conductance.api.NCMaterials.PLUTONIUM_241;
import static conductance.api.NCMaterials.POLONIUM;
import static conductance.api.NCMaterials.POTASSIUM;
import static conductance.api.NCMaterials.PRASEODYMIUM;
import static conductance.api.NCMaterials.PROMETHIUM;
import static conductance.api.NCMaterials.PROTACTINIUM;
import static conductance.api.NCMaterials.RADIUM;
import static conductance.api.NCMaterials.RADON;
import static conductance.api.NCMaterials.RHENIUM;
import static conductance.api.NCMaterials.RHODIUM;
import static conductance.api.NCMaterials.ROENTGENIUM;
import static conductance.api.NCMaterials.RUBIDIUM;
import static conductance.api.NCMaterials.RUTHENIUM;
import static conductance.api.NCMaterials.RUTHERFORDIUM;
import static conductance.api.NCMaterials.SAMARIUM;
import static conductance.api.NCMaterials.SCANDIUM;
import static conductance.api.NCMaterials.SEABORGIUM;
import static conductance.api.NCMaterials.SELENIUM;
import static conductance.api.NCMaterials.SILICON;
import static conductance.api.NCMaterials.SILVER;
import static conductance.api.NCMaterials.SODIUM;
import static conductance.api.NCMaterials.SPACETIME;
import static conductance.api.NCMaterials.STRONTIUM;
import static conductance.api.NCMaterials.SULFUR;
import static conductance.api.NCMaterials.TANTALUM;
import static conductance.api.NCMaterials.TECHNETIUM;
import static conductance.api.NCMaterials.TELLURIUM;
import static conductance.api.NCMaterials.TENNESSINE;
import static conductance.api.NCMaterials.TERBIUM;
import static conductance.api.NCMaterials.THALLIUM;
import static conductance.api.NCMaterials.THORIUM;
import static conductance.api.NCMaterials.THULIUM;
import static conductance.api.NCMaterials.TIN;
import static conductance.api.NCMaterials.TITANIUM;
import static conductance.api.NCMaterials.TRITIUM;
import static conductance.api.NCMaterials.TUNGSTEN;
import static conductance.api.NCMaterials.URANIUM_235;
import static conductance.api.NCMaterials.URANIUM_238;
import static conductance.api.NCMaterials.VANADIUM;
import static conductance.api.NCMaterials.XENON;
import static conductance.api.NCMaterials.YTTERBIUM;
import static conductance.api.NCMaterials.YTTRIUM;
import static conductance.api.NCMaterials.ZINC;
import static conductance.api.NCMaterials.ZIRCONIUM;
import static conductance.api.NCTextureSets.BRIGHT;
import static conductance.api.NCTextureSets.METALLIC;
import static conductance.api.NCTextureSets.SHINY;

public final class MaterialLoaderPeriodicTable {

	//@formatter:off
	public static void init(final MaterialRegister register) {
		HYDROGEN = register.register("hydrogen")
				.gas()
				.color(0, 0, 255)
				.periodicElement(NCPeriodicElements.HYDROGEN)
				.build();
		TRITIUM = register.register("tritium")
				.gas()
				.color(255, 0, 0)
				.periodicElement(NCPeriodicElements.TRITIUM)
				.build();
		DEUTERIUM = register.register("deuterium")
				.gas()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.DEUTERIUM)
				.build();

		HELIUM = register.register("helium")
				.liquid().gas().plasma()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.HELIUM)
				.build();

		HELIUM_3 = register.register("helium_3")
				.gas()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.HELIUM_3)
				.build();

		LITHIUM = register.register("lithium")
				.dust()
				.liquid()
				.ore()
				.color(225, 220, 255)
				.periodicElement(NCPeriodicElements.LITHIUM)
				.build();

		BERYLLIUM = register.register("beryllium")
				.ingot()
				.liquid(1560)
				.ore()
				.color(100, 180, 100)
				.periodicElement(NCPeriodicElements.BERYLLIUM)
				.build();

		BORON = register.register("boron")
				.dust()
				.color(210, 250, 210)
				.periodicElement(NCPeriodicElements.BORON)
				.build();

		CARBON = register.register("carbon")
				.dust()
				.liquid(4600)
				.color(20, 20, 20)
				.periodicElement(NCPeriodicElements.CARBON)
				.build();

		NITROGEN = register.register("nitrogen")
				.gas().plasma()
				.color(0, 150, 200)
				.periodicElement(NCPeriodicElements.NITROGEN)
				.build();

		OXYGEN = register.register("oxygen")
				.gas().plasma().liquid(85)
				.color(0, 100, 200)
				.periodicElement(NCPeriodicElements.OXYGEN)
				.build();

		FLUORINE = register.register("fluorine")
				.gas()
				.color(255, 255, 255)
				.periodicElement(NCPeriodicElements.FLUORINE)
				.build();

		NEON = register.register("neon")
				.gas()
				.color(0xFAB4B4)
				.periodicElement(NCPeriodicElements.NEON)
				.build();

		SODIUM = register.register("sodium")
				.dust()
				.color(0, 0, 150)
				.periodicElement(NCPeriodicElements.SODIUM)
				.build();

		MAGNESIUM = register.register("magnesium")
				.dust()
				.liquid(923)
				.color(255, 200, 200)
				.periodicElement(NCPeriodicElements.MAGNESIUM)
				.build();

		ALUMINIUM = register.register("aluminium")
				.ingot()
				.liquid(933)
				.ore()
				.color(128, 200, 240)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.ALUMINIUM)
				.build();

		SILICON = register.register("silicon")
				.ingot()
				.liquid()
				.color(60, 60, 80).textureSet(METALLIC)
				.flags(GENERATE_FOIL)
				.periodicElement(NCPeriodicElements.SILICON)
				.build();

		PHOSPHORUS = register.register("phosphorus")
				.dust()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.PHOSPHORUS)
				.build();

		SULFUR = register.register("sulfur")
				.dust()
				.ore()
				.color(200, 200, 0)
				.periodicElement(NCPeriodicElements.SULFUR)
				.build();

		CHLORINE = register.register("chlorine")
				.gas()
				.color(255, 255, 255)
				.periodicElement(NCPeriodicElements.CHLORINE)
				.build();

		ARGON = register.register("argon")
				.gas().plasma()
				.color(0, 255, 0)
				.periodicElement(NCPeriodicElements.ARGON)
				.build();

		POTASSIUM = register.register("potassium")
				.dust()
				.liquid(337)
				.color(154, 172, 223).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.POTASSIUM)
				.build();

		CALCIUM = register.register("calcium")
				.dust()
				.color(255, 245, 245).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CALCIUM)
				.build();

		SCANDIUM = register.register("scandium")
				.dust()
				.color(204, 204, 204).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.SCANDIUM)
				.build();

		TITANIUM = register.register("titanium")
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL).liquid()
				.color(220, 160, 240).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL)
				.periodicElement(NCPeriodicElements.TITANIUM)
				.build();

		VANADIUM = register.register("vanadium")
				.ingot().liquid()
				.color(50, 50, 50).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.VANADIUM)
				.build();

		CHROMIUM = register.register("chromium")
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(2180)
				.color(255, 230, 230).textureSet(SHINY)
				.addFlagAndPreset(METAL_EXTRA)
				.periodicElement(NCPeriodicElements.CHROMIUM)
				.build();

		MANGANESE = register.register("manganese")
				.ingot()
				.liquid(1519)
				.color(250, 250, 250)
				.addFlagAndPreset(METAL_DEFAULT, GENERATE_FOIL, GENERATE_BOLT_AND_SCREW)
				.periodicElement(NCPeriodicElements.MANGANESE)
				.build();

		IRON = register.register("iron")
				.ingot()
				.liquid(1811).plasma()
				.ore()
				.color(200, 200, 200).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.IRON)
				.build();

		COBALT = register.register("cobalt")
				.ingot()
				.liquid(1768)
				.ore()
				.color(80, 80, 250).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.COBALT)
				.build();

		NICKEL = register.register("nickel")
				.ingot()
				.liquid(1728).plasma()
				.ore()
				.color(200, 200, 250).textureSet(METALLIC)
				.addFlagAndPreset(METAL_DEFAULT, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.NICKEL)
				.build();

		COPPER = register.register("copper")
				.ingot()
				.liquid(1358)
				.ore()
				.color(255, 100, 0).textureSet(SHINY)
				.addFlagAndPreset(METAL_ALL, CAN_MORTAR, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.COPPER)
				.build();

		ZINC = register.register("zinc")
				.ingot()
				.liquid(693)
				.color(250, 240, 240).textureSet(METALLIC)
				.addFlagAndPreset(METAL_DEFAULT, GENERATE_FOIL, GENERATE_RING, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.ZINC)
				.build();

		GALLIUM = register.register("gallium")
				.ingot()
				.liquid(303)
				.color(220, 220, 255).textureSet(SHINY)
				.addFlagAndPreset(METAL_DEFAULT, GENERATE_FOIL)
				.periodicElement(NCPeriodicElements.GALLIUM)
				.build();

		GERMANIUM = register.register("germanium")
				.dust()
				.color(0x6a6248).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.GERMANIUM)
				.build();

		ARSENIC = register.register("arsenic")
				.dust()
				.gas(887)
				.color(255, 255, 255)
				.periodicElement(NCPeriodicElements.ARSENIC)
				.build();

		SELENIUM = register.register("selenium")
				.dust()
				.color(0x401b24).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.SELENIUM)
				.build();

		BROMINE = register.register("bromine")
				.liquid(59)
				.color(0x080101).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.BROMINE)
				.build();

		KRYPTON = register.register("krypton")
				.gas()
				.color(0x80FF80)
				.periodicElement(NCPeriodicElements.KRYPTON)
				.build();

		RUBIDIUM = register.register("rubidium")
				.dust()
				.color(240, 30, 30).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RUBIDIUM)
				.build();

		STRONTIUM = register.register("strontium")
				.dust()
				.color(200, 200, 200).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.STRONTIUM)
				.build();

		YTTRIUM = register.register("yttrium")
				.ingot()
				.liquid()
				.color(220, 250, 220).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.YTTRIUM)
				.build();

		ZIRCONIUM = register.register("zirconium")
				.dust()
				.color(0x271813).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.ZIRCONIUM)
				.build();

		NIOBIUM = register.register("niobium")
				.ingot()
				.liquid()
				.color(190, 180, 200).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.NIOBIUM)
				.build();

		MOLYBDENUM = register.register("molybdenum")
				.ingot()
				.liquid(2896)
				.ore()
				.color(180, 180, 220).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.MOLYBDENUM)
				.flags(GENERATE_FOIL, GENERATE_BOLT_AND_SCREW)
				.build();

		TECHNETIUM = register.register("technetium")
				.ingot()
				.dust()
				.color(0xd7fce2).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.TECHNETIUM)
				.addFlagAndPreset(METAL_ALL)
				.build();

		RUTHENIUM = register.register("ruthenium")
				.ingot()
				.liquid()
				.color(0x3c7285).textureSet(SHINY)
				.flags(GENERATE_FOIL, GENERATE_GEAR)
				.periodicElement(NCPeriodicElements.RUTHENIUM)
				.build();

		RHODIUM = register.register("rhodium")
				.ingot()
				.liquid()
				.color(0xDC0C58).textureSet(BRIGHT)
				.addFlagAndPreset(METAL_ALL)
				.periodicElement(NCPeriodicElements.RHODIUM)
				.build();

		PALLADIUM = register.register("palladium")
				.ingot()
				.liquid()
				.ore()
				.color(128, 128, 128).textureSet(SHINY)
				.addFlagAndPreset(METAL_ALL)
				.periodicElement(NCPeriodicElements.PALLADIUM)
				.build();

		SILVER = register.register("silver")
				.ingot()
				.liquid(1235)
				.ore()
				.color(220, 220, 255).textureSet(SHINY)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.SILVER)
				.build();

		CADMIUM = register.register("cadmium")
				.dust()
				.color(50, 50, 60).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.CADMIUM)
				.build();

		INDIUM = register.register("indium")
				.ingot()
				.liquid(430)
				.color(64, 0, 128).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.INDIUM)
				.build();

		TIN = register.register("tin")
				.ingot()
				.liquid(505)
				.ore()
				.color(220, 220, 220)
				.addFlagAndPreset(METAL_ALL, GENERATE_ROTOR, GENERATE_FINE_WIRE, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.TIN)
				.build();

		ANTIMONY = register.register("antimony")
				.ingot()
				.liquid(904)
				.color(220, 220, 240).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.ANTIMONY)
				.build();

		TELLURIUM = register.register("tellurium")
				.dust()
				.color(206, 277, 86).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.TELLURIUM)
				.build();

		IODINE = register.register("iodine")
				.dust()
				.color(0x773000).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.IODINE)
				.build();

		XENON = register.register("xenon")
				.gas()
				.color(0x00FFFF)
				.periodicElement(NCPeriodicElements.XENON)
				.build();

		CAESIUM = register.register("caesium")
				.dust()
				.color(176, 196, 222).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CAESIUM)
				.build();

		BARIUM = register.register("barium")
				.dust()
				.color(255, 255, 255).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.BARIUM)
				.build();

		LANTHANUM = register.register("lanthanum")
				.dust()
				.liquid(1193)
				.color(138, 138, 138).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.LANTHANUM)
				.build();

		CERIUM = register.register("cerium")
				.dust()
				.liquid(1608)
				.color(123, 212, 144).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CERIUM)
				.build();

		PRASEODYMIUM = register.register("praseodymium")
				.ingot()
				.color(117, 214, 129).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PRASEODYMIUM)
				.build();

		NEODYMIUM = register.register("neodymium")
				.ingot()
				.liquid()
				.ore()
				.color(100, 100, 100).textureSet(METALLIC)
				.addFlagAndPreset(METAL_DEFAULT, GENERATE_ROD, GENERATE_BOLT_AND_SCREW)
				.periodicElement(NCPeriodicElements.NEODYMIUM)
				.build();

		PROMETHIUM = register.register("promethium")
				.dust()
				.color(36, 181, 53)
				.textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PROMETHIUM)
				.build();

		SAMARIUM = register.register("samarium")
				.ingot()
				.liquid(1345)
				.ore()
				.color(255, 255, 204).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.SAMARIUM)
				.build();

		EUROPIUM = register.register("europium")
				.ingot()
				.liquid(1099).plasma()
				.color(246, 181, 255).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.EUROPIUM)
				.build();

		GADOLINIUM = register.register("gadolinium")
				.dust()
				.color(59, 186, 28).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.GADOLINIUM)
				.build();

		TERBIUM = register.register("terbium")
				.dust()
				.color(255, 255, 255).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.TERBIUM)
				.build();

		DYSPROSIUM = register.register("dysprosium")
				.dust()
				.color(105, 209, 80).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.DYSPROSIUM)
				.build();

		HOLMIUM = register.register("holmium")
				.dust()
				.color(22, 8, 166).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.HOLMIUM)
				.build();

		ERBIUM = register.register("erbium")
				.dust()
				.color(176, 152, 81).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.ERBIUM)
				.build();

		THULIUM = register.register("thulium")
				.dust()
				.color(89, 107, 194).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.THULIUM)
				.build();

		YTTERBIUM = register.register("ytterbium")
				.dust()
				.color(44, 199, 80).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.YTTERBIUM)
				.build();

		LUTETIUM = register.register("lutetium")
				.dust()
				.liquid(1925)
				.color(188, 62, 199).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.LUTETIUM)
				.build();

		HAFNIUM = register.register("hafnium")
				.dust()
				.color(0x2b4a3a).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.HAFNIUM)
				.build();

		TANTALUM = register.register("tantalum")
				.ingot()
				.liquid(3290)
				.color(105, 183, 255).textureSet(METALLIC)
				.addFlagAndPreset(METAL_DEFAULT, GENERATE_FOIL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.TANTALUM)
				.build();

		TUNGSTEN = register.register("tungsten")
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(3695)
				.color(50, 50, 50).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.TUNGSTEN)
				.build();

		RHENIUM = register.register("thenium")
				.dust()
				.color(0x37393d).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RHENIUM)
				.build();

		OSMIUM = register.register("osmium")
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(3306)
				.color(50, 50, 255).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.OSMIUM)
				.build();

		IRIDIUM = register.register("iridium")
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(2719)
				.ore()
				.color(240, 240, 245).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.IRIDIUM)
				.build();

		PLATINUM = register.register("platinum")
				.ingot()
				.liquid(2041)
				.ore()
				.color(255, 255, 200).textureSet(SHINY)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.PLATINUM)
				.build();

		GOLD = register.register("gold")
				.ingot()
				.liquid(1337)
				.ore()
				.color(255, 255, 30).textureSet(SHINY)
				.addFlagAndPreset(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.GOLD)
				.build();

		MERCURY = register.register("mercury")
				.liquid()
				.color(255, 220, 220)
				.periodicElement(NCPeriodicElements.MERCURY)
				.build();

		THALLIUM = register.register("thallium")
				.dust()
				.color(0x1e576a).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.THALLIUM)
				.build();

		LEAD = register.register("lead")
				.ingot()
				.liquid(600)
				.ore()
				.color(140, 100, 140)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.LEAD)
				.build();

		BISMUTH = register.register("bismuth")
				.ingot()
				.liquid(545)
				.color(100, 160, 160).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.BISMUTH)
				.build();

		POLONIUM = register.register("polonium")
				.dust()
				.color(0xC9D47E)
				.periodicElement(NCPeriodicElements.POLONIUM)
				.build();

		ASTATINE = register.register("astatine")
				.dust()
				.color(0x17212b)
				.periodicElement(NCPeriodicElements.ASTATINE)
				.build();

		RADON = register.register("radon")
				.gas()
				.color(255, 0, 255)
				.periodicElement(NCPeriodicElements.RADON)
				.build();

		FRANCIUM = register.register("francium")
				.dust()
				.color(0x0000ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.FRANCIUM)
				.build();

		RADIUM = register.register("radium")
				.dust()
				.color(0x90ff2d).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RADIUM)
				.build();

		ACTINIUM = register.register("actinium")
				.dust()
				.color(0x353d41).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.ACTINIUM)
				.build();

		THORIUM = register.register("thorium")
				.ingot()
				.liquid(2023).plasma()
				.ore()
				.color(0, 30, 0).textureSet(SHINY)
				.addFlagAndPreset(METAL_DEFAULT, GENERATE_ROD)
				.periodicElement(NCPeriodicElements.THORIUM)
				.build();

		PROTACTINIUM = register.register("protactinium")
				.dust()
				.color(0xA78B6D).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PROTACTINIUM)
				.build();

		URANIUM_238 = register.register("uranium")
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(1405).plasma()
				.color(50, 240, 50).textureSet(METALLIC)
				.addFlagAndPreset(METAL_DEFAULT)
				.periodicElement(NCPeriodicElements.URANIUM_238)
				.build();

		URANIUM_235 = register.register("uranium_235")
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(1405).plasma()
				.color(70, 250, 70).textureSet(SHINY)
				.addFlagAndPreset(METAL_DEFAULT)
				.periodicElement(NCPeriodicElements.URANIUM_235)
				.build();

		NEPTUNIUM = register.register("neptunium")
				.dust()
				.plasma()
				.color(0x284D7B).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.NEPTUNIUM)
				.build();

		PLUTONIUM_239 = register.register("plutonium")
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(913).plasma()
				.ore(true)
				.color(240, 50, 50).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PLUTONIUM_239)
				.build();

		PLUTONIUM_241 = register.register("plutonium_241")
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(913).plasma()
				.color(250, 70, 70).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.PLUTONIUM_241)
				.build();

		AMERICIUM = register.register("americium")
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(1449).plasma()
				.color(200, 200, 200).textureSet(METALLIC)
				.addFlagAndPreset(METAL_EXTRA, GENERATE_FOIL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.AMERICIUM)
				.build();

		CURIUM = register.register("curium")
				.dust()
				.plasma()
				.color(0x7B544E).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CURIUM)
				.build();

		BERKELIUM = register.register("berkelium")
				.dust()
				.plasma()
				.color(0x645A88).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.BERKELIUM)
				.build();

		CALIFORNIUM = register.register("californium")
				.dust()
				.plasma()
				.color(0xA85A12).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CALIFORNIUM)
				.build();

		EINSTEINIUM = register.register("einsteinium")
				.dust()
				.plasma()
				.color(0xCE9F00).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.EINSTEINIUM)
				.build();

		FERMIUM = register.register("fermium")
				.dust()
				.plasma()
				.color(0x3e0022).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.FERMIUM)
				.build();

		MENDELEVIUM = register.register("mendelevium")
				.dust()
				.plasma()
				.color(0x1D4ACF).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.MENDELEVIUM)
				.build();

		NOBELIUM = register.register("nobelium")
				.dust()
				.color(0x43deff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.NOBELIUM)
				.build();

		LAWRENCIUM = register.register("lawrencium")
				.dust()
				.color(0x5D7575).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.LAWRENCIUM)
				.build();

		RUTHERFORDIUM = register.register("rutherfordium")
				.dust()
				.color(0xFFF6A1).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RUTHERFORDIUM)
				.build();

		DUBNIUM = register.register("dubnium")
				.dust()
				.color(0x00f3ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.DUBNIUM)
				.build();

		SEABORGIUM = register.register("seaborgium")
				.dust()
				.color(0x19C5FF).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.SEABORGIUM)
				.build();

		BOHRIUM = register.register("bohrium")
				.dust()
				.color(0xDC57FF).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.BOHRIUM)
				.build();

		HASSIUM = register.register("hassium")
				.dust()
				.color(0xDDDDDD)
				.periodicElement(NCPeriodicElements.HASSIUM)
				.build();

		MEITNERIUM = register.register("meitnerium")
				.dust()
				.color(0x6e90ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.MEITNERIUM)
				.build();

		DARMSTADTIUM = register.register("darmstadtium")
				.ingot()
				.liquid()
				.color(0x578062)
				.addFlagAndPreset(METAL_EXTRA2, GENERATE_GEAR, GENERATE_SMALL_GEAR)
				.periodicElement(NCPeriodicElements.DARMSTADTIUM)
				.build();

		ROENTGENIUM = register.register("roentgenium")
				.dust()
				.color(0xE3FDEC).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.ROENTGENIUM)
				.build();

		COPERNICIUM = register.register("copernium")
				.dust()
				.color(0xFFFEFF)
				.periodicElement(NCPeriodicElements.COPERNICIUM)
				.build();

		NIHONIUM = register.register("nohinium")
				.dust()
				.color(0xa68bff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.NIHONIUM)
				.build();

		FLEROVIUM = register.register("flerovium")
				.dust()
				.color(0xd2ff00).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.FLEROVIUM)
				.build();

		MOSCOVIUM = register.register("moscovium")
				.dust()
				.color(0xbd91ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.MOSCOVIUM)
				.build();

		LIVERMORIUM = register.register("livermorium")
				.dust()
				.color(0xff8b8b).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.LIVERMORIUM)
				.build();

		TENNESSINE = register.register("tennessine")
				.dust()
				.color(0xbca3ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.TENNESSINE)
				.build();

		OGANESSON = register.register("oganesson")
				.gas()
				.color(0x142D64).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.OGANESSON)
				.build();

		NEUTRONIUM = register.register("neutronium")
				.ingot()
				.color(250, 250, 250).textureSet(METALLIC)
				.addFlagAndPreset(METAL_ALL)
				.periodicElement(NCPeriodicElements.NEUTRONIUM)
				.build();

		COSMIC_NEUTRONIUM = register.register("cosmic_neutronium")
				.ingot()
				.color(255, 255, 255)
				.addFlagAndPreset(METAL_ALL)
				.periodicElement(NCPeriodicElements.COSMIC_NEUTRONIUM)
				.build();

		INFINITY = register.register("infinity")
				.ingot()
				.liquid()
				.color(255, 255, 255)
				.addFlagAndPreset(METAL_ALL)
				.periodicElement(NCPeriodicElements.INFINITY)
				.build();

		SPACETIME = register.register("spacetime")
				.ingot()
				.liquid(1000000000)
				.color(255, 255, 255)
				.addFlagAndPreset(METAL_ALL)
				.periodicElement(NCPeriodicElements.SPACETIME)
				.build();
	}
	//@formatter:on

	private MaterialLoaderPeriodicTable() {
	}
}
