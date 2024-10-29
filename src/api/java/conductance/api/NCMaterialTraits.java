package conductance.api;

import conductance.api.material.MaterialTraitKey;
import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.material.traits.MaterialTraitOre;
import conductance.api.material.traits.MaterialTraitWood;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMaterialTraits {

	public static MaterialTraitKey<MaterialTraitDust> DUST;
	public static MaterialTraitKey<MaterialTraitIngot> INGOT;
	public static MaterialTraitKey<MaterialTraitGem> GEM;
	public static MaterialTraitKey<MaterialTraitOre> ORE;
	public static MaterialTraitKey<MaterialTraitWood> WOOD;

	public static MaterialTraitKey<MaterialTraitFluid.Liquid> LIQUID;
	public static MaterialTraitKey<MaterialTraitFluid.Gas> GAS;
	public static MaterialTraitKey<MaterialTraitFluid.Plasma> PLASMA;

	private NCMaterialTraits() {
	}
}
