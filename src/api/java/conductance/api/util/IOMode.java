package conductance.api.util;

public enum IOMode {

	INPUT,
	OUTPUT,
	INPUT_OUTPUT,
	BLOCKED;

	public boolean isInput() {
		return this == IOMode.INPUT || this == IOMode.INPUT_OUTPUT;
	}

	public boolean isOutput() {
		return this == IOMode.OUTPUT || this == IOMode.INPUT_OUTPUT;
	}
}
