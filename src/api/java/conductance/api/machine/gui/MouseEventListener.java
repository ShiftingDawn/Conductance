package conductance.api.machine.gui;

public interface MouseEventListener {

	enum Event { PRESS, RELEASE }

	boolean onMouseEvent(IGuiWidget widget, Event event, int button, int mouseX, int mouseY);
}
