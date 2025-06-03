package conductance.api.material.traits;

import java.util.function.Consumer;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitKey;

public final class MaterialTraitDust implements IMaterialTrait<MaterialTraitDust> {

	@Override
	public void validate(final Material material, final Consumer<MaterialTraitKey<?>> assertTrait) {
	}
}
