package conductance.api;

import java.util.function.Predicate;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.TaggedMaterialSet;

public class NCMaterialTaggedSets {

	public static TaggedMaterialSet DUST;
	public static TaggedMaterialSet INGOT;
	public static TaggedMaterialSet GEM;

	public static TaggedMaterialSet STORAGE_BLOCK;

	public static TaggedMaterialSet LIQUID;
	public static TaggedMaterialSet GAS;
	public static TaggedMaterialSet PLASMA;

	// region Predicates
	public static final Predicate<Material> PREDICATE_HAS_DUST = material -> material.hasTrait(NCMaterialTraits.DUST);
	public static final Predicate<Material> PREDICATE_HAS_INGOT = material -> material.hasTrait(NCMaterialTraits.INGOT);
	public static final Predicate<Material> PREDICATE_HAS_GEM = material -> material.hasTrait(NCMaterialTraits.GEM);

	public static Predicate<Material> hasFlag(final MaterialFlag flag) {
		return material -> material.hasFlag(flag);
	}
	// endregion
}
