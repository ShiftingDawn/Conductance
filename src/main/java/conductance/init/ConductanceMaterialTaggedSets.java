package conductance.init;

import net.minecraft.tags.BlockTags;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialOreTypes;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
import conductance.api.material.Material;
import conductance.api.plugin.MaterialTaggedSetRegister;
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

public final class ConductanceMaterialTaggedSets {

	//@formatter:off
	public static void init(final MaterialTaggedSetRegister register) {
		DUST = register.register("dust", ConductanceMaterialTaggedSets::dustUnlocalizedNameGenerator)
				.addTag("dusts/%s",  "%s Dusts")
				.addTagUnformatted("dusts", "Dusts")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.DUST)
				.generatorPredicate(PREDICATE_HAS_DUST)
				.build();

		INGOT = register.register("ingot", ConductanceMaterialTaggedSets::ingotUnlocalizedNameGenerator)
				.addTag("ingots/%s", "%s Ingots")
				.addTagUnformatted("ingots", "Ingots")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.INGOT)
				.generatorPredicate(PREDICATE_HAS_INGOT)
				.build();
		NUGGET = register.register("nugget")
				.addTag("nuggets/%s", "%s Nuggets")
				.addTagUnformatted("nuggets", "Nuggets")
				.unitValue(CAPI.UNIT / 9)
				.hasItems(true)
				.textureType(NCTextureTypes.NUGGET)
				.generatorPredicate(PREDICATE_HAS_INGOT)
				.build();

		GEM = register.register("gem", "%s")
				.addTag("gems/%s", "%s Gems")
				.addTagUnformatted("gems", "Gems")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();
		GEM_FLAWED = register.register("flawed_gem", "flawed_%s")
				.addTag("flawed_gems/%s", "Flawed %s Gems")
				.addTagUnformatted("flawed_gems", "Flawed Gems")
				.unitValue(CAPI.UNIT / 2)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM_FLAWED)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();
		GEM_FLAWLESS = register.register("flawless_gem", "flawless_%s")
				.addTag("flawless_gems/%s", "Flawless %s Gems")
				.addTagUnformatted("flawless_gems", "Flawless Gems")
				.unitValue(CAPI.UNIT * 2)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM_FLAWLESS)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();
		GEM_EXQUISITE = register.register("exquisite_gem", "exquisite_%s")
				.addTag("exquisite_gems/%s", "Exquisite %s")
				.addTagUnformatted("exquisite_gems", "Exquisite Gems")
				.unitValue(CAPI.UNIT * 4)
				.hasItems(true)
				.textureType(NCTextureTypes.GEM_EXQUISITE)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();

		STORAGE_BLOCK = register.register("block", "block_of_%s")
				.addTag("storage_blocks/%s", "%s Storage Blocks")
				.addTagUnformatted("storage_blocks", "Storage Blocks")
				.unitValue(CAPI.UNIT * 9)
				.hasBlocks(true)
				.textureType(NCTextureTypes.STORAGE_BLOCK)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(mat -> mat.hasTrait(NCMaterialTraits.INGOT) || mat.hasTrait(NCMaterialTraits.GEM) || mat.hasFlag(NCMaterialFlags.GENERATE_BLOCK))
				.build();

		ORE_STONE = register.register("ore", "%s_ore", NCMaterialOreTypes.ORE_TYPE_STONE)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/stone", "Stone ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_GRANITE = register.register("granite_ore", "granite_%s_ore", NCMaterialOreTypes.ORE_TYPE_GRANITE)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/granite", "Granite Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_DIORITE = register.register("diorite_ore", "diorite_%s_ore", NCMaterialOreTypes.ORE_TYPE_DIORITE)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/diorite", "Diorite Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_ANDESITE = register.register("andesite_ore", "andesite_%s_ore", NCMaterialOreTypes.ORE_TYPE_ANDESITE)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/andesite", "Andesite Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_TUFF = register.register("tuff_ore", "tuff_%s_ore", NCMaterialOreTypes.ORE_TYPE_TUFF)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/tuff", "Tuff Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_DEEPSLATE = register.register("deepslate_ore", "deepslate_%s_ore", NCMaterialOreTypes.ORE_TYPE_DEEPSLATE)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/deepslate", "Deepslate Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_NETHERRACK = register.register("nether_ore", "nether_%s_ore", NCMaterialOreTypes.ORE_TYPE_NETHERRACK)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/netherrack", "Netherrack Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_BASALT = register.register("basalt_ore", "basalt_%s_ore", NCMaterialOreTypes.ORE_TYPE_BASALT)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/basalt", "Basalt Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_BLACKSTONE = register.register("blackstone_ore", "blackstone_%s_ore", NCMaterialOreTypes.ORE_TYPE_BLACKSTONE)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/blackstone", "Blackstone Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_END_STONE = register.register("end_ore", "end_%s_ore", NCMaterialOreTypes.ORE_TYPE_END_STONE)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/end_stone", "End Stone Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_GRAVEL = register.register("gravel_ore", "gravel_%s_ore", NCMaterialOreTypes.ORE_TYPE_GRAVEL)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/gravel", "Gravel Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_SHOVEL)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_SAND = register.register("sand_ore", "sand_%s_ore", NCMaterialOreTypes.ORE_TYPE_SAND)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/sand", "Sand Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_SHOVEL)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		ORE_RED_SAND = register.register("red_sand_ore", "red_sand_%s_ore", NCMaterialOreTypes.ORE_TYPE_RED_SAND)
				.addTag("ores/%s", "%s Ores")
				.addTagVanilla("%s_ores", "%s Ores")
				.addTagUnformatted("ores", "Ores")
				.addTagUnformatted("ores_in_ground/red_sand", "Red Sand Ores")
				.hasBlocks(true, false)
				.miningTool(BlockTags.MINEABLE_WITH_SHOVEL)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		RAW_ORE = register.register("raw_ore", "raw_%s")
				.addTag("raw_materials/%s", "Raw %s")
				.addTagUnformatted("raw_materials", "Raw Materials")
				.hasItems(true)
				.textureType(NCTextureTypes.RAW_ORE)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();
		RAW_ORE_BLOCK = register.register("raw_ore_block", "raw_%s_block")
				.addTag("storage_blocks/raw_%s", "Raw %s Storage Blocks")
				.addTagUnformatted("storage_blocks", "Storage Blocks")
				.hasBlocks(true)
				.textureType(NCTextureTypes.RAW_ORE_BLOCK)
				.generatorPredicate(hasTrait(NCMaterialTraits.ORE))
				.build();

		LIQUID = register.register("liquid", ConductanceMaterialTaggedSets::liquidUnlocalizedNameGenerator)
				.addTag("%s", "%s")
				.hasFluids(true)
				.textureType(NCTextureTypes.LIQUID)
				.generatorPredicate(hasTrait(NCMaterialTraits.LIQUID))
				.fluidGeneratorCallback((mat, builder) -> builder.properties(p -> p
						.density(mat.getTrait(NCMaterialTraits.LIQUID).getDensity())
						.viscosity(mat.getTrait(NCMaterialTraits.LIQUID).getViscosity())
						.temperature(mat.getTrait(NCMaterialTraits.LIQUID).getTemperature())
						.lightLevel(mat.getData().getBlockLightLevel())
				))
				.build();
		GAS = register.register("gas", ConductanceMaterialTaggedSets::gasUnlocalizedNameGenerator)
				.addTag("gases/%s", "%s Gases")
				.hasFluids(true)
				.textureType(NCTextureTypes.GAS)
				.generatorPredicate(hasTrait(NCMaterialTraits.GAS))
				.fluidGeneratorCallback((mat, builder) -> builder.properties(p -> p
						.density(mat.getTrait(NCMaterialTraits.GAS).getDensity())
						.viscosity(mat.getTrait(NCMaterialTraits.GAS).getViscosity())
						.temperature(mat.getTrait(NCMaterialTraits.GAS).getTemperature())
						.lightLevel(mat.getData().getBlockLightLevel())
				))
				.build();
		PLASMA = register.register("plasma")
				.addTag("plasmas/%s", "%s Plasmas")
				.hasFluids(true)
				.textureType(NCTextureTypes.PLASMA)
				.generatorPredicate(hasTrait(NCMaterialTraits.PLASMA))
				.fluidGeneratorCallback((mat, builder) -> builder.properties(p -> p
						.density(mat.getTrait(NCMaterialTraits.PLASMA).getDensity())
						.viscosity(mat.getTrait(NCMaterialTraits.PLASMA).getViscosity())
						.temperature(mat.getTrait(NCMaterialTraits.PLASMA).getTemperature())
						.lightLevel(mat.getData().getBlockLightLevel())
				))
				.build();

		PLATE = register.register("plate", ConductanceMaterialTaggedSets::plateUnlocalizedNameGenerator)
				.addTag("plates/%s", "%s Plates")
				.addTagUnformatted("plates", "Plates")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.PLATE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE))
				.build();
		PLATE_DOUBLE = register.register("double_plate", ConductanceMaterialTaggedSets::plateDoubleUnlocalizedNameGenerator)
				.addTag("double_plates/%s", "Double %s Plates")
				.addTagUnformatted("double_plates", "Double Plates")
				.unitValue(CAPI.UNIT * 2)
				.hasItems(true)
				.textureType(NCTextureTypes.PLATE_DOUBLE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE))
				.build();
		PLATE_DENSE = register.register("dense_plate", ConductanceMaterialTaggedSets::plateDenseUnlocalizedNameGenerator)
				.addTag("dense_plates/%s", "Dense %s Plates")
				.addTagUnformatted("dense_plates", "Dense Plates")
				.unitValue(CAPI.UNIT * 9)
				.hasItems(true)
				.textureType(NCTextureTypes.PLATE_DENSE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE))
				.build();
		FOIL = register.register("foil", ConductanceMaterialTaggedSets::foilUnlocalizedNameGenerator)
				.addTag("foils/%s", "%s Foils")
				.addTagUnformatted("foils", "Foils")
				.unitValue(CAPI.UNIT / 4)
				.hasItems(true)
				.textureType(NCTextureTypes.FOIL)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FOIL))
				.build();

		GEAR = register.register("gear")
				.addTag("gears/%s", "%s Gears")
				.addTagUnformatted("gears", "Gears")
				.unitValue(CAPI.UNIT * 4)
				.hasItems(true)
				.textureType(NCTextureTypes.GEAR)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_GEAR))
				.build();
		GEAR_SMALL = register.register("small_gear", "small_%s_gear")
				.addTag("small_gears/%s", "Small %s Gears")
				.addTagUnformatted("small_gears", "Small Gears")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.GEAR_SMALL)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_SMALL_GEAR))
				.build();

		LENS = register.register("lens")
				.addTag("lenses/%s", "%s Lenses")
				.addTagUnformatted("lenses", "Lenses")
				.unitValue(CAPI.UNIT)
				.hasItems(true)
				.textureType(NCTextureTypes.LENS)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_LENS))
				.build();

		ROD = register.register("rod")
				.addTag("rods/%s", "%s Rods")
				.addTagUnformatted("rods", "Rods")
				.unitValue(CAPI.UNIT / 2)
				.hasItems(true)
				.textureType(NCTextureTypes.ROD)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_ROD))
				.build();
		BOLT = register.register("bolt")
				.addTag("bolts/%s", "%s Bolts")
				.addTagUnformatted("bolts", "Bolts")
				.unitValue(CAPI.UNIT / 8)
				.hasItems(true)
				.textureType(NCTextureTypes.BOLT)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_BOLT_AND_SCREW))
				.build();
		SCREW = register.register("screw")
				.addTag("screws/%s", "%s Screws")
				.addTagUnformatted("screws", "Screws")
				.unitValue(CAPI.UNIT / 8)
				.hasItems(true)
				.textureType(NCTextureTypes.SCREW)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_BOLT_AND_SCREW))
				.build();
		RING = register.register("ring")
				.addTag("rings/%s", "%s Rings")
				.addTagUnformatted("rings", "Rings")
				.unitValue(CAPI.UNIT / 4)
				.hasItems(true)
				.textureType(NCTextureTypes.RING)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_RING))
				.build();

		FINE_WIRE = register.register("fine_wire")
				.addTag("fine_wires/%s", "Fine %s Wires")
				.addTagUnformatted("fine_wires", "Fine Wires")
				.unitValue(CAPI.UNIT / 8)
				.hasItems(true)
				.textureType(NCTextureTypes.FINE_WIRE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FINE_WIRE))
				.build();
		ROTOR = register.register("rotor")
				.addTag("rotors/%s", "%s Rotors")
				.addTagUnformatted("rotors", "Rotors")
				.unitValue(CAPI.UNIT * 4)
				.hasItems(true)
				.textureType(NCTextureTypes.ROTOR)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_ROTOR))
				.build();

		FRAME_BOX = register.register("frame_box")
				.addTag("frame_boxes/%s", "%s Frame Boxes")
				.addTagUnformatted("frame_boxes", "Frame Boxes")
				.unitValue(CAPI.UNIT * 2)
				.hasBlocks(true, true, false)
				.textureType(NCTextureTypes.FRAME_BOX)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FRAME_BOX))
				.build();

		WIRE_1X = register.register("1x_wire", "1x_%s_wire")
				.addTag("1x_wires/%s", "1x %s Wires")
				.addTagUnformatted("1x_wires", "1x Wires")
				.unitValue(CAPI.UNIT / 2)
				.hasBlocks(true, false)
				.build();
		WIRE_2X = register.register("2x_wire", "2x_%s_wire")
				.addTag("2x_wires/%s", "2x %s Wires")
				.addTagUnformatted("2x_wires", "2x Wires")
				.unitValue(CAPI.UNIT)
				.hasBlocks(true, false)
				.build();
		WIRE_4X = register.register("4x_wire", "4x_%s_wire")
				.addTag("4x_wires/%s", "4x %s Wires")
				.addTagUnformatted("4x_wires", "4x Wires")
				.unitValue(CAPI.UNIT * 2)
				.hasBlocks(true, false)
				.build();
		WIRE_8X = register.register("8x_wire", "8x_%s_wire")
				.addTag("8x_wires/%s", "8x %s Wires")
				.addTagUnformatted("8x_wires", "8x Wires")
				.unitValue(CAPI.UNIT * 4)
				.hasBlocks(true, false)
				.build();
		WIRE_12X = register.register("12x_wire", "12x_%s_wire")
				.addTag("12x_wires/%s", "12x %s Wires")
				.addTagUnformatted("12x_wires", "12x Wires")
				.unitValue(CAPI.UNIT * 6)
				.hasBlocks(true, false)
				.build();
		WIRE_16X = register.register("16x_wire", "16x_%s_wire")
				.addTag("16x_wires/%s", "16x %s Wires")
				.addTagUnformatted("16x_wires", "16x Wires")
				.unitValue(CAPI.UNIT * 8)
				.hasBlocks(true, false)
				.build();
	}
	//@formatter:on

	private static String dustUnlocalizedNameGenerator(final Material material) {
		if (material.hasFlag(NCMaterialFlags.IS_SYNTHETIC) || material.hasTrait(NCMaterialTraits.WOOD)) {
			return "%s_pulp";
		}
		return "%s_dust";
	}

	private static String ingotUnlocalizedNameGenerator(final Material material) {
		if (material.hasFlag(NCMaterialFlags.IS_SYNTHETIC)) {
			return "%s_bar";
		}
		return "%s_ingot";
	}

	private static String liquidUnlocalizedNameGenerator(final Material material) {
		if (material.hasTrait(NCMaterialTraits.INGOT)) {
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
		if (material.hasFlag(NCMaterialFlags.IS_SYNTHETIC)) {
			return "%s_sheet";
		}
		if (material.hasTrait(NCMaterialTraits.WOOD)) {
			return "%s_plank";
		}
		return "%s_plate";
	}

	private static String plateDoubleUnlocalizedNameGenerator(final Material material) {
		if (material.hasFlag(NCMaterialFlags.IS_SYNTHETIC)) {
			return "stitched_%s_sheet";
		}
		if (material.hasTrait(NCMaterialTraits.WOOD)) {
			return "double_%s_plank";
		}
		return "double_%s_plate";
	}

	private static String plateDenseUnlocalizedNameGenerator(final Material material) {
		if (material.hasFlag(NCMaterialFlags.IS_SYNTHETIC)) {
			return "compressed_%s_sheet";
		}
		return "dense_%s_plate";
	}

	private static String foilUnlocalizedNameGenerator(final Material material) {
		if (material.hasFlag(NCMaterialFlags.IS_SYNTHETIC)) {
			return "thin_%s_sheet";
		}
		return "%s_foil";
	}

	private ConductanceMaterialTaggedSets() {
	}
}
