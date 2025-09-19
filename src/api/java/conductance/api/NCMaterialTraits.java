package conductance.api;

import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.MaterialTraitOre;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMaterialTraits {

	public static MaterialTraitKey<MaterialTraitOre> ORE;

	public static MaterialTraitKey<MaterialTraitFluid.Liquid> LIQUID;
	public static MaterialTraitKey<MaterialTraitFluid.Gas> GAS;
	public static MaterialTraitKey<MaterialTraitFluid.Plasma> PLASMA;

	private NCMaterialTraits() {
	}
}
