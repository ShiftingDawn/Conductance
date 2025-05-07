package conductance.api.material.traits;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

public final class MaterialTraitOre implements IMaterialTrait<MaterialTraitOre> {

	@Getter
	private int dropMultiplier = 1;
	@Getter
	private int byproductMultiplier = 1;
	@Setter
	@Getter
	private boolean emissive = false;
	@Setter
	@Getter
	@Nullable
	private Material smeltResult;
	@Setter
	@Getter
	@Nullable
	private Material pulverizeResult;

	public void setDropMultiplier(final int dropMultiplier) {
		if (dropMultiplier <= 0) {
			throw new IllegalArgumentException("dropMultiplier cannot be <= 0!");
		}
		this.dropMultiplier = dropMultiplier;
	}

	public void setByproductMultiplier(final int byproductMultiplier) {
		if (byproductMultiplier <= 0) {
			throw new IllegalArgumentException("byproductMultiplier cannot be <= 0!");
		}
		this.byproductMultiplier = byproductMultiplier;
	}

	@Override
	public void verify(final Material material, final MaterialTraitMap traitMap) {
		traitMap.set(NCMaterialTraits.DUST, new MaterialTraitDust());

		if (this.smeltResult != null) {
			this.smeltResult.getTraits().set(NCMaterialTraits.DUST, new MaterialTraitDust());
		}
		if (this.pulverizeResult != null) {
			this.pulverizeResult.getTraits().set(NCMaterialTraits.DUST, new MaterialTraitDust());
		}
	}
}
