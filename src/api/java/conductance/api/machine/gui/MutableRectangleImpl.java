package conductance.api.machine.gui;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class MutableRectangleImpl implements MutableRectangle {

	private MutablePoint position;
	private MutableSize size;

	@Override
	public MutablePoint position() {
		return this.position;
	}

	@Override
	public void position(final Point newPosition) {
		if (newPosition instanceof final MutablePoint mut) {
			this.position = mut;
		} else {
			this.position.x(newPosition.x());
			this.position.y(newPosition.y());
		}
	}

	@Override
	public MutableSize size() {
		return this.size;
	}

	@Override
	public void size(final Size newSize) {
		if (newSize instanceof final MutableSize mut) {
			this.size = mut;
		} else {
			this.size.width(newSize.width());
			this.size.height(newSize.height());
		}
	}

	@Override
	public String toString() {
		return "MutableRectangle[" + this.position.x() + "," + this.position.y() + ":" + this.size.width() + "x" + this.size.height() + "]";
	}
}
