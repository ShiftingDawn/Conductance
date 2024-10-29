package conductance.init;

import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCTextureTypes;
import conductance.api.plugin.MaterialTaggedSetRegister;
import static conductance.api.NCMaterialTaggedSets.DUST;
import static conductance.api.NCMaterialTaggedSets.GEM;
import static conductance.api.NCMaterialTaggedSets.INGOT;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_DUST;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_GEM;
import static conductance.api.NCMaterialTaggedSets.PREDICATE_HAS_INGOT;

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
	}
	//@formatter:on
}
