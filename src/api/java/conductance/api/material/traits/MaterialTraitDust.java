package conductance.api.material.traits;

import lombok.Getter;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

@Getter
public final class MaterialTraitDust implements IMaterialTrait<MaterialTraitDust> {

	private int harvestLevel;
	private int burnTime;

	public MaterialTraitDust(final int harvestLevel, final int burnTime) {
		this.harvestLevel = harvestLevel;
		this.burnTime = burnTime;
	}

	public MaterialTraitDust() {
		this(2, 0);
	}

	public void setHarvestLevel(final int harvestLevel) {
		if (harvestLevel <= 0) {
			throw new IllegalArgumentException("harvestLevel must be >= 0!");
		}
		this.harvestLevel = harvestLevel;
	}

	public void setBurnTime(final int burnTime) {
		if (burnTime < 0) {
			throw new IllegalArgumentException("burnTime cannot be negative!");
		}
		this.burnTime = burnTime;
	}

	@Override
	public void verify(Material material, MaterialTraitMap traitMap) {
	}
}
