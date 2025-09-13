package conductance.core.material;

import java.util.Collection;
import java.util.Set;
import conductance.api.material.MaterialFlag;

final class MaterialFlagImpl implements MaterialFlag {

	private final Set<MaterialFlag> requiredFlags;

	MaterialFlagImpl(final Collection<MaterialFlag> requiredFlags) {
		this.requiredFlags = Set.copyOf(requiredFlags);
	}
}
