package conductance.api.machine.gui;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class MutablePointImpl implements MutablePoint {

	private ManagedInt x;
	private ManagedInt y;

	@Override
	public ManagedInt holderX() {
		return this.x;
	}

	@Override
	public void holderX(final ManagedInt newHolder) {
		this.x = newHolder;
	}

	@Override
	public ManagedInt holderY() {
		return this.y;
	}

	@Override
	public void holderY(final ManagedInt newHolder) {
		this.y = newHolder;
	}

	@Override
	public String toString() {
		return "MutablePoint[" + this.x() + "," + this.y() + "]";
	}
}
