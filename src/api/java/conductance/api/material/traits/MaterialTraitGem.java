package conductance.api.material.traits;

import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

public final class MaterialTraitGem implements IMaterialTrait<MaterialTraitGem> {

	@Override
	public void verify(Material material, MaterialTraitMap traitMap) {
		traitMap.set(NCMaterialTraits.DUST, new MaterialTraitDust());
		if (traitMap.has(NCMaterialTraits.INGOT)) {
			throw new IllegalStateException("Material %s has both an gem and ingot trait, this is not allowed!".formatted(material.getRegistryKey()));
		}
	}
}
