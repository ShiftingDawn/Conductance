package conductance.api.machine;

public interface IBlockCapabilityHandler {

	CapIO getCapabilityIoMode();

	default boolean canCapabilityInput() {
		return this.getCapabilityIoMode().isInput();
	}

	default boolean canCapabilityOutput() {
		return this.getCapabilityIoMode().isOutput();
	}
}
