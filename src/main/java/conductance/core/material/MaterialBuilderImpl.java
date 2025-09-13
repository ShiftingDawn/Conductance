package conductance.core.material;

import java.util.HashSet;
import java.util.Set;
import conductance.api.NCMaterialFlags;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.event.MaterialBuilder;

final class MaterialBuilderImpl implements MaterialBuilder {

	private final Set<MaterialFlag> flags = new HashSet<>();

	@Override
	public MaterialBuilder dust() {
		return this.flag(NCMaterialFlags.DUST);
	}

	@Override
	public MaterialBuilder ingot() {
		return this.flag(NCMaterialFlags.INGOT);
	}

	@Override
	public MaterialBuilder gem() {
		return this.flag(NCMaterialFlags.GEM);
	}

	private MaterialBuilder flag(final MaterialFlag flag) {
		this.flags.add(flag);
		return this;
	}

	public Material build() {
		return new MaterialImpl(this.flags);
	}
}
