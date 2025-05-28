package conductance.api.material.traits;

import java.util.function.Consumer;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitKey;

public final class MaterialTraitWood implements IMaterialTrait<MaterialTraitWood> {

	@Override
	public void validate(final Material material, final Consumer<MaterialTraitKey<?>> assertTrait) {
		assertTrait.accept(NCMaterialTraits.DUST);
	}
}
