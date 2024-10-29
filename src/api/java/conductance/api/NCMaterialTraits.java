package conductance.api;

import conductance.api.material.MaterialTraitKey;
import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;

@SuppressWarnings("NotNullFieldNotInitialized")
public class NCMaterialTraits {

	public static MaterialTraitKey<MaterialTraitDust> DUST;
	public static MaterialTraitKey<MaterialTraitIngot> INGOT;
	public static MaterialTraitKey<MaterialTraitGem> GEM;
}
