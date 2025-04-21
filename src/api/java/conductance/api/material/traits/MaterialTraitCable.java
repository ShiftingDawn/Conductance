package conductance.api.material.traits;

import java.util.Objects;
import lombok.Getter;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;
import conductance.api.util.tier.Tier;

@Getter
public class MaterialTraitCable implements IMaterialTrait<MaterialTraitCable> {

	private final Tier tier;
	private final int amperage;
	private final int cableLoss;

	public MaterialTraitCable(final Tier tier, final int amperage, final int cableLoss) {
		this.tier = tier;
		this.amperage = amperage;
		this.cableLoss = cableLoss;
	}

	@Override
	public void verify(final Material material, final MaterialTraitMap traitMap) {
		traitMap.set(NCMaterialTraits.DUST, new MaterialTraitDust());
	}

	public boolean isSuperconductor() {
		return this.cableLoss == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.tier.getIndex(), this.amperage, this.cableLoss);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		final MaterialTraitCable that = (MaterialTraitCable) o;
		return this.amperage == that.amperage && this.cableLoss == that.cableLoss && this.tier.getIndex() == that.tier.getIndex();
	}
}
