package conductance.api.material.traits;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitKey;

@Getter
@RequiredArgsConstructor
public final class MaterialTraitIngot implements IMaterialTrait<MaterialTraitIngot> {

	@Nullable
	private final Supplier<Material> magneticForm;
	@Nullable
	private final Supplier<Material> demagnetizedForm;

	@Override
	public void validate(final Material material, final Consumer<MaterialTraitKey<?>> assertTrait) {
		assertTrait.accept(NCMaterialTraits.DUST);
		if (material.has(NCMaterialTraits.GEM)) {
			throw new IllegalStateException("Material %s has both an ingot and gem trait, this is not allowed!".formatted(material.getRegistryKey()));
		}

		if (this.magneticForm != null) {
			final Material magMat = this.magneticForm.get();
			if (!magMat.has(NCMaterialTraits.INGOT)) {
				throw new IllegalStateException("Magnetic form %s of material %s does not have an ingot trait!".formatted(magMat.getRegistryKey(), material.getRegistryKey()));
			}
		}
		if (this.demagnetizedForm != null) {
			final Material deMagMat = this.demagnetizedForm.get();
			if (!deMagMat.has(NCMaterialTraits.INGOT)) {
				throw new IllegalStateException("Demagnetized form %s of material %s does not have an ingot trait!".formatted(deMagMat.getRegistryKey(), material.getRegistryKey()));
			}
		}
	}
}
