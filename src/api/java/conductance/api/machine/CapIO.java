package conductance.api.machine;

public enum CapIO {

	IN, OUT, BOTH, NONE;

	public boolean isInput() {
		return this == CapIO.IN || this == CapIO.BOTH;
	}

	public boolean isOutput() {
		return this == CapIO.OUT || this == CapIO.BOTH;
	}
}
