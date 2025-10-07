package conductance.api.machine.gui;

import conductance.api.CAPI;

public interface MutablePoint extends Point {

	@Override
	default int x() {
		return this.holderX().getAsInt();
	}

	default int x(final int newX) {
		final int oldX = this.x();
		this.holderX().accept(newX);
		return oldX;
	}

	@Override
	default int y() {
		return this.holderY().getAsInt();
	}

	default int y(final int newY) {
		final int oldY = this.y();
		this.holderY().accept(newY);
		return oldY;
	}

	ManagedInt holderX();

	void holderX(ManagedInt newHolder);

	ManagedInt holderY();

	void holderY(ManagedInt newHolder);

	static MutablePoint of(final Point point) {
		return point instanceof final MutablePoint mut ? mut : MutablePoint.of(point.x(), point.y());
	}

	static MutablePoint of(final ManagedInt x, final ManagedInt y) {
		return new MutablePointImpl(x, y);
	}

	static MutablePoint of(final int x, final int y) {
		final IntHolder holderX = CAPI.make(new IntHolder(), holder -> holder.setValue(x));
		final IntHolder holderY = CAPI.make(new IntHolder(), holder -> holder.setValue(y));
		return MutablePoint.of(new ManagedInt(holderX::setValue, holderY::getValue), new ManagedInt(holderY::setValue, holderY::getValue));
	}
}
