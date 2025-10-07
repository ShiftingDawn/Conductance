package conductance.api.machine.gui;

public interface MutableRectangle extends Rectangle, MutablePoint, MutableSize {

	@Override
	MutablePoint position();

	void position(Point newPosition);

	@Override
	MutableSize size();

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

	void size(Size newSize);

	@Override
	default ManagedInt holderX() {
		return this.position().holderX();
	}

	@Override
	default void holderX(final ManagedInt newHolder) {
		this.position().holderX(newHolder);
	}

	@Override
	default ManagedInt holderY() {
		return this.position().holderY();
	}

	@Override
	default void holderY(final ManagedInt newHolder) {
		this.position().holderY(newHolder);
	}

	@Override
	default ManagedInt holderWidth() {
		return this.size().holderWidth();
	}

	@Override
	default void holderWidth(final ManagedInt newHolder) {
		this.size().holderWidth(newHolder);
	}

	@Override
	default ManagedInt holderHeight() {
		return this.size().holderHeight();
	}

	@Override
	default void holderHeight(final ManagedInt newHolder) {
		this.size().holderHeight(newHolder);
	}

	static MutableRectangle of(final Rectangle rectangle) {
		return rectangle instanceof final MutableRectangle mut ? mut : MutableRectangle.of(rectangle.position(), rectangle.size());
	}

	static MutableRectangle of(final Point position, final Size size) {
		return new MutableRectangleImpl(MutablePoint.of(position), MutableSize.of(size));
	}
}
