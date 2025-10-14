package conductance.api.material;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialFlags;

@Getter
@RequiredArgsConstructor
public final class MaterialTraitOre implements MaterialTrait<MaterialTraitOre> {

	private final int dropMultiplier;
	private final int byproductMultiplier;
	private final boolean emissive;
	private final @Nullable Supplier<Material> smeltResult;
	private final @Nullable Supplier<Material> pulverizeResult;

	@Override
	public List<String> validate(final Material material) {
		final List<String> result = new ArrayList<>();
		if (this.smeltResult != null) {
			if (!this.smeltResult.get().hasFlag(NCMaterialFlags.DUST) && !this.smeltResult.get().hasFlag(NCMaterialFlags.INGOT) && !this.smeltResult.get().hasFlag(NCMaterialFlags.GEM)) {
				result.add("Smelt result material requires either dust, ingot or gem flag");
			}
		}
		if (this.pulverizeResult != null) {
			if (!this.pulverizeResult.get().hasFlag(NCMaterialFlags.DUST) && !this.pulverizeResult.get().hasFlag(NCMaterialFlags.INGOT) && !this.pulverizeResult.get().hasFlag(NCMaterialFlags.GEM)) {
				result.add("Pulverize result material requires either dust, ingot or gem flag");
			}
		}
		return result;
	}
}
