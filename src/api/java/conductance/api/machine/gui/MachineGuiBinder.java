package conductance.api.machine.gui;

import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

public interface MachineGuiBinder<T> {

	void bindTemplate(WidgetGroup template, T instance, boolean autoCalc);
}