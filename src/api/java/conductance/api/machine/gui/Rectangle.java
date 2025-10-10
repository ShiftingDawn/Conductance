package conductance.api.machine.gui;

public interface Rectangle extends Point, Size {

	Point position();

	Size size();

	@Override
	default int x() {
		return this.position().x();
	}

	@Override
	default int y() {
		return this.position().y();
	}

	@Override
	default int width() {
		return this.size().width();
	}

	@Override
	default int height() {
		return this.size().height();
	}

	default int maxX() {
		return this.x() + this.width();
	}

	default int maxY() {
		return this.y() + this.height();
	}

	default int centerX() {
		return this.x() + this.width() / 2;
	}

	default int centerY() {
		return this.y() + this.height() / 2;
	}

	default Point center(final Size size) {
		return Point.of(
			this.x() + (this.width() - size.width()) / 2,
			this.y() + (this.height() - size.height()) / 2
		);
	}

	default Point center(final int width, final int height) {
		return this.center(Size.of(width, height));
	}

	default boolean contains(final int x, final int y) {
		return x >= this.x() && x <= this.maxX() && y >= this.y() && y <= this.maxY();
	}

	@Override
	default Rectangle copy() {
		return Rectangle.of(this.position().copy(), this.size().copy());
	}

	static Rectangle of(final Point position, final Size size) {
		return new RectangleFixed(position, size);
	}

	static Rectangle of(final int x, final int y, final int width, final int height) {
		return Rectangle.of(Point.of(x, y), Size.of(width, height));
	}

	static Rectangle of(final int x, final int y, final Size size) {
		return Rectangle.of(Point.of(x, y), size);
	}

	static Rectangle of(final Point position, final int width, final int height) {
		return Rectangle.of(position, Size.of(width, height));
	}
}
