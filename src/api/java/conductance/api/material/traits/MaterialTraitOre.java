package conductance.api.material.traits;

import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

@AllArgsConstructor
public final class MaterialTraitOre implements IMaterialTrait<MaterialTraitOre> {

	@Getter
	private int dropMultiplier = 1;
	@Getter
	private int byproductMultiplier = 1;
	@Getter
	private boolean emissive = false;
	@Nullable
	@Getter
	private Supplier<Material> pulverizeResult;
	@Nullable
	@Getter
	private Supplier<Material> smeltResult;

	@Override
	public void verify(final Material material, final MaterialTraitMap traitMap) {
		traitMap.set(NCMaterialTraits.DUST, new MaterialTraitDust());

		if (this.smeltResult != null) {
			this.smeltResult.get().getTraits().set(NCMaterialTraits.DUST, new MaterialTraitDust());
		}
		if (this.pulverizeResult != null) {
			this.pulverizeResult.get().getTraits().set(NCMaterialTraits.DUST, new MaterialTraitDust());
		}
	}
}
