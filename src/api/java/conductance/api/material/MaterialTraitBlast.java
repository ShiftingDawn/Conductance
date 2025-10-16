package conductance.api.material;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.NCMaterialFlags;
import conductance.api.coil.CoilBlockType;
import conductance.api.tier.Tier;

@Getter
@RequiredArgsConstructor
public final class MaterialTraitBlast implements MaterialTrait<MaterialTraitBlast> {

	private final int temperature;
	private final Tier recipeTier;

	public MaterialTraitBlast(final CoilBlockType coilBlockType, final Tier recipeTier) {
		this(coilBlockType.getTemperature() - 250, recipeTier);
	}

	@Override
	public List<String> validate(final Material material) {
		if (!material.hasFlag(NCMaterialFlags.DUST) || !material.hasFlag(NCMaterialFlags.INGOT)) {
			return List.of("Blast trait requires dust and ingot flag");
		}
		return List.of();
	}
}
