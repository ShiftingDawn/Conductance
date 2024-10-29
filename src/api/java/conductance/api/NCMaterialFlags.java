package conductance.api;

import java.util.HashSet;
import java.util.Set;
import conductance.api.material.MaterialFlag;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMaterialFlags {

	// region Property traits
	public static MaterialFlag IS_SYNTHETIC;
	// endregion

	// region Dust traits
	public static MaterialFlag GENERATE_PLATE;
	public static MaterialFlag GENERATE_ROD;
	public static MaterialFlag GENERATE_BOLT_AND_SCREW;
	public static MaterialFlag GENERATE_GEAR;
	public static MaterialFlag GENERATE_SMALL_GEAR;
	public static MaterialFlag GENERATE_BLOCK;
	// endregion

	// region Ingot traits
	public static MaterialFlag GENERATE_FOIL;
	public static MaterialFlag GENERATE_RING;
	public static MaterialFlag GENERATE_FINE_WIRE;
	public static MaterialFlag GENERATE_ROTOR;
	public static MaterialFlag GENERATE_FRAME;
	// endregion

	// region Gem traits
	public static MaterialFlag CAN_CRYSTALLIZE;
	public static MaterialFlag GENERATE_LENS;
	// endregion

	// region Recipe traits
	public static MaterialFlag CAN_MORTAR;
	public static MaterialFlag NO_SMELTING;
	public static MaterialFlag NO_DECOMPOSE;
	public static MaterialFlag NO_HANDLING;
	public static MaterialFlag CAN_CENTRIFUGE;
	public static MaterialFlag CAN_ELECTROLYZE;
	// endregion

	// region Trait collections
	public static final Set<MaterialFlag> METAL_DEFAULT = new HashSet<>();
	public static final Set<MaterialFlag> METAL_EXTRA = new HashSet<>();
	public static final Set<MaterialFlag> METAL_EXTRA2 = new HashSet<>();
	public static final Set<MaterialFlag> METAL_ALL = new HashSet<>();
	// endregion

	private NCMaterialFlags() {
	}
}
