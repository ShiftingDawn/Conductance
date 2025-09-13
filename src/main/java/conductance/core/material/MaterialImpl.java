package conductance.core.material;

import java.util.Collections;
import java.util.Set;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;

final class MaterialImpl implements Material {

	private final Set<MaterialFlag> flags;

	MaterialImpl(final Set<MaterialFlag> flags) {
		this.flags = Collections.unmodifiableSet(flags);
	}
}
