package conductance.init;

import net.minecraft.tags.BlockTags;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
import conductance.api.plugin.MaterialTaggedSetRegister;
import static conductance.api.NCMaterialTaggedSets.*;

public class ConductanceMaterialTaggedSets {

	//@formatter:off
	public static void init(final MaterialTaggedSetRegister register) {
		DUST = register.register("dust")
				.addTagCommon("dusts/%s")
				.addTagCommonUnformatted("dusts")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.DUST)
				.generatorPredicate(PREDICATE_HAS_DUST)
				.build();
		INGOT = register.register("ingot")
				.addTagCommon("ingots/%s")
				.addTagCommonUnformatted("ingots")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.INGOT)
				.generatorPredicate(PREDICATE_HAS_INGOT)
				.build();
		GEM = register.register("gem")
				.addTagCommon("gems/%s")
				.addTagCommonUnformatted("gems")
				.unitValue(CAPI.UNIT)
				.generateItems(true)
				.textureType(NCTextureTypes.GEM)
				.generatorPredicate(PREDICATE_HAS_GEM)
				.build();

		STORAGE_BLOCK = register.register("block", "block_of_%s")
				.addTagCommon("storage_blocks/%s")
				.addTagCommonUnformatted("storage_blocks")
				.unitValue(CAPI.UNIT * 9)
				.generateBlocks(true)
				.textureType(NCTextureTypes.STORAGE_BLOCK)
				.miningTool(BlockTags.MINEABLE_WITH_PICKAXE)
				.generatorPredicate(mat -> mat.hasTrait(NCMaterialTraits.INGOT) || mat.hasTrait(NCMaterialTraits.GEM) || mat.hasFlag(NCMaterialFlags.GENERATE_BLOCK))
				.build();

		LIQUID = register.register("liquid", mat -> mat.hasTrait(NCMaterialTraits.INGOT) ? "molten_%s" : "%s")
				.addTagCommon("%s")
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
				.addTagCommon("gases/%s")
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
				.addTagCommon("plasmas/%s")
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
	}
	//@formatter:on
}
