package conductance.api;

import java.util.function.Predicate;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.TaggedMaterialSet;

public class NCMaterialTaggedSets {

	public static TaggedMaterialSet DUST;

	public static TaggedMaterialSet INGOT;
	public static TaggedMaterialSet NUGGET;

	public static TaggedMaterialSet GEM;
	public static TaggedMaterialSet GEM_FLAWED;
	public static TaggedMaterialSet GEM_FLAWLESS;
	public static TaggedMaterialSet GEM_EXQUISITE;

	public static TaggedMaterialSet STORAGE_BLOCK;

	public static TaggedMaterialSet LIQUID;
	public static TaggedMaterialSet GAS;
	public static TaggedMaterialSet PLASMA;

	public static TaggedMaterialSet PLATE;
	public static TaggedMaterialSet PLATE_DOUBLE;
	public static TaggedMaterialSet PLATE_DENSE;
	public static TaggedMaterialSet FOIL;

	public static TaggedMaterialSet GEAR;
	public static TaggedMaterialSet GEAR_SMALL;

	public static TaggedMaterialSet LENS;

	public static TaggedMaterialSet ROD;
	public static TaggedMaterialSet BOLT;
	public static TaggedMaterialSet SCREW;
	public static TaggedMaterialSet RING;

	public static TaggedMaterialSet FINE_WIRE;
	public static TaggedMaterialSet ROTOR;

	public static TaggedMaterialSet FRAME_BOX;

	// region Predicates
	public static final Predicate<Material> PREDICATE_HAS_DUST = material -> material.hasTrait(NCMaterialTraits.DUST);
	public static final Predicate<Material> PREDICATE_HAS_INGOT = material -> material.hasTrait(NCMaterialTraits.INGOT);
	public static final Predicate<Material> PREDICATE_HAS_GEM = material -> material.hasTrait(NCMaterialTraits.GEM);

	public static Predicate<Material> hasFlag(final MaterialFlag flag) {
		return material -> material.hasFlag(flag);
	}
	// endregion
}
