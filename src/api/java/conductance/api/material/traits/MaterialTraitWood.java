package conductance.api.material.traits;

import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

public final class MaterialTraitWood implements IMaterialTrait<MaterialTraitWood> {

	@Override
	public void verify(final Material material, final MaterialTraitMap traitMap) {
		traitMap.set(NCMaterialTraits.DUST, new MaterialTraitDust());
	}
}
