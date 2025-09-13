package conductance.api.material.event;

import java.util.Collection;
import java.util.Collections;
import conductance.api.material.MaterialFlag;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialFlagEvent extends IConductancePluginEvent {

	MaterialFlag register(String registryName, Collection<MaterialFlag> requiredFlags);

	default MaterialFlag register(final String registryName) {
		return this.register(registryName, Collections.emptyList());
	}
}
