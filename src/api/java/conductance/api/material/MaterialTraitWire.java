package conductance.api.material;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.tier.Tier;

@Getter
@RequiredArgsConstructor
public final class MaterialTraitWire implements MaterialTrait<MaterialTraitWire> {

	//TODO validate that owning material has dust flag set
	private final Tier tier;
	private final int amperage;
}
