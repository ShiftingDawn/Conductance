package conductance.api.machine;

import conductance.api.util.IOMode;

public interface ICapabilityHandler {

	IOMode getHandlerIoMode();

	default boolean canCapabilityInput() {
		return this.getHandlerIoMode().isInput();
	}

	default boolean canCapabilityOutput() {
		return this.getHandlerIoMode().isOutput();
	}
}
