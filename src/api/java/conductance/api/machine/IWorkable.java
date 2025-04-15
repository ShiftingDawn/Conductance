package conductance.api.machine;

public interface IWorkable extends IControllable {

	int getProgress();

	int getProgressMax();

	boolean isWorking();
}
