package conductance.init.material;

import net.minecraft.tags.BlockTags;
import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTraits;
import conductance.api.NCPeriodicElements;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.Conductance;
import static conductance.api.NCMaterialTextureSets.BRIGHT;
import static conductance.api.NCMaterialTextureSets.METALLIC;
import static conductance.api.NCMaterialTextureSets.SHINY;
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

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialsPeriodicTable {

	@EventListener(priority = -100)
	private static void initialize(final RegisterMaterialEvent event) {
		HYDROGEN = event.register("hydrogen", NCPeriodicElements.HYDROGEN, b -> b
				.gas()
				.color(0x0000FF)
		);

		TRITIUM = event.register("tritium", NCPeriodicElements.TRITIUM, b -> b
				.gas()
				.color(0xFF0000)
		);

		DEUTERIUM = event.register("deuterium", NCPeriodicElements.DEUTERIUM, b -> b
				.gas()
				.color(0xFFFF00)
		);

		HELIUM = event.register("helium", NCPeriodicElements.HELIUM, b -> b
				.liquid().gas().plasma()
				.color(0xFFFF00)
				.prop(NCMaterialProps.DEFAULT_FLUID, NCMaterialTraits.GAS)
		);

		HELIUM_3 = event.register("helium_3", NCPeriodicElements.HELIUM_3, b -> b
				.gas()
				.color(0xFFFF00)
		);

		LITHIUM = event.register("lithium", NCPeriodicElements.LITHIUM, b -> b
				.dust()
				.liquid()
				// .ore()
				.color(0xE1DCFF)
		);

		BERYLLIUM = event.register("beryllium", NCPeriodicElements.BERYLLIUM, b -> b
				.dust().ingot()
				.liquid(1560)
				// .ore()
				.color(0x64B464)
		);

		BORON = event.register("boron", NCPeriodicElements.BORON, b -> b
				.dust()
				.color(0xD2FAD2)
		);

		CARBON = event.register("carbon", NCPeriodicElements.CARBON, b -> b
				.dust()
				.liquid(4600)
				.color(0x141414)
		);

		NITROGEN = event.register("nitrogen", NCPeriodicElements.NITROGEN, b -> b
				.gas().plasma()
				.color(0x0096C8)
		);

		OXYGEN = event.register("oxygen", NCPeriodicElements.OXYGEN, b -> b
				.liquid(85).gas().plasma()
				.color(0x0064C8)
				.prop(NCMaterialProps.DEFAULT_FLUID, NCMaterialTraits.GAS)
		);

		FLUORINE = event.register("fluorine", NCPeriodicElements.FLUORINE, b -> b
				.gas()
				.color(0x61A0D8)
		);

		NEON = event.register("neon", NCPeriodicElements.NEON, b -> b
				.gas()
				.color(0xFAB4B4)
		);

		SODIUM = event.register("sodium", NCPeriodicElements.SODIUM, b -> b
				.dust()
				.color(0x000096)
		);

		MAGNESIUM = event.register("magnesium", NCPeriodicElements.MAGNESIUM, b -> b
				.dust().ingot()
				.liquid(923)
				.color(0xFFC8C8)
		);

		ALUMINIUM = event.register("aluminium", NCPeriodicElements.ALUMINIUM, b -> b
				.metalAll().fineWire()
				.liquid(933)
				// .ore()
				.color(0x80C8F0)
		);

		SILICON = event.register("silicon", NCPeriodicElements.SILICON, b -> b
				.metalDefault()
				.liquid().plasma()
				.style(0x3C3C50, METALLIC)
		);

		PHOSPHORUS = event.register("phosphorus", NCPeriodicElements.PHOSPHORUS, b -> b
				.dust()
				.color(0xFFFF00)
		);

		SULFUR = event.register("sulfur", NCPeriodicElements.SULFUR, b -> b
				.dust()
				// .ore()
				.color(0xC8C800)
		);

		CHLORINE = event.register("chlorine", NCPeriodicElements.CHLORINE, b -> b
				.gas()
				.color(0x246D6D)
		);

		ARGON = event.register("argon", NCPeriodicElements.ARGON, b -> b
				.gas().plasma()
				.color(0x00FF00)
		);

		POTASSIUM = event.register("potassium", NCPeriodicElements.POTASSIUM, b -> b
				.dust()
				.liquid(337)
				.style(0x9AACDF, METALLIC)
		);

		CALCIUM = event.register("calcium", NCPeriodicElements.CALCIUM, b -> b
				.dust()
				.style(0xFFF5F5, METALLIC)
		);

		SCANDIUM = event.register("scandium", NCPeriodicElements.SCANDIUM, b -> b
				.dust()
				.style(0xCCCCCC, METALLIC)
		);

		TITANIUM = event.register("titanium", NCPeriodicElements.TITANIUM, b -> b
				.metalAll().fineWire()
				.liquid()
				.style(0xDCA0F0, METALLIC)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_DIAMOND_TOOL)
		);

		VANADIUM = event.register("vanadium", NCPeriodicElements.VANADIUM, b -> b
				.dust().ingot()
				.liquid()
				.style(0x323232, METALLIC)
		);

		CHROMIUM = event.register("chromium", NCPeriodicElements.CHROMIUM, b -> b
				.metalAll()
				.liquid(2180)
				.style(0xFFE6E6, SHINY)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_IRON_TOOL)
		);

		MANGANESE = event.register("manganese", NCPeriodicElements.MANGANESE, b -> b
				.metalDefault().foil().fineWire()
				.liquid(1519)
				.color(0xFAFAFA)
		);

		IRON = event.register("iron", NCPeriodicElements.IRON, b -> b
				.metalAll().fineWire()
				.liquid(1811).plasma()
				// .ore()
				.style(0xC8C8C8, METALLIC)
		);

		COBALT = event.register("cobalt", NCPeriodicElements.COBALT, b -> b
				.metalDefault().fineWire()
				.liquid(1768)
				// .ore()
				.style(0x5050FA, METALLIC)
		);

		NICKEL = event.register("nickel", NCPeriodicElements.NICKEL, b -> b
				.metalDefault()
				.liquid(1728).plasma()
				// .ore()
				.style(0xC8C8FA, METALLIC)
		);

		COPPER = event.register("copper", NCPeriodicElements.COPPER, b -> b
				.metalAll().fineWire()
				.liquid(1358)
				// .ore()
				.style(0xFF6400, SHINY)
		);

		ZINC = event.register("zinc", NCPeriodicElements.ZINC, b -> b
				.metalDefault().foil()
				.liquid(693)
				.style(0xFAF0F0, METALLIC)
		);

		GALLIUM = event.register("gallium", NCPeriodicElements.GALLIUM, b -> b
				.metalDefault().foil().fineWire()
				.liquid(303)
				.style(0xDCDCFF, SHINY)
		);

		GERMANIUM = event.register("germanium", NCPeriodicElements.GERMANIUM, b -> b
				.dust()
				.style(0x6A6248, SHINY)
		);

		ARSENIC = event.register("arsenic", NCPeriodicElements.ARSENIC, b -> b
				.dust()
				.gas(887)
				.color(0xFFFFFF)
		);

		SELENIUM = event.register("selenium", NCPeriodicElements.SELENIUM, b -> b
				.dust()
				.style(0x401B24, SHINY)
		);

		BROMINE = event.register("bromine", NCPeriodicElements.BROMINE, b -> b
				.liquid(59)
				.style(0x080101, SHINY)
		);

		KRYPTON = event.register("krypton", NCPeriodicElements.KRYPTON, b -> b
				.gas()
				.color(0x80FF80)
		);

		RUBIDIUM = event.register("rubidium", NCPeriodicElements.RUBIDIUM, b -> b
				.dust()
				.style(0xF01E1E, SHINY)
		);

		STRONTIUM = event.register("strontium", NCPeriodicElements.STRONTIUM, b -> b
				.dust()
				.style(0xC8C8C8, METALLIC)
		);

		YTTRIUM = event.register("yttrium", NCPeriodicElements.YTTRIUM, b -> b
				.metalDefault()
				.liquid()
				.style(0xDCFADC, METALLIC)
		);

		ZIRCONIUM = event.register("zirconium", NCPeriodicElements.ZIRCONIUM, b -> b
				.dust()
				.style(0x271813, METALLIC)
		);

		NIOBIUM = event.register("niobium", NCPeriodicElements.NIOBIUM, b -> b
				.metalDefault().foil().fineWire()
				.liquid()
				.style(0xBEB4C8, METALLIC)
		);

		MOLYBDENUM = event.register("molybdenum", NCPeriodicElements.MOLYBDENUM, b -> b
				.metalDefault().foil()
				.liquid(2896)
				// .ore()
				.style(0xB4B4DC, SHINY)
		);

		TECHNETIUM = event.register("technetium", NCPeriodicElements.TECHNETIUM, b -> b
				.metalAll().fineWire()
				.style(0xD7FCE2, SHINY)
		);

		RUTHENIUM = event.register("ruthenium", NCPeriodicElements.RUTHENIUM, b -> b
				.metalExtra()
				.liquid()
				.style(0x3C7285, SHINY)
		);

		RHODIUM = event.register("rhodium", NCPeriodicElements.RHODIUM, b -> b
				.metalExtra()
				.liquid()
				.style(0xDC0C58, BRIGHT)
		);

		PALLADIUM = event.register("palladium", NCPeriodicElements.PALLADIUM, b -> b
				.metalExtra().fineWire()
				.liquid()
				// .ore()
				.style(0x808080, SHINY)
		);

		SILVER = event.register("silver", NCPeriodicElements.SILVER, b -> b
				.metalExtra().fineWire()
				.liquid(1235)
				// .ore()
				.style(0xDCDCFF, SHINY)
		);

		CADMIUM = event.register("cadmium", NCPeriodicElements.CADMIUM, b -> b
				.dust()
				.style(0x32323C, SHINY)
		);

		INDIUM = event.register("indium", NCPeriodicElements.INDIUM, b -> b
				.dust()
				.liquid(430)
				.style(0x400080, SHINY)
		);

		TIN = event.register("tin", NCPeriodicElements.TIN, b -> b
				.metalAll().fineWire()
				.liquid(505)
				// .ore()
				.color(0xDCDCDC)
		);

		ANTIMONY = event.register("antimony", NCPeriodicElements.ANTIMONY, b -> b
				.dust().ingot()
				.liquid(904)
				.style(0xDCDCF0, SHINY)
		);

		TELLURIUM = event.register("tellurium", NCPeriodicElements.TELLURIUM, b -> b
				.dust()
				.style(0xCEF456, METALLIC)
		);

		IODINE = event.register("iodine", NCPeriodicElements.IODINE, b -> b
				.dust()
				.style(0x773000, SHINY)
		);

		XENON = event.register("xenon", NCPeriodicElements.XENON, b -> b
				.gas()
				.color(0x00FFFF)
		);

		CAESIUM = event.register("caesium", NCPeriodicElements.CAESIUM, b -> b
				.dust()
				.style(0xB0C4DE, METALLIC)
		);

		BARIUM = event.register("barium", NCPeriodicElements.BARIUM, b -> b
				.dust()
				.style(0xFFFFFF, METALLIC)
		);

		LANTHANUM = event.register("lanthanum", NCPeriodicElements.LANTHANUM, b -> b
				.dust()
				.liquid(1193)
				.style(0x8A8A8A, METALLIC)
		);

		CERIUM = event.register("cerium", NCPeriodicElements.CERIUM, b -> b
				.dust()
				.liquid(1608)
				.style(0x7BD490, METALLIC)
		);

		PRASEODYMIUM = event.register("praseodymium", NCPeriodicElements.PRASEODYMIUM, b -> b
				.dust().ingot().rod().fineWire()
				.style(0x75D681, METALLIC)
		);

		NEODYMIUM = event.register("neodymium", NCPeriodicElements.NEODYMIUM, b -> b
				.dust().ingot().rod().fineWire()
				.liquid()
				// .ore()
				.style(0x646464, METALLIC)
		);

		PROMETHIUM = event.register("promethium", NCPeriodicElements.PROMETHIUM, b -> b
				.dust()
				.color(0x24B535)
				.textureSet(METALLIC)
		);

		SAMARIUM = event.register("samarium", NCPeriodicElements.SAMARIUM, b -> b
				.dust().ingot().rod().fineWire()
				.liquid(1345)
				// .ore()
				.style(0xFFFFCC, METALLIC)
		);

		EUROPIUM = event.register("europium", NCPeriodicElements.EUROPIUM, b -> b
				.metalDefault().foil().fineWire()
				.liquid(1099).plasma()
				.style(0xF6B5FF, METALLIC)
		);

		GADOLINIUM = event.register("gadolinium", NCPeriodicElements.GADOLINIUM, b -> b
				.dust()
				.style(0x3BBA1C, METALLIC)
		);

		TERBIUM = event.register("terbium", NCPeriodicElements.TERBIUM, b -> b
				.dust()
				.style(0xFFFFFF, METALLIC)
		);

		DYSPROSIUM = event.register("dysprosium", NCPeriodicElements.DYSPROSIUM, b -> b
				.dust()
				.style(0x69D150, METALLIC)
		);

		HOLMIUM = event.register("holmium", NCPeriodicElements.HOLMIUM, b -> b
				.dust()
				.style(0x1608A6, METALLIC)
		);

		ERBIUM = event.register("erbium", NCPeriodicElements.ERBIUM, b -> b
				.dust()
				.style(0xB09851, METALLIC)
		);

		THULIUM = event.register("thulium", NCPeriodicElements.THULIUM, b -> b
				.dust()
				.style(0x596BC2, METALLIC)
		);

		YTTERBIUM = event.register("ytterbium", NCPeriodicElements.YTTERBIUM, b -> b
				.dust()
				.style(0x2CC750, METALLIC)
		);

		LUTETIUM = event.register("lutetium", NCPeriodicElements.LUTETIUM, b -> b
				.dust()
				.liquid(1925)
				.style(0xBC3EC7, METALLIC)
		);

		HAFNIUM = event.register("hafnium", NCPeriodicElements.HAFNIUM, b -> b
				.dust()
				.style(0x2B4A3A, SHINY)
		);

		TANTALUM = event.register("tantalum", NCPeriodicElements.TANTALUM, b -> b
				.metalDefault().foil().fineWire()
				.liquid(3290)
				.style(0x69B7FF, METALLIC)
		);

		TUNGSTEN = event.register("tungsten", NCPeriodicElements.TUNGSTEN, b -> b
				.metalAll().fineWire()
				.liquid(3695)
				.style(0x323232, METALLIC)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_DIAMOND_TOOL)
		);

		RHENIUM = event.register("thenium", NCPeriodicElements.RHENIUM, b -> b
				.dust()
				.style(0x37393D, SHINY)
		);

		OSMIUM = event.register("osmium", NCPeriodicElements.OSMIUM, b -> b
				.metalAll().fineWire()
				.liquid(3306)
				.style(0x3232FF, METALLIC)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_DIAMOND_TOOL)
		);

		IRIDIUM = event.register("iridium", NCPeriodicElements.IRIDIUM, b -> b
				.metalAll().fineWire()
				.liquid(2719)
				// .ore()
				.style(0xF0F0F5, METALLIC)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_DIAMOND_TOOL)
		);

		PLATINUM = event.register("platinum", NCPeriodicElements.PLATINUM, b -> b
				.metalAll().fineWire()
				.liquid(2041)
				// .ore()
				.style(0xFFFFC8, SHINY)
		);

		GOLD = event.register("gold", NCPeriodicElements.GOLD, b -> b
				.metalExtra().fineWire()
				.liquid(1337)
				// .ore()
				.style(0xFFFF1E, SHINY)
		);

		MERCURY = event.register("mercury", NCPeriodicElements.MERCURY, b -> b
				.liquid()
				.color(0xFFDCDC)
		);

		THALLIUM = event.register("thallium", NCPeriodicElements.THALLIUM, b -> b
				.dust()
				.style(0x1E576A, SHINY)
		);

		LEAD = event.register("lead", NCPeriodicElements.LEAD, b -> b
				.metalExtra().fineWire()
				.liquid(600)
				// .ore()
				.color(0x8C648C)
		);

		BISMUTH = event.register("bismuth", NCPeriodicElements.BISMUTH, b -> b
				.dust().ingot()
				.liquid(545)
				.style(0x64A0A0, METALLIC)
		);

		POLONIUM = event.register("polonium", NCPeriodicElements.POLONIUM, b -> b
				.dust()
				.color(0xC9D47E)
		);

		ASTATINE = event.register("astatine", NCPeriodicElements.ASTATINE, b -> b
				.dust()
				.color(0x17212B)
		);

		RADON = event.register("radon", NCPeriodicElements.RADON, b -> b
				.gas()
				.color(0xFF00FF)
		);

		FRANCIUM = event.register("francium", NCPeriodicElements.FRANCIUM, b -> b
				.dust()
				.style(0x0000FF, SHINY)
		);

		RADIUM = event.register("radium", NCPeriodicElements.RADIUM, b -> b
				.dust()
				.style(0x90FF2D, SHINY)
		);

		ACTINIUM = event.register("actinium", NCPeriodicElements.ACTINIUM, b -> b
				.dust()
				.style(0x353D41, METALLIC)
		);

		THORIUM = event.register("thorium", NCPeriodicElements.THORIUM, b -> b
				.metalDefault()
				.liquid(2023).plasma()
				// .ore()
				.style(0x001E00, SHINY)
		);

		PROTACTINIUM = event.register("protactinium", NCPeriodicElements.PROTACTINIUM, b -> b
				.dust()
				.style(0xA78B6D, METALLIC)
		);

		URANIUM_238 = event.register("uranium", NCPeriodicElements.URANIUM_238, b -> b
				.metalDefault()
				.liquid(1405).plasma()
				.style(0x32F032, METALLIC)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_IRON_TOOL)
		);

		URANIUM_235 = event.register("uranium_235", NCPeriodicElements.URANIUM_235, b -> b
				.metalDefault()
				.liquid(1405).plasma()
				.style(0x46FA46, SHINY)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_IRON_TOOL)
		);

		NEPTUNIUM = event.register("neptunium", NCPeriodicElements.NEPTUNIUM, b -> b
				.dust()
				.plasma()
				.style(0x284D7B, METALLIC)
		);

		PLUTONIUM_239 = event.register("plutonium", NCPeriodicElements.PLUTONIUM_239, b -> b
				.metalDefault()
				.liquid(913).plasma()
				//.ore(true)
				.style(0xF03232, METALLIC)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_IRON_TOOL)
		);

		PLUTONIUM_241 = event.register("plutonium_241", NCPeriodicElements.PLUTONIUM_241, b -> b
				.metalDefault()
				.liquid(913).plasma()
				.style(0xFA4646, SHINY)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_IRON_TOOL)
		);

		AMERICIUM = event.register("americium", NCPeriodicElements.AMERICIUM, b -> b
				.metalExtra().fineWire()
				.liquid(1449).plasma()
				.style(0xC8C8C8, METALLIC)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_IRON_TOOL)
		);

		CURIUM = event.register("curium", NCPeriodicElements.CURIUM, b -> b
				.dust()
				.plasma()
				.style(0x7B544E, METALLIC)
		);

		BERKELIUM = event.register("berkelium", NCPeriodicElements.BERKELIUM, b -> b
				.dust()
				.plasma()
				.style(0x645A88, METALLIC)
		);

		CALIFORNIUM = event.register("californium", NCPeriodicElements.CALIFORNIUM, b -> b
				.dust()
				.plasma()
				.style(0xA85A12, METALLIC)
		);

		EINSTEINIUM = event.register("einsteinium", NCPeriodicElements.EINSTEINIUM, b -> b
				.dust()
				.plasma()
				.style(0xCE9F00, METALLIC)
		);

		FERMIUM = event.register("fermium", NCPeriodicElements.FERMIUM, b -> b
				.dust()
				.plasma()
				.style(0x3e0022, METALLIC)
		);

		MENDELEVIUM = event.register("mendelevium", NCPeriodicElements.MENDELEVIUM, b -> b
				.dust()
				.plasma()
				.style(0x1D4ACF, METALLIC)
		);

		NOBELIUM = event.register("nobelium", NCPeriodicElements.NOBELIUM, b -> b
				.dust()
				.style(0x43deff, SHINY)
		);

		LAWRENCIUM = event.register("lawrencium", NCPeriodicElements.LAWRENCIUM, b -> b
				.dust()
				.style(0x5D7575, METALLIC)
		);

		RUTHERFORDIUM = event.register("rutherfordium", NCPeriodicElements.RUTHERFORDIUM, b -> b
				.dust()
				.style(0xFFF6A1, SHINY)
		);

		DUBNIUM = event.register("dubnium", NCPeriodicElements.DUBNIUM, b -> b
				.dust()
				.style(0x00F3FF, SHINY)
		);

		SEABORGIUM = event.register("seaborgium", NCPeriodicElements.SEABORGIUM, b -> b
				.dust()
				.style(0x19C5FF, SHINY)
		);

		BOHRIUM = event.register("bohrium", NCPeriodicElements.BOHRIUM, b -> b
				.dust()
				.style(0xDC57FF, SHINY)
		);

		HASSIUM = event.register("hassium", NCPeriodicElements.HASSIUM, b -> b
				.dust()
				.color(0xDDDDDD)
		);

		MEITNERIUM = event.register("meitnerium", NCPeriodicElements.MEITNERIUM, b -> b
				.dust()
				.style(0x6E90FF, SHINY)
		);

		DARMSTADTIUM = event.register("darmstadtium", NCPeriodicElements.DARMSTADTIUM, b -> b
				.metalExtra()
				.liquid()
				.color(0x578062)
				.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_IRON_TOOL)
		);

		ROENTGENIUM = event.register("roentgenium", NCPeriodicElements.ROENTGENIUM, b -> b
				.dust()
				.style(0xE3FDEC, SHINY)
		);

		COPERNICIUM = event.register("copernium", NCPeriodicElements.COPERNICIUM, b -> b
				.dust()
				.color(0xFFFEFF)
		);

		NIHONIUM = event.register("nohinium", NCPeriodicElements.NIHONIUM, b -> b
				.dust()
				.style(0xA68BFF, SHINY)
		);

		FLEROVIUM = event.register("flerovium", NCPeriodicElements.FLEROVIUM, b -> b
				.dust()
				.style(0xD2FF00, SHINY)
		);

		MOSCOVIUM = event.register("moscovium", NCPeriodicElements.MOSCOVIUM, b -> b
				.dust()
				.style(0xBD91FF, SHINY)
		);

		LIVERMORIUM = event.register("livermorium", NCPeriodicElements.LIVERMORIUM, b -> b
				.dust()
				.style(0xFF8B8B, SHINY)
		);

		TENNESSINE = event.register("tennessine", NCPeriodicElements.TENNESSINE, b -> b
				.dust()
				.style(0xBCA3FF, SHINY)
		);

		OGANESSON = event.register("oganesson", NCPeriodicElements.OGANESSON, b -> b
				.gas()
				.style(0x142D64, METALLIC)
		);
	}

	@EventListener(priority = -99)
	private static void addPeriodicTableMaterialTranslations(final AddTranslationEvent event) {
		event.add(HELIUM_3, "Helium-3");
		event.add(URANIUM_235, "Uranium-235");
		event.add(PLUTONIUM_241, "Plutonium-241");
	}

	private MaterialsPeriodicTable() {
	}
}
