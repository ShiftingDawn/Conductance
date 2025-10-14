package conductance.api.material;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.NCMaterialFlags;
import conductance.api.tier.Tier;

@Getter
@RequiredArgsConstructor
public final class MaterialTraitWire implements MaterialTrait<MaterialTraitWire> {

	private final Tier tier;
	private final int amperage;

	@Override
	public List<String> validate(final Material material) {
		if (!material.hasFlag(NCMaterialFlags.DUST)) {
			return List.of("Wire trait requires dust flag");
		}
		return List.of();
	}
}
