package conductance.api;

import conductance.api.material.MaterialTraitBlast;
import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.MaterialTraitOre;
import conductance.api.material.MaterialTraitWire;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMaterialTraits {

	public static MaterialTraitKey<MaterialTraitOre> ORE;

	public static MaterialTraitKey<MaterialTraitWire> WIRE;

	public static MaterialTraitKey<MaterialTraitFluid.Liquid> LIQUID;
	public static MaterialTraitKey<MaterialTraitFluid.Gas> GAS;
	public static MaterialTraitKey<MaterialTraitFluid.Plasma> PLASMA;

	public static MaterialTraitKey<MaterialTraitBlast> BLAST;

	private NCMaterialTraits() {
	}
}
