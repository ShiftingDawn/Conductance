package conductance.api.machine.gui;

public interface ProgressProvider {

	enum Direction {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT, UP_TO_DOWN, DOWN_TO_UP
	}

	long getMaxProgress();

	long getCurrentProgress();
}
