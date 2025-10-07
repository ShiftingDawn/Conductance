package conductance.api.machine.gui;

record RectangleFixed(Point position, Size size) implements Rectangle {

	@Override
	public String toString() {
		return "Rectangle[" + this.position.x() + "," + this.position.y() + ":" + this.size.width() + "x" + this.size.height() + "]";
	}
}
