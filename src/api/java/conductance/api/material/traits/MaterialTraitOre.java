package conductance.api.material.traits;

import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitKey;

@Getter
@RequiredArgsConstructor
public final class MaterialTraitOre implements IMaterialTrait<MaterialTraitOre> {

	private final int dropMultiplier;
	private final int byproductMultiplier;
	private final boolean emissive;
	@Nullable
	private final Supplier<Material> smeltResult;
	@Nullable
	private final Supplier<Material> pulverizeResult;

	@Override
	public void validate(final Material material, final Consumer<MaterialTraitKey<?>> assertTrait) {
		assertTrait.accept(NCMaterialTraits.DUST);

		if (this.smeltResult != null) {
			final Material smeltMat = this.smeltResult.get();
			if (!smeltMat.has(NCMaterialTraits.DUST)) {
				throw new IllegalStateException("Smelt result %s of material %s does not have a dust/ingot/gem trait!".formatted(smeltMat.getRegistryKey(), material.getRegistryKey()));
			}
		}
		if (this.pulverizeResult != null) {
			final Material pulverizeMat = this.pulverizeResult.get();
			if (!pulverizeMat.has(NCMaterialTraits.DUST)) {
				throw new IllegalStateException("Pulverize result %s of material %s does not have a dust/ingot/gem trait!".formatted(pulverizeMat.getRegistryKey(), material.getRegistryKey()));
			}
		}
	}
}
