package conductance.init;

import net.minecraft.tags.BlockTags;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
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
import static conductance.api.NCMaterialTaggedSets.PLASMA;
import static conductance.api.NCMaterialTaggedSets.PLATE;
import static conductance.api.NCMaterialTaggedSets.PLATE_DENSE;
import static conductance.api.NCMaterialTaggedSets.PLATE_DOUBLE;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_DUST;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_GEM;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_INGOT;
import static conductance.api.NCMaterialTaggedSets.RING;
import static conductance.api.NCMaterialTaggedSets.ROD;
import static conductance.api.NCMaterialTaggedSets.ROTOR;
import static conductance.api.NCMaterialTaggedSets.SCREW;
import static conductance.api.NCMaterialTaggedSets.STORAGE_BLOCK;
import static conductance.api.NCMaterialTaggedSets.hasFlag;

public class ConductanceMaterialTaggedSets {

	//@formatter:off
	public static void init(final MaterialTaggedSetRegister register) {
		DUST = register.register("dust")
				.addTag("dusts/%s")
				.addTagUnformatted("dusts")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.DUST)
				.generatorPredicate(PREDICATE_HAS_DUST)
				.build();

		INGOT = register.register("ingot")
				.addTag("ingots/%s")
				.addTagUnformatted("ingots")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.INGOT)
				.generatorPredicate(PREDICATE_HAS_INGOT)
				.build();
		NUGGET = register.register("nugget")
				.addTag("nuggets/%s")
				.addTagUnformatted("nuggets")
				.unitValue(CAPI.UNIT / 9)
				.generateItems(true)
				.textureType(NCTextureTypes.NUGGET)
				.generatorPredicate(PREDICATE_HAS_INGOT)
				.build();

		GEM = register.register("gem", "%s")
				.addTag("gems/%s")
				.addTagUnformatted("gems")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.GEM)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();
		GEM_FLAWED = register.register("flawed_gem", "flawed_%s")
				.addTag("flawed_gems/%s")
				.addTagUnformatted("flawed_gems")
				.unitValue(CAPI.UNIT / 2)
				.generateItems(true)
				.textureType(NCTextureTypes.GEM_FLAWED)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();
		GEM_FLAWLESS = register.register("flawless_gem", "flawless_%s")
				.addTag("flawless_gems/%s")
				.addTagUnformatted("flawless_gems")
				.unitValue(CAPI.UNIT * 2)
				.generateItems(true)
				.textureType(NCTextureTypes.GEM_FLAWLESS)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();
		GEM_EXQUISITE = register.register("exquisite_gem", "exquisite_%s")
				.addTag("exquisite_gems/%s")
				.addTagUnformatted("exquisite_gems")
				.unitValue(CAPI.UNIT * 4)
				.generateItems(true)
				.textureType(NCTextureTypes.GEM_EXQUISITE)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();

		STORAGE_BLOCK = register.register("block", "block_of_%s")
				.addTag("storage_blocks/%s")
				.addTagUnformatted("storage_blocks")
				.unitValue(CAPI.UNIT * 9)
				.generateBlocks(true)
				.textureType(NCTextureTypes.STORAGE_BLOCK)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(mat -> mat.hasTrait(NCMaterialTraits.INGOT) || mat.hasTrait(NCMaterialTraits.GEM) || mat.hasFlag(NCMaterialFlags.GENERATE_BLOCK))
				.build();

		LIQUID = register.register("liquid", mat -> mat.hasTrait(NCMaterialTraits.INGOT) ? "molten_%s" : "%s")
				.addTag("%s")
				.generateFluids(true)
				.textureType(NCTextureTypes.LIQUID)
				.generatorPredicate(mat -> mat.hasTrait(NCMaterialTraits.LIQUID))
				.fluidGeneratorCallback((mat, builder) -> builder.properties(p -> p
						.density(mat.getTrait(NCMaterialTraits.LIQUID).getDensity())
						.viscosity(mat.getTrait(NCMaterialTraits.LIQUID).getViscosity())
						.viscosity(mat.getTrait(NCMaterialTraits.LIQUID).getTemperature())
						.lightLevel(mat.getData().getBlockLightLevel())
				))
				.build();
		GAS = register.register("gas")
				.addTag("gases/%s")
				.generateFluids(true)
				.textureType(NCTextureTypes.GAS)
				.generatorPredicate(mat -> mat.hasTrait(NCMaterialTraits.GAS))
				.fluidGeneratorCallback((mat, builder) -> builder.properties(p -> p
						.density(mat.getTrait(NCMaterialTraits.GAS).getDensity())
						.viscosity(mat.getTrait(NCMaterialTraits.GAS).getViscosity())
						.viscosity(mat.getTrait(NCMaterialTraits.GAS).getTemperature())
						.lightLevel(mat.getData().getBlockLightLevel())
				))
				.build();
		PLASMA = register.register("plasma")
				.addTag("plasmas/%s")
				.generateFluids(true)
				.textureType(NCTextureTypes.PLASMA)
				.generatorPredicate(mat -> mat.hasTrait(NCMaterialTraits.PLASMA))
				.fluidGeneratorCallback((mat, builder) -> builder.properties(p -> p
						.density(mat.getTrait(NCMaterialTraits.PLASMA).getDensity())
						.viscosity(mat.getTrait(NCMaterialTraits.PLASMA).getViscosity())
						.viscosity(mat.getTrait(NCMaterialTraits.PLASMA).getTemperature())
						.lightLevel(mat.getData().getBlockLightLevel())
				))
				.build();

		PLATE = register.register("plate")
				.addTag("plates/%s")
				.addTagUnformatted("plates")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.PLATE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE))
				.build();
		PLATE_DOUBLE = register.register("double_plate", "double_%s_plate")
				.addTag("double_plates/%s")
				.addTagUnformatted("double_plates")
				.unitValue(CAPI.UNIT * 2)
				.generateItems(true)
				.textureType(NCTextureTypes.PLATE_DOUBLE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE))
				.build();
		PLATE_DENSE = register.register("dense_plate", "dense_%s_plate")
				.addTag("dense_plates/%s")
				.addTagUnformatted("dense_plates")
				.unitValue(CAPI.UNIT * 9)
				.generateItems(true)
				.textureType(NCTextureTypes.PLATE_DENSE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_PLATE))
				.build();
		FOIL = register.register("foil")
				.addTag("foils/%s")
				.addTagUnformatted("foils")
				.unitValue(CAPI.UNIT / 4)
				.generateItems(true)
				.textureType(NCTextureTypes.FOIL)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FOIL))
				.build();

		GEAR = register.register("gear")
				.addTag("gears/%s")
				.addTagUnformatted("gears")
				.unitValue(CAPI.UNIT * 4)
				.generateItems(true)
				.textureType(NCTextureTypes.GEAR)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_GEAR))
				.build();
		GEAR_SMALL = register.register("small_gear", "small_%s_gear")
				.addTag("small_gears/%s")
				.addTagUnformatted("small_gears")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.GEAR_SMALL)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_SMALL_GEAR))
				.build();

		LENS = register.register("lens")
				.addTag("lenses/%s")
				.addTagUnformatted("lenses")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.LENS)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_LENS))
				.build();

		ROD = register.register("rod")
				.addTag("rods/%s")
				.addTagUnformatted("rods")
				.unitValue(CAPI.UNIT / 2)
				.generateItems(true)
				.textureType(NCTextureTypes.ROD)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_ROD))
				.build();
		BOLT = register.register("bolt")
				.addTag("bolts/%s")
				.addTagUnformatted("bolts")
				.unitValue(CAPI.UNIT / 8)
				.generateItems(true)
				.textureType(NCTextureTypes.BOLT)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_BOLT_AND_SCREW))
				.build();
		SCREW = register.register("screw")
				.addTag("screws/%s")
				.addTagUnformatted("screws")
				.unitValue(CAPI.UNIT / 8)
				.generateItems(true)
				.textureType(NCTextureTypes.SCREW)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_BOLT_AND_SCREW))
				.build();
		RING = register.register("ring")
				.addTag("rings/%s")
				.addTagUnformatted("rings")
				.unitValue(CAPI.UNIT / 4)
				.generateItems(true)
				.textureType(NCTextureTypes.RING)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_RING))
				.build();

		FINE_WIRE = register.register("fine_wire")
				.addTag("fine_wires/%s")
				.addTagUnformatted("fine_wires")
				.unitValue(CAPI.UNIT / 8)
				.generateItems(true)
				.textureType(NCTextureTypes.FINE_WIRE)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FINE_WIRE))
				.build();
		ROTOR = register.register("rotor")
				.addTag("rotors/%s")
				.addTagUnformatted("rotors")
				.unitValue(CAPI.UNIT * 4)
				.generateItems(true)
				.textureType(NCTextureTypes.ROTOR)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_ROTOR))
				.build();

		FRAME_BOX = register.register("frame_box")
				.addTag("frame_boxes/%s")
				.addTagUnformatted("frame_boxes")
				.unitValue(CAPI.UNIT * 2)
				.generateBlocks(true)
				.textureType(NCTextureTypes.FRAME_BOX)
				.generatorPredicate(hasFlag(NCMaterialFlags.GENERATE_FRAME))
				.build();
	}
	//@formatter:on
}
