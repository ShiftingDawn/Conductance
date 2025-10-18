package conductance.api.machine.api;

public interface IWorkable {

	default boolean getDefaultWorkingState() {
		return false;
	}

	void setWorking(boolean working);

	boolean isWorking();
}
