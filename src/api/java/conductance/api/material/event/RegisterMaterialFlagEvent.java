package conductance.api.material.event;

import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialFlagValidator;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialFlagEvent extends IConductancePluginEvent {

	MaterialFlag register(String registryName, Collection<MaterialFlag> requiredFlags, @Nullable MaterialFlagValidator validator);

	default MaterialFlag register(final String registryName, @Nullable final MaterialFlagValidator validator) {
		return this.register(registryName, Collections.emptyList(), validator);
	}

	default MaterialFlag register(final String registryName, final Collection<MaterialFlag> requiredFlags) {
		return this.register(registryName, requiredFlags, null);
	}

	default MaterialFlag register(final String registryName) {
		return this.register(registryName, Collections.emptyList(), null);
	}
}
