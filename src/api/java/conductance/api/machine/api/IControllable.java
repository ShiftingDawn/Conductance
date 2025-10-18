package conductance.api.machine.api;

public interface IControllable {

	void setProcessingAllowed(boolean allowed);

	boolean isProcessingAllowed();
}
