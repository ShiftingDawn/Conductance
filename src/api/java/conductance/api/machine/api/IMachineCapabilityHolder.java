package conductance.api.machine.api;

import java.util.Map;
import java.util.Optional;
import conductance.api.machine.MachineCapability;

public interface IMachineCapabilityHolder extends IFeatureBase {

	void registerCapability(String key, MachineCapability capability);

	default <C extends MachineCapability> Optional<C> getCapability(final Class<C> type, final boolean exact) {
		for (final MachineCapability capability : this.getCapabilities().values()) {
			if (exact) {
				if (type.equals(capability.getClass())) {
					return Optional.of(type.cast(capability));
				}
			} else if (type.isAssignableFrom(capability.getClass())) {
				return Optional.of(type.cast(capability));
			}
		}
		return Optional.empty();
	}

	default <C extends MachineCapability> Optional<C> getCapability(final Class<C> type) {
		return this.getCapability(type, false);
	}

	Map<String, MachineCapability> getCapabilities();
}
