package conductance.api.machine.gui;

public interface Point {

	int x();

	int y();

	static Point of(final int x, final int y) {
		return new PointImpl(x, y);
	}
}
