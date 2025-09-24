package conductance.api.machine;

public interface IBlockCapabilityHandler {

	CapIO getHandlerIo();

	default boolean canCapabilityInput() {
		return this.getHandlerIo().isInput();
	}

	default boolean canCapabilityOutput() {
		return this.getHandlerIo().isOutput();
	}
}
