package conductance.api.plugin;

import java.util.Set;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;

public interface RegisterMaterialFlagEvent extends IConductancePluginEvent {

	MaterialFlag register(String name, Set<MaterialFlag> requiredFlags, Set<MaterialTraitKey<?>> requiredTraits);

	default MaterialFlag register(final String name) {
		return this.register(name, Set.of(), Set.of());
	}
}
