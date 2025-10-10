package conductance.api.machine.gui;

public interface Point {

	int x();

	int y();

	default Point copy() {
		return Point.of(this.x(), this.y());
	}

	static Point of(final int x, final int y) {
		return new PointImpl(x, y);
	}
}
