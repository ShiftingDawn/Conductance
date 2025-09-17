package conductance.api;

import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMaterialTraits {

	public static MaterialTraitKey<MaterialTraitFluid.Liquid> LIQUID;
	public static MaterialTraitKey<MaterialTraitFluid.Gas> GAS;
	public static MaterialTraitKey<MaterialTraitFluid.Plasma> PLASMA;

	private NCMaterialTraits() {
	}
}
