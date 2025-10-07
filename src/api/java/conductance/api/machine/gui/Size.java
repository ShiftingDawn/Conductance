package conductance.api.machine.gui;

public interface Size {

	int width();

	int height();

	static Size of(final int width, final int height) {
		return new SizeImpl(width, height);
	}
}
