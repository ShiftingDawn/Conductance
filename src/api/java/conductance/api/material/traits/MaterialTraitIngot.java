package conductance.api.material.traits;

import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

@Getter
@Setter
public final class MaterialTraitIngot implements IMaterialTrait<MaterialTraitIngot> {

	@Nullable
	private Material demagnetizedForm;
	@Nullable
	private Material magneticForm;

	@Override
	public void verify(Material material, MaterialTraitMap traitMap) {
		traitMap.set(NCMaterialTraits.DUST, new MaterialTraitDust());
		if (traitMap.has(NCMaterialTraits.GEM)) {
			throw new IllegalStateException("Material %s has both an ingot and gem trait, this is not allowed!".formatted(material.getRegistryKey()));
		}

		if (this.magneticForm != null) {
			this.magneticForm.getTraits().set(NCMaterialTraits.INGOT, new MaterialTraitIngot());
			// noinspection ConstantConditions
			this.magneticForm.getTrait(NCMaterialTraits.INGOT).setDemagnetizedForm(material);
		}

		if (this.demagnetizedForm != null) {
			this.demagnetizedForm.getTraits().set(NCMaterialTraits.INGOT, new MaterialTraitIngot());
		}
	}
}
