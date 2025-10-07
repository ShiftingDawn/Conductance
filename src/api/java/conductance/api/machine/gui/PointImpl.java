package conductance.api.machine.gui;

record PointImpl(int x, int y) implements Point {

	@Override
	public String toString() {
		return "Point[" + this.x() + "," + this.y() + "]";
	}
}
