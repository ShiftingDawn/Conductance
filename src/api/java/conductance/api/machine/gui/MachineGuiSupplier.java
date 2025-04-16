package conductance.api.machine.gui;

import java.util.function.Supplier;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MachineBlockEntity;

public class MachineGuiSupplier implements MachineGuiTemplate<WidgetGroup, MachineBlockEntity<?>> {

	private final Supplier<WidgetGroup> widgetSupplier;
	private final MachineGuiBinder<MachineBlockEntity<?>> binder;

	public MachineGuiSupplier(final Supplier<WidgetGroup> widgetSupplier, final MachineGuiBinder<MachineBlockEntity<?>> binder) {
		this.widgetSupplier = widgetSupplier;
		this.binder = binder;
	}

	@Override
	public WidgetGroup createDefault() {
		return this.widgetSupplier.get();
	}

	@Override
	public void setupGui(final WidgetGroup template, final MachineBlockEntity<?> instance, final GuiTheme theme, final boolean autoCalc) {
		this.binder.bindTemplate(template, instance, theme, autoCalc);
	}
}
