package conductance.api.material;

import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public final class MaterialTraitOre implements MaterialTrait<MaterialTraitOre> {

	private final int dropMultiplier;
	private final int byproductMultiplier;
	private final boolean emissive;
	//TODO validate that these materials have a dust, ingot or gem flag
	private final @Nullable Supplier<Material> smeltResult;
	private final @Nullable Supplier<Material> pulverizeResult;
}
