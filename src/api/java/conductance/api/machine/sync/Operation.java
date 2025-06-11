package conductance.api.machine.sync;

public enum Operation {

	PERSIST_FULL,
	PERSIST_PARTIAL,

	NETWORK_FULL,
	NETWORK_PARTIAL;

	public boolean isFull() {
		return this == Operation.PERSIST_FULL || this == Operation.NETWORK_FULL;
	}

	public boolean isPersist() {
		return this == Operation.PERSIST_FULL || this == Operation.PERSIST_PARTIAL;
	}
}
