package conductance.api.machine.gui;

public interface MouseEventListener {

	enum Event { PRESS, RELEASE }

	boolean onMouseEvent(GuiWidget widget, Event event, int button, int mouseX, int mouseY);
}
