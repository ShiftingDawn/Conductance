package conductance.api.machine;

import conductance.api.util.IOMode;

public interface ICapabilityHandler {

	IOMode getCapabilityMode();

	default boolean canCapabilityInput() {
		return this.getCapabilityMode().isInput();
	}

	default boolean canCapabilityOutput() {
		return this.getCapabilityMode().isOutput();
	}
}
