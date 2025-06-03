package conductance.init;

import net.minecraft.tags.BlockTags;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialOreTypes;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
import conductance.api.material.Material;
import conductance.api.material.event.RegisterMaterialTaggedSetEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialTaggedSets.BOLT;
import static conductance.api.NCMaterialTaggedSets.DUST;
import static conductance.api.NCMaterialTaggedSets.FINE_WIRE;
import static conductance.api.NCMaterialTaggedSets.FOIL;
import static conductance.api.NCMaterialTaggedSets.FRAME_BOX;
import static conductance.api.NCMaterialTaggedSets.GAS;
import static conductance.api.NCMaterialTaggedSets.GEAR;
import static conductance.api.NCMaterialTaggedSets.GEAR_SMALL;
import static conductance.api.NCMaterialTaggedSets.GEM;
import static conductance.api.NCMaterialTaggedSets.GEM_EXQUISITE;
import static conductance.api.NCMaterialTaggedSets.GEM_FLAWED;
import static conductance.api.NCMaterialTaggedSets.GEM_FLAWLESS;
import static conductance.api.NCMaterialTaggedSets.INGOT;
import static conductance.api.NCMaterialTaggedSets.LENS;
import static conductance.api.NCMaterialTaggedSets.LIQUID;
import static conductance.api.NCMaterialTaggedSets.NUGGET;
import static conductance.api.NCMaterialTaggedSets.ORE_ANDESITE;
import static conductance.api.NCMaterialTaggedSets.ORE_BASALT;
import static conductance.api.NCMaterialTaggedSets.ORE_BLACKSTONE;
import static conductance.api.NCMaterialTaggedSets.ORE_DEEPSLATE;
import static conductance.api.NCMaterialTaggedSets.ORE_DIORITE;
import static conductance.api.NCMaterialTaggedSets.ORE_END_STONE;
import static conductance.api.NCMaterialTaggedSets.ORE_GRANITE;
import static conductance.api.NCMaterialTaggedSets.ORE_GRAVEL;
import static conductance.api.NCMaterialTaggedSets.ORE_NETHERRACK;
import static conductance.api.NCMaterialTaggedSets.ORE_RED_SAND;
import static conductance.api.NCMaterialTaggedSets.ORE_SAND;
import static conductance.api.NCMaterialTaggedSets.ORE_STONE;
import static conductance.api.NCMaterialTaggedSets.ORE_TUFF;
import static conductance.api.NCMaterialTaggedSets.PLASMA;
import static conductance.api.NCMaterialTaggedSets.PLATE;
import static conductance.api.NCMaterialTaggedSets.PLATE_DENSE;
import static conductance.api.NCMaterialTaggedSets.PLATE_DOUBLE;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_DUST;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_GEM;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_INGOT;
import static conductance.api.NCMaterialTaggedSets.RAW_ORE;
import static conductance.api.NCMaterialTaggedSets.RAW_ORE_BLOCK;
import static conductance.api.NCMaterialTaggedSets.RING;
import static conductance.api.NCMaterialTaggedSets.ROD;
import static conductance.api.NCMaterialTaggedSets.ROTOR;
import static conductance.api.NCMaterialTaggedSets.SCREW;
import static conductance.api.NCMaterialTaggedSets.STORAGE_BLOCK;
import static conductance.api.NCMaterialTaggedSets.WIRE_12X;
import static conductance.api.NCMaterialTaggedSets.WIRE_16X;
import static conductance.api.NCMaterialTaggedSets.WIRE_1X;
import static conductance.api.NCMaterialTaggedSets.WIRE_2X;
import static conductance.api.NCMaterialTaggedSets.WIRE_4X;
import static conductance.api.NCMaterialTaggedSets.WIRE_8X;
import static conductance.api.NCMaterialTaggedSets.hasFlag;
import static conductance.api.NCMaterialTaggedSets.hasTrait;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialTaggedSets {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialTaggedSetEvent event) {
		DUST = event.register("dust", ConductanceMaterialTaggedSets::dustUnlocalizedNameGenerator, builder -> builder
				.addTag("dusts/%s", "%s Dusts")
				.addTagUnformatted("dusts", "Dusts")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.DUST)
				.generatorPredicate(PREDICATE_HAS_DUST));

		INGOT = event.register("ingot", ConductanceMaterialTaggedSets::ingotUnlocalizedNameGenerator, builder -> builder
				.addTag("ingots/%s", "%s Ingots")
				.addTagUnformatted("ingots", "Ingots")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.INGOT)
				.generatorPredicate(PREDICATE_HAS_INGOT));
		NUGGET = event.register("nugget", builder -> builder
				.addTag("nuggets/%s", "%s Nuggets")
				.addTagUnformatted("nuggets", "Nuggets")
				.unitValue(CAPI.UNIT / 9)
				.hasItems(true)
				.textureType(NCTextureTypes.NUGGET)
				.generatorPredicate(PREDICATE_HAS_INGOT));

		GEM = event.register("gem", "%s", builder -> builder
				.addTag("gems/%s", "%s Gems")
				.addTagUnformatted("gems", "Gems")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM)
				.generatorPredicate(PREDICATE_HAS_GEM));
		GEM_FLAWED = event.register("flawed_gem", "flawed_%s", builder -> builder
				.addTag("flawed_gems/%s", "Flawed %s Gems")
				.addTagUnformatted("flawed_gems", "Flawed Gems")
				.unitValue(CAPI.UNIT / 2)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM_FLAWED)
				.generatorPredicate(PREDICATE_HAS_GEM));
		GEM_FLAWLESS = event.register("flawless_gem", "flawless_%s", builder -> builder
				.addTag("flawless_gems/%s", "Flawless %s Gems")
				.addTagUnformatted("flawless_gems", "Flawless Gems")
				.unitValue(CAPI.UNIT * 2)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM_FLAWLESS)
				.generatorPredicate(PREDICATE_HAS_GEM));
		GEM_EXQUISITE = event.register("exquisite_gem", "exquisite_%s", builder -> builder
				.addTag("exquisite_gems/%s", "Exquisite %s")
				.addTagUnformatted("exquisite_gems", "Exquisite Gems")
				.unitValue(CAPI.UNIT * 4)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM_EXQUISITE)
				.generatorPredicate(PREDICATE_HAS_GEM));

		STORAGE_BLOCK = event.register("block", "block_of_%s", builder -> builder
				.addTag("storage_blocks/%s", "%s Storage Blocks")
				.addTagUnformatted("storage_blocks", "Storage Blocks")
				.unitValue(CAPI.UNIT * 9)
				.hasBlocks(true)
				.textureType(NCTextureTypes.STORAGE_BLOCK)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(mat -> mat.has(NCMaterialTraits.INGOT) || mat.has(NCMaterialTraits.GEM) || mat.has(NCMaterialFlags.GENERATE_BLOCK)));

		ORE_STONE = event.register("ore", "%s_ore", NCMaterialOreTypes.ORE_TYPE_STONE, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/stone", "Stone ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_GRANITE = event.register("granite_ore", "granite_%s_ore", NCMaterialOreTypes.ORE_TYPE_GRANITE, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/granite", "Granite Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_DIORITE = event.register("diorite_ore", "diorite_%s_ore", NCMaterialOreTypes.ORE_TYPE_DIORITE, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/diorite", "Diorite Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_ANDESITE = event.register("andesite_ore", "andesite_%s_ore", NCMaterialOreTypes.ORE_TYPE_ANDESITE, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/andesite", "Andesite Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_TUFF = event.register("tuff_ore", "tuff_%s_ore", NCMaterialOreTypes.ORE_TYPE_TUFF, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/tuff", "Tuff Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_DEEPSLATE = event.register("deepslate_ore", "deepslate_%s_ore", NCMaterialOreTypes.ORE_TYPE_DEEPSLATE, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/deepslate", "Deepslate Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_NETHERRACK = event.register("nether_ore", "nether_%s_ore", NCMaterialOreTypes.ORE_TYPE_NETHERRACK, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/netherrack", "Netherrack Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_BASALT = event.register("basalt_ore", "basalt_%s_ore", NCMaterialOreTypes.ORE_TYPE_BASALT, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/basalt", "Basalt Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_BLACKSTONE = event.register("blackstone_ore", "blackstone_%s_ore", NCMaterialOreTypes.ORE_TYPE_BLACKSTONE, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/blackstone", "Blackstone Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_END_STONE = event.register("end_ore", "end_%s_ore", NCMaterialOreTypes.ORE_TYPE_END_STONE, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/end_stone", "End Stone Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_GRAVEL = event.register("gravel_ore", "gravel_%s_ore", NCMaterialOreTypes.ORE_TYPE_GRAVEL, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/gravel", "Gravel Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_SHOVEL)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_SAND = event.register("sand_ore", "sand_%s_ore", NCMaterialOreTypes.ORE_TYPE_SAND, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/sand", "Sand Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_SHOVEL)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		ORE_RED_SAND = event.register("red_sand_ore", "red_sand_%s_ore", NCMaterialOreTypes.ORE_TYPE_RED_SAND, builder -> builder
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/red_sand", "Red Sand Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_SHOVEL)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		RAW_ORE = event.register("raw_ore", "raw_%s", builder -> builder
				.addTag("raw_materials/%s", "Raw %s")
				.addTagUnformatted("raw_materials", "Raw Materials")
				.hasItems(true)
				.textureType(NCTextureTypes.RAW_ORE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));
		RAW_ORE_BLOCK = event.register("raw_ore_block", "raw_%s_block", builder -> builder
				.addTag("storage_blocks/raw_%s", "Raw %s Storage Blocks")
				.addTagUnformatted("storage_blocks", "Storage Blocks")
				.hasBlocks(true)
				.textureType(NCTextureTypes.RAW_ORE_BLOCK)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE)));

		LIQUID = event.register("liquid", ConductanceMaterialTaggedSets::liquidUnlocalizedNameGenerator, builder -> builder
				.addTag("%s", "%s")
				.hasFluids(true)
				.textureType(NCTextureTypes.LIQUID)
				.generatorPredicate(hasTrait(NCMaterialTraits.LIQUID))
				.fluidGeneratorCallback((mat, b) -> b.properties(p -> p
						.density(mat.get(NCMaterialTraits.LIQUID).getDensity())
						.viscosity(mat.get(NCMaterialTraits.LIQUID).getViscosity())
						.temperature(mat.get(NCMaterialTraits.LIQUID).getTemperature())
						.lightLevel(mat.getBlockLightLevel())
				)));
		GAS = event.register("gas", ConductanceMaterialTaggedSets::gasUnlocalizedNameGenerator, builder -> builder
				.addTag("gases/%s", "%s Gases")
				.hasFluids(true)
				.textureType(NCTextureTypes.GAS)
				.generatorPredicate(hasTrait(NCMaterialTraits.GAS))
				.fluidGeneratorCallback((mat, b) -> b.properties(p -> p
						.density(mat.get(NCMaterialTraits.GAS).getDensity())
						.viscosity(mat.get(NCMaterialTraits.GAS).getViscosity())
						.temperature(mat.get(NCMaterialTraits.GAS).getTemperature())
						.lightLevel(mat.getBlockLightLevel())
				)));
		PLASMA = event.register("plasma", builder -> builder
				.addTag("plasmas/%s", "%s Plasmas")
				.hasFluids(true)
				.textureType(NCTextureTypes.PLASMA)
				.generatorPredicate(hasTrait(NCMaterialTraits.PLASMA))
				.fluidGeneratorCallback((mat, b) -> b.properties(p -> p
						.density(mat.get(NCMaterialTraits.PLASMA).getDensity())
						.viscosity(mat.get(NCMaterialTraits.PLASMA).getViscosity())
						.temperature(mat.get(NCMaterialTraits.PLASMA).getTemperature())
						.lightLevel(mat.getBlockLightLevel())
				)));

		PLATE = event.register("plate", ConductanceMaterialTaggedSets::plateUnlocalizedNameGenerator, builder -> builder
				.addTag("plates/%s", "%s Plates")
				.addTagUnformatted("plates", "Plates")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.PLATE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE)));
		PLATE_DOUBLE = event.register("double_plate", ConductanceMaterialTaggedSets::plateDoubleUnlocalizedNameGenerator, builder -> builder
				.addTag("double_plates/%s", "Double %s Plates")
				.addTagUnformatted("double_plates", "Double Plates")
				.unitValue(CAPI.UNIT * 2)
				.hasItems(true)
				.textureType(NCTextureTypes.PLATE_DOUBLE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE)));
		PLATE_DENSE = event.register("dense_plate", ConductanceMaterialTaggedSets::plateDenseUnlocalizedNameGenerator, builder -> builder
				.addTag("dense_plates/%s", "Dense %s Plates")
				.addTagUnformatted("dense_plates", "Dense Plates")
				.unitValue(CAPI.UNIT * 9)
				.hasItems(true)
				.textureType(NCTextureTypes.PLATE_DENSE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE)));
		FOIL = event.register("foil", ConductanceMaterialTaggedSets::foilUnlocalizedNameGenerator, builder -> builder
				.addTag("foils/%s", "%s Foils")
				.addTagUnformatted("foils", "Foils")
				.unitValue(CAPI.UNIT / 4)
				.hasItems(true)
				.textureType(NCTextureTypes.FOIL)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FOIL)));

		GEAR = event.register("gear", builder -> builder
				.addTag("gears/%s", "%s Gears")
				.addTagUnformatted("gears", "Gears")
				.unitValue(CAPI.UNIT * 4)
				.hasItems(true)
				.textureType(NCTextureTypes.GEAR)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_GEAR)));
		GEAR_SMALL = event.register("small_gear", "small_%s_gear", builder -> builder
				.addTag("small_gears/%s", "Small %s Gears")
				.addTagUnformatted("small_gears", "Small Gears")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.GEAR_SMALL)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_SMALL_GEAR)));

		LENS = event.register("lens", builder -> builder
				.addTag("lenses/%s", "%s Lenses")
				.addTagUnformatted("lenses", "Lenses")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.LENS)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_LENS)));

		ROD = event.register("rod", builder -> builder
				.addTag("rods/%s", "%s Rods")
				.addTagUnformatted("rods", "Rods")
				.unitValue(CAPI.UNIT / 2)
				.hasItems(true)
				.textureType(NCTextureTypes.ROD)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_ROD)));
		BOLT = event.register("bolt", builder -> builder
				.addTag("bolts/%s", "%s Bolts")
				.addTagUnformatted("bolts", "Bolts")
				.unitValue(CAPI.UNIT / 8)
				.hasItems(true)
				.textureType(NCTextureTypes.BOLT)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_BOLT_AND_SCREW)));
		SCREW = event.register("screw", builder -> builder
				.addTag("screws/%s", "%s Screws")
				.addTagUnformatted("screws", "Screws")
				.unitValue(CAPI.UNIT / 8)
				.hasItems(true)
				.textureType(NCTextureTypes.SCREW)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_BOLT_AND_SCREW)));
		RING = event.register("ring", builder -> builder
				.addTag("rings/%s", "%s Rings")
				.addTagUnformatted("rings", "Rings")
				.unitValue(CAPI.UNIT / 4)
				.hasItems(true)
				.textureType(NCTextureTypes.RING)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_RING)));

		FINE_WIRE = event.register("fine_wire", builder -> builder
				.addTag("fine_wires/%s", "Fine %s Wires")
				.addTagUnformatted("fine_wires", "Fine Wires")
				.unitValue(CAPI.UNIT / 8)
				.hasItems(true)
				.textureType(NCTextureTypes.FINE_WIRE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FINE_WIRE)));
		ROTOR = event.register("rotor", builder -> builder
				.addTag("rotors/%s", "%s Rotors")
				.addTagUnformatted("rotors", "Rotors")
				.unitValue(CAPI.UNIT * 4)
				.hasItems(true)
				.textureType(NCTextureTypes.ROTOR)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_ROTOR)));

		FRAME_BOX = event.register("frame_box", builder -> builder
				.addTag("frame_boxes/%s", "%s Frame Boxes")
				.addTagUnformatted("frame_boxes", "Frame Boxes")
				.unitValue(CAPI.UNIT * 2)
				.hasBlocks(true, true, false)
				.textureType(NCTextureTypes.FRAME_BOX)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FRAME_BOX)));

		WIRE_1X = event.register("1x_wire", "1x_%s_wire", builder -> builder
				.addTag("1x_wires/%s", "1x %s Wires")
				.addTagUnformatted("1x_wires", "1x Wires")
				.unitValue(CAPI.UNIT / 2)
				.hasBlocks(true, false));
		WIRE_2X = event.register("2x_wire", "2x_%s_wire", builder -> builder
				.addTag("2x_wires/%s", "2x %s Wires")
				.addTagUnformatted("2x_wires", "2x Wires")
				.unitValue(CAPI.UNIT)
				.hasBlocks(true, false));
		WIRE_4X = event.register("4x_wire", "4x_%s_wire", builder -> builder
				.addTag("4x_wires/%s", "4x %s Wires")
				.addTagUnformatted("4x_wires", "4x Wires")
				.unitValue(CAPI.UNIT * 2)
				.hasBlocks(true, false));
		WIRE_8X = event.register("8x_wire", "8x_%s_wire", builder -> builder
				.addTag("8x_wires/%s", "8x %s Wires")
				.addTagUnformatted("8x_wires", "8x Wires")
				.unitValue(CAPI.UNIT * 4)
				.hasBlocks(true, false));
		WIRE_12X = event.register("12x_wire", "12x_%s_wire", builder -> builder
				.addTag("12x_wires/%s", "12x %s Wires")
				.addTagUnformatted("12x_wires", "12x Wires")
				.unitValue(CAPI.UNIT * 6)
				.hasBlocks(true, false));
		WIRE_16X = event.register("16x_wire", "16x_%s_wire", builder -> builder
				.addTag("16x_wires/%s", "16x %s Wires")
				.addTagUnformatted("16x_wires", "16x Wires")
				.unitValue(CAPI.UNIT * 8)
				.hasBlocks(true, false));
	}

	private static String dustUnlocalizedNameGenerator(final Material material) {
		if (material.has(NCMaterialFlags.IS_SYNTHETIC) || material.has(NCMaterialTraits.WOOD)) {
			return "%s_pulp";
		}
		return "%s_dust";
	}

	private static String ingotUnlocalizedNameGenerator(final Material material) {
		if (material.has(NCMaterialFlags.IS_SYNTHETIC)) {
			return "%s_bar";
		}
		return "%s_ingot";
	}

	private static String liquidUnlocalizedNameGenerator(final Material material) {
		if (material.has(NCMaterialTraits.INGOT)) {
			return "molten_%s";
		}
		if (material.getDefaultFluid() == NCMaterialTraits.GAS) {
			return "liquid_%s";
		}
		return "%s";
	}

	private static String gasUnlocalizedNameGenerator(final Material material) {
		if (material.getDefaultFluid() == NCMaterialTraits.GAS) {
			return "%s";
		}
		return "%s_gas";
	}

	private static String plateUnlocalizedNameGenerator(final Material material) {
		if (material.has(NCMaterialFlags.IS_SYNTHETIC)) {
			return "%s_sheet";
		}
		if (material.has(NCMaterialTraits.WOOD)) {
			return "%s_plank";
		}
		return "%s_plate";
	}

	private static String plateDoubleUnlocalizedNameGenerator(final Material material) {
		if (material.has(NCMaterialFlags.IS_SYNTHETIC)) {
			return "stitched_%s_sheet";
		}
		if (material.has(NCMaterialTraits.WOOD)) {
			return "double_%s_plank";
		}
		return "double_%s_plate";
	}

	private static String plateDenseUnlocalizedNameGenerator(final Material material) {
		if (material.has(NCMaterialFlags.IS_SYNTHETIC)) {
			return "compressed_%s_sheet";
		}
		return "dense_%s_plate";
	}

	private static String foilUnlocalizedNameGenerator(final Material material) {
		if (material.has(NCMaterialFlags.IS_SYNTHETIC)) {
			return "thin_%s_sheet";
		}
		return "%s_foil";
	}

	private ConductanceMaterialTaggedSets() {
	}
}
