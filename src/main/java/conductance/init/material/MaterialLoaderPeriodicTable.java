package conductance.init.material;

import net.minecraft.tags.BlockTags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCPeriodicElements;
import conductance.api.NCTiers;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialFlags.CAN_MORTAR;
import static conductance.api.NCMaterialFlags.GENERATE_BOLT_AND_SCREW;
import static conductance.api.NCMaterialFlags.GENERATE_FINE_WIRE;
import static conductance.api.NCMaterialFlags.GENERATE_FOIL;
import static conductance.api.NCMaterialFlags.GENERATE_GEAR;
import static conductance.api.NCMaterialFlags.GENERATE_PLATE;
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
import static conductance.api.NCMaterials.MAGNETIC_IRON;
import static conductance.api.NCMaterials.MAGNETIC_SAMARIUM;
import static conductance.api.NCMaterials.MANGANESE;
import static conductance.api.NCMaterials.MEITNERIUM;
import static conductance.api.NCMaterials.MENDELEVIUM;
import static conductance.api.NCMaterials.MERCURY;
import static conductance.api.NCMaterials.MOLYBDENUM;
import static conductance.api.NCMaterials.MOSCOVIUM;
import static conductance.api.NCMaterials.NEODYMIUM;
import static conductance.api.NCMaterials.NEON;
import static conductance.api.NCMaterials.NEPTUNIUM;
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

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialLoaderPeriodicTable {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialEvent register) {
		HYDROGEN = register.register("hydrogen", builder -> builder
				.gas()
				.color(0, 0, 255)
				.periodicElement(NCPeriodicElements.HYDROGEN));
		TRITIUM = register.register("tritium", builder -> builder
				.gas()
				.color(255, 0, 0)
				.periodicElement(NCPeriodicElements.TRITIUM));
		DEUTERIUM = register.register("deuterium", builder -> builder
				.gas()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.DEUTERIUM));

		HELIUM = register.register("helium", builder -> builder
				.liquid().gas().plasma()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.HELIUM)
				.defaultFluid(NCMaterialTraits.GAS));

		HELIUM_3 = register.register("helium_3", builder -> builder
				.gas()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.HELIUM_3));

		LITHIUM = register.register("lithium", builder -> builder
				.dust()
				.liquid()
				.ore()
				.color(225, 220, 255)
				.periodicElement(NCPeriodicElements.LITHIUM));

		BERYLLIUM = register.register("beryllium", builder -> builder
				.ingot()
				.liquid(1560)
				.ore()
				.color(100, 180, 100)
				.periodicElement(NCPeriodicElements.BERYLLIUM));

		BORON = register.register("boron", builder -> builder
				.dust()
				.color(210, 250, 210)
				.periodicElement(NCPeriodicElements.BORON));

		CARBON = register.register("carbon", builder -> builder
				.dust()
				.liquid(4600)
				.color(20, 20, 20)
				.periodicElement(NCPeriodicElements.CARBON));

		NITROGEN = register.register("nitrogen", builder -> builder
				.gas().plasma()
				.color(0, 150, 200)
				.periodicElement(NCPeriodicElements.NITROGEN));

		OXYGEN = register.register("oxygen", builder -> builder
				.gas().plasma().liquid(85)
				.color(0, 100, 200)
				.periodicElement(NCPeriodicElements.OXYGEN)
				.defaultFluid(NCMaterialTraits.GAS));

		FLUORINE = register.register("fluorine", builder -> builder
				.gas()
				.color(255, 255, 255)
				.periodicElement(NCPeriodicElements.FLUORINE));

		NEON = register.register("neon", builder -> builder
				.gas()
				.color(0xFAB4B4)
				.periodicElement(NCPeriodicElements.NEON));

		SODIUM = register.register("sodium", builder -> builder
				.dust()
				.color(0, 0, 150)
				.periodicElement(NCPeriodicElements.SODIUM));

		MAGNESIUM = register.register("magnesium", builder -> builder
				.dust()
				.liquid(923)
				.color(255, 200, 200)
				.periodicElement(NCPeriodicElements.MAGNESIUM));

		ALUMINIUM = register.register("aluminium", builder -> builder
				.ingot()
				.liquid(933)
				.ore()
				.color(0x80c8f0)
				.flags(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.ALUMINIUM)
				.wire(NCTiers.EV, 1));

		SILICON = register.register("silicon", builder -> builder
				.ingot()
				.liquid()
				.color(60, 60, 80).textureSet(METALLIC)
				.flags(GENERATE_FOIL)
				.periodicElement(NCPeriodicElements.SILICON));

		PHOSPHORUS = register.register("phosphorus", builder -> builder
				.dust()
				.color(255, 255, 0)
				.periodicElement(NCPeriodicElements.PHOSPHORUS));

		SULFUR = register.register("sulfur", builder -> builder
				.dust()
				.ore()
				.color(200, 200, 0)
				.periodicElement(NCPeriodicElements.SULFUR));

		CHLORINE = register.register("chlorine", builder -> builder
				.gas()
				.color(255, 255, 255)
				.periodicElement(NCPeriodicElements.CHLORINE));

		ARGON = register.register("argon", builder -> builder
				.gas().plasma()
				.color(0, 255, 0)
				.periodicElement(NCPeriodicElements.ARGON));

		POTASSIUM = register.register("potassium", builder -> builder
				.dust()
				.liquid(337)
				.color(154, 172, 223).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.POTASSIUM));

		CALCIUM = register.register("calcium", builder -> builder
				.dust()
				.color(255, 245, 245).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CALCIUM));

		SCANDIUM = register.register("scandium", builder -> builder
				.dust()
				.color(204, 204, 204).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.SCANDIUM));

		TITANIUM = register.register("titanium", builder -> builder
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL).liquid()
				.color(220, 160, 240).textureSet(METALLIC)
				.flags(METAL_ALL)
				.periodicElement(NCPeriodicElements.TITANIUM));

		VANADIUM = register.register("vanadium", builder -> builder
				.ingot().liquid()
				.color(50, 50, 50).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.VANADIUM));

		CHROMIUM = register.register("chromium", builder -> builder
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(2180)
				.color(255, 230, 230).textureSet(SHINY)
				.flags(METAL_EXTRA)
				.periodicElement(NCPeriodicElements.CHROMIUM));

		MANGANESE = register.register("manganese", builder -> builder
				.ingot()
				.liquid(1519)
				.color(250, 250, 250)
				.flags(GENERATE_PLATE, GENERATE_FOIL)
				.periodicElement(NCPeriodicElements.MANGANESE));

		IRON = register.register("iron", builder -> builder
				.ingot(() -> new MaterialTraitIngot(() -> MAGNETIC_IRON, null))
				.liquid(1811).plasma()
				.ore()
				.color(200, 200, 200).textureSet(METALLIC)
				.flags(METAL_ALL, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.IRON));

		COBALT = register.register("cobalt", builder -> builder
				.ingot()
				.liquid(1768)
				.ore()
				.color(80, 80, 250).textureSet(METALLIC)
				.flags(METAL_EXTRA2, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.COBALT));

		NICKEL = register.register("nickel", builder -> builder
				.ingot()
				.liquid(1728).plasma()
				.ore()
				.color(200, 200, 250).textureSet(METALLIC)
				.flags(METAL_DEFAULT, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.NICKEL));

		COPPER = register.register("copper", builder -> builder
				.ingot()
				.liquid(1358)
				.ore()
				.color(255, 100, 0).textureSet(SHINY)
				.flags(METAL_ALL, CAN_MORTAR, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.COPPER));

		ZINC = register.register("zinc", builder -> builder
				.ingot()
				.liquid(693)
				.color(250, 240, 240).textureSet(METALLIC)
				.flags(METAL_DEFAULT, GENERATE_FOIL, GENERATE_RING, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.ZINC));

		GALLIUM = register.register("gallium", builder -> builder
				.ingot()
				.liquid(303)
				.color(220, 220, 255).textureSet(SHINY)
				.flags(METAL_DEFAULT, GENERATE_FOIL)
				.periodicElement(NCPeriodicElements.GALLIUM));

		GERMANIUM = register.register("germanium", builder -> builder
				.dust()
				.color(0x6a6248).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.GERMANIUM));

		ARSENIC = register.register("arsenic", builder -> builder
				.dust()
				.gas(887)
				.color(255, 255, 255)
				.periodicElement(NCPeriodicElements.ARSENIC));

		SELENIUM = register.register("selenium", builder -> builder
				.dust()
				.color(0x401b24).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.SELENIUM));

		BROMINE = register.register("bromine", builder -> builder
				.liquid(59)
				.color(0x080101).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.BROMINE));

		KRYPTON = register.register("krypton", builder -> builder
				.gas()
				.color(0x80FF80)
				.periodicElement(NCPeriodicElements.KRYPTON));

		RUBIDIUM = register.register("rubidium", builder -> builder
				.dust()
				.color(240, 30, 30).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RUBIDIUM));

		STRONTIUM = register.register("strontium", builder -> builder
				.dust()
				.color(200, 200, 200).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.STRONTIUM));

		YTTRIUM = register.register("yttrium", builder -> builder
				.ingot()
				.liquid()
				.color(220, 250, 220).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.YTTRIUM));

		ZIRCONIUM = register.register("zirconium", builder -> builder
				.dust()
				.color(0x271813).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.ZIRCONIUM));

		NIOBIUM = register.register("niobium", builder -> builder
				.ingot()
				.liquid()
				.color(190, 180, 200).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.NIOBIUM));

		MOLYBDENUM = register.register("molybdenum", builder -> builder
				.ingot()
				.liquid(2896)
				.ore()
				.color(180, 180, 220).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.MOLYBDENUM)
				.flags(GENERATE_PLATE, GENERATE_FOIL));

		TECHNETIUM = register.register("technetium", builder -> builder
				.ingot()
				.dust()
				.color(0xd7fce2).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.TECHNETIUM)
				.flags(METAL_ALL));

		RUTHENIUM = register.register("ruthenium", builder -> builder
				.ingot()
				.liquid()
				.color(0x3c7285).textureSet(SHINY)
				.flags(GENERATE_FOIL, GENERATE_GEAR)
				.periodicElement(NCPeriodicElements.RUTHENIUM));

		RHODIUM = register.register("rhodium", builder -> builder
				.ingot()
				.liquid()
				.color(0xDC0C58).textureSet(BRIGHT)
				.flags(METAL_ALL)
				.periodicElement(NCPeriodicElements.RHODIUM));

		PALLADIUM = register.register("palladium", builder -> builder
				.ingot()
				.liquid()
				.ore()
				.color(128, 128, 128).textureSet(SHINY)
				.flags(METAL_ALL)
				.periodicElement(NCPeriodicElements.PALLADIUM));

		SILVER = register.register("silver", builder -> builder
				.ingot()
				.liquid(1235)
				.ore()
				.wire(NCTiers.LV, 1)
				.color(220, 220, 255).textureSet(SHINY)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.SILVER));

		CADMIUM = register.register("cadmium", builder -> builder
				.dust()
				.color(50, 50, 60).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.CADMIUM));

		INDIUM = register.register("indium", builder -> builder
				.ingot()
				.liquid(430)
				.color(64, 0, 128).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.INDIUM));

		TIN = register.register("tin", builder -> builder
				.ingot()
				.liquid(505)
				.ore()
				.wire(NCTiers.LV, 1)
				.color(220, 220, 220)
				.flags(METAL_ALL, GENERATE_ROTOR, GENERATE_FINE_WIRE, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.TIN));

		ANTIMONY = register.register("antimony", builder -> builder
				.ingot()
				.liquid(904)
				.color(220, 220, 240).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.ANTIMONY));

		TELLURIUM = register.register("tellurium", builder -> builder
				.dust()
				.color(206, 277, 86).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.TELLURIUM));

		IODINE = register.register("iodine", builder -> builder
				.dust()
				.color(0x773000).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.IODINE));

		XENON = register.register("xenon", builder -> builder
				.gas()
				.color(0x00FFFF)
				.periodicElement(NCPeriodicElements.XENON));

		CAESIUM = register.register("caesium", builder -> builder
				.dust()
				.color(176, 196, 222).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CAESIUM));

		BARIUM = register.register("barium", builder -> builder
				.dust()
				.color(255, 255, 255).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.BARIUM));

		LANTHANUM = register.register("lanthanum", builder -> builder
				.dust()
				.liquid(1193)
				.color(138, 138, 138).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.LANTHANUM));

		CERIUM = register.register("cerium", builder -> builder
				.dust()
				.liquid(1608)
				.color(123, 212, 144).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CERIUM));

		PRASEODYMIUM = register.register("praseodymium", builder -> builder
				.ingot()
				.color(117, 214, 129).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PRASEODYMIUM));

		NEODYMIUM = register.register("neodymium", builder -> builder
				.ingot(() -> new MaterialTraitIngot(() -> MAGNETIC_SAMARIUM, null))
				.liquid()
				.ore()
				.color(100, 100, 100).textureSet(METALLIC)
				.flags(METAL_DEFAULT, GENERATE_ROD, GENERATE_BOLT_AND_SCREW)
				.periodicElement(NCPeriodicElements.NEODYMIUM));

		PROMETHIUM = register.register("promethium", builder -> builder
				.dust()
				.color(36, 181, 53)
				.textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PROMETHIUM));

		SAMARIUM = register.register("samarium", builder -> builder
				.ingot(() -> new MaterialTraitIngot(() -> MAGNETIC_SAMARIUM, null))
				.liquid(1345)
				.ore()
				.color(255, 255, 204).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.SAMARIUM));

		EUROPIUM = register.register("europium", builder -> builder
				.ingot()
				.liquid(1099).plasma()
				.color(246, 181, 255).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.EUROPIUM));

		GADOLINIUM = register.register("gadolinium", builder -> builder
				.dust()
				.color(59, 186, 28).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.GADOLINIUM));

		TERBIUM = register.register("terbium", builder -> builder
				.dust()
				.color(255, 255, 255).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.TERBIUM));

		DYSPROSIUM = register.register("dysprosium", builder -> builder
				.dust()
				.color(105, 209, 80).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.DYSPROSIUM));

		HOLMIUM = register.register("holmium", builder -> builder
				.dust()
				.color(22, 8, 166).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.HOLMIUM));

		ERBIUM = register.register("erbium", builder -> builder
				.dust()
				.color(176, 152, 81).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.ERBIUM));

		THULIUM = register.register("thulium", builder -> builder
				.dust()
				.color(89, 107, 194).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.THULIUM));

		YTTERBIUM = register.register("ytterbium", builder -> builder
				.dust()
				.color(44, 199, 80).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.YTTERBIUM));

		LUTETIUM = register.register("lutetium", builder -> builder
				.dust()
				.liquid(1925)
				.color(188, 62, 199).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.LUTETIUM));

		HAFNIUM = register.register("hafnium", builder -> builder
				.dust()
				.color(0x2b4a3a).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.HAFNIUM));

		TANTALUM = register.register("tantalum", builder -> builder
				.ingot()
				.liquid(3290)
				.color(105, 183, 255).textureSet(METALLIC)
				.flags(METAL_DEFAULT, GENERATE_FOIL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.TANTALUM));

		TUNGSTEN = register.register("tungsten", builder -> builder
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(3695)
				.color(50, 50, 50).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.TUNGSTEN));

		RHENIUM = register.register("thenium", builder -> builder
				.dust()
				.color(0x37393d).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RHENIUM));

		OSMIUM = register.register("osmium", builder -> builder
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(3306)
				.color(50, 50, 255).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.OSMIUM));

		IRIDIUM = register.register("iridium", builder -> builder
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(2719)
				.ore()
				.color(240, 240, 245).textureSet(METALLIC)
				.flags(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.IRIDIUM));

		PLATINUM = register.register("platinum", builder -> builder
				.ingot()
				.liquid(2041)
				.ore()
				.color(255, 255, 200).textureSet(SHINY)
				.flags(METAL_ALL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.PLATINUM));

		GOLD = register.register("gold", builder -> builder
				.ingot()
				.liquid(1337)
				.ore()
				.color(255, 255, 30).textureSet(SHINY)
				.flags(METAL_ALL, GENERATE_FINE_WIRE, CAN_MORTAR)
				.periodicElement(NCPeriodicElements.GOLD));

		MERCURY = register.register("mercury", builder -> builder
				.liquid()
				.color(255, 220, 220)
				.periodicElement(NCPeriodicElements.MERCURY));

		THALLIUM = register.register("thallium", builder -> builder
				.dust()
				.color(0x1e576a).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.THALLIUM));

		LEAD = register.register("lead", builder -> builder
				.ingot()
				.liquid(600)
				.ore()
				.color(140, 100, 140)
				.flags(METAL_EXTRA2, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.LEAD));

		BISMUTH = register.register("bismuth", builder -> builder
				.ingot()
				.liquid(545)
				.color(100, 160, 160).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.BISMUTH));

		POLONIUM = register.register("polonium", builder -> builder
				.dust()
				.color(0xC9D47E)
				.periodicElement(NCPeriodicElements.POLONIUM));

		ASTATINE = register.register("astatine", builder -> builder
				.dust()
				.color(0x17212b)
				.periodicElement(NCPeriodicElements.ASTATINE));

		RADON = register.register("radon", builder -> builder
				.gas()
				.color(255, 0, 255)
				.periodicElement(NCPeriodicElements.RADON));

		FRANCIUM = register.register("francium", builder -> builder
				.dust()
				.color(0x0000ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.FRANCIUM));

		RADIUM = register.register("radium", builder -> builder
				.dust()
				.color(0x90ff2d).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RADIUM));

		ACTINIUM = register.register("actinium", builder -> builder
				.dust()
				.color(0x353d41).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.ACTINIUM));

		THORIUM = register.register("thorium", builder -> builder
				.ingot()
				.liquid(2023).plasma()
				.ore()
				.color(0, 30, 0).textureSet(SHINY)
				.flags(METAL_DEFAULT, GENERATE_ROD)
				.periodicElement(NCPeriodicElements.THORIUM));

		PROTACTINIUM = register.register("protactinium", builder -> builder
				.dust()
				.color(0xA78B6D).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PROTACTINIUM));

		URANIUM_238 = register.register("uranium", builder -> builder
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(1405).plasma()
				.color(50, 240, 50).textureSet(METALLIC)
				.flags(METAL_DEFAULT)
				.periodicElement(NCPeriodicElements.URANIUM_238));

		URANIUM_235 = register.register("uranium_235", builder -> builder
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(1405).plasma()
				.color(70, 250, 70).textureSet(SHINY)
				.flags(METAL_DEFAULT)
				.periodicElement(NCPeriodicElements.URANIUM_235));

		NEPTUNIUM = register.register("neptunium", builder -> builder
				.dust()
				.plasma()
				.color(0x284D7B).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.NEPTUNIUM));

		PLUTONIUM_239 = register.register("plutonium", builder -> builder
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(913).plasma()
				.ore(true)
				.color(240, 50, 50).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.PLUTONIUM_239));

		PLUTONIUM_241 = register.register("plutonium_241", builder -> builder
				.ingot(BlockTags.NEEDS_IRON_TOOL)
				.liquid(913).plasma()
				.color(250, 70, 70).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.PLUTONIUM_241));

		AMERICIUM = register.register("americium", builder -> builder
				.ingot(BlockTags.NEEDS_DIAMOND_TOOL)
				.liquid(1449).plasma()
				.color(200, 200, 200).textureSet(METALLIC)
				.flags(METAL_EXTRA, GENERATE_FOIL, GENERATE_FINE_WIRE)
				.periodicElement(NCPeriodicElements.AMERICIUM));

		CURIUM = register.register("curium", builder -> builder
				.dust()
				.plasma()
				.color(0x7B544E).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CURIUM));

		BERKELIUM = register.register("berkelium", builder -> builder
				.dust()
				.plasma()
				.color(0x645A88).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.BERKELIUM));

		CALIFORNIUM = register.register("californium", builder -> builder
				.dust()
				.plasma()
				.color(0xA85A12).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.CALIFORNIUM));

		EINSTEINIUM = register.register("einsteinium", builder -> builder
				.dust()
				.plasma()
				.color(0xCE9F00).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.EINSTEINIUM));

		FERMIUM = register.register("fermium", builder -> builder
				.dust()
				.plasma()
				.color(0x3e0022).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.FERMIUM));

		MENDELEVIUM = register.register("mendelevium", builder -> builder
				.dust()
				.plasma()
				.color(0x1D4ACF).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.MENDELEVIUM));

		NOBELIUM = register.register("nobelium", builder -> builder
				.dust()
				.color(0x43deff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.NOBELIUM));

		LAWRENCIUM = register.register("lawrencium", builder -> builder
				.dust()
				.color(0x5D7575).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.LAWRENCIUM));

		RUTHERFORDIUM = register.register("rutherfordium", builder -> builder
				.dust()
				.color(0xFFF6A1).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.RUTHERFORDIUM));

		DUBNIUM = register.register("dubnium", builder -> builder
				.dust()
				.color(0x00f3ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.DUBNIUM));

		SEABORGIUM = register.register("seaborgium", builder -> builder
				.dust()
				.color(0x19C5FF).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.SEABORGIUM));

		BOHRIUM = register.register("bohrium", builder -> builder
				.dust()
				.color(0xDC57FF).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.BOHRIUM));

		HASSIUM = register.register("hassium", builder -> builder
				.dust()
				.color(0xDDDDDD)
				.periodicElement(NCPeriodicElements.HASSIUM));

		MEITNERIUM = register.register("meitnerium", builder -> builder
				.dust()
				.color(0x6e90ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.MEITNERIUM));

		DARMSTADTIUM = register.register("darmstadtium", builder -> builder
				.ingot()
				.liquid()
				.color(0x578062)
				.flags(METAL_EXTRA2, GENERATE_GEAR, GENERATE_SMALL_GEAR)
				.periodicElement(NCPeriodicElements.DARMSTADTIUM));

		ROENTGENIUM = register.register("roentgenium", builder -> builder
				.dust()
				.color(0xE3FDEC).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.ROENTGENIUM));

		COPERNICIUM = register.register("copernium", builder -> builder
				.dust()
				.color(0xFFFEFF)
				.periodicElement(NCPeriodicElements.COPERNICIUM));

		NIHONIUM = register.register("nohinium", builder -> builder
				.dust()
				.color(0xa68bff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.NIHONIUM));

		FLEROVIUM = register.register("flerovium", builder -> builder
				.dust()
				.color(0xd2ff00).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.FLEROVIUM));

		MOSCOVIUM = register.register("moscovium", builder -> builder
				.dust()
				.color(0xbd91ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.MOSCOVIUM));

		LIVERMORIUM = register.register("livermorium", builder -> builder
				.dust()
				.color(0xff8b8b).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.LIVERMORIUM));

		TENNESSINE = register.register("tennessine", builder -> builder
				.dust()
				.color(0xbca3ff).textureSet(SHINY)
				.periodicElement(NCPeriodicElements.TENNESSINE));

		OGANESSON = register.register("oganesson", builder -> builder
				.gas()
				.color(0x142D64).textureSet(METALLIC)
				.periodicElement(NCPeriodicElements.OGANESSON));
	}

	private MaterialLoaderPeriodicTable() {
	}
}
