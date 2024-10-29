package conductance.init;

import net.minecraft.tags.BlockTags;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
import conductance.api.plugin.MaterialTaggedSetRegister;
import static conductance.api.NCMaterialTaggedSets.DUST;
import static conductance.api.NCMaterialTaggedSets.GEM;
import static conductance.api.NCMaterialTaggedSets.INGOT;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_DUST;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_GEM;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_INGOT;
import static conductance.api.NCMaterialTaggedSets.STORAGE_BLOCK;

public class ConductanceMaterialTaggedSets {

	//@formatter:off
	public static void init(MaterialTaggedSetRegister register) {
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
	}
	//@formatter:on
}
