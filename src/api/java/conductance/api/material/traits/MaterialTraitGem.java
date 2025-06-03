package conductance.api.material.traits;

import java.util.function.Consumer;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitKey;

public final class MaterialTraitGem implements IMaterialTrait<MaterialTraitGem> {

	@Override
	public void validate(final Material material, final Consumer<MaterialTraitKey<?>> assertTrait) {
		assertTrait.accept(NCMaterialTraits.DUST);
		if (material.has(NCMaterialTraits.INGOT)) {
			throw new IllegalStateException("Material %s has both an gem and ingot trait, this is not allowed!".formatted(material.getRegistryKey()));
		}
	}
}
