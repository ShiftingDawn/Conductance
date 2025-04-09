package conductance.api.machine.gui;

import java.util.function.Supplier;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MetaBlockEntity;

public class MachineGuiSupplier implements MachineGuiTemplate<WidgetGroup, MetaBlockEntity<?>> {

	private final Supplier<WidgetGroup> widgetSupplier;
	private final MachineGuiBinder<MetaBlockEntity<?>> binder;

	public MachineGuiSupplier(final Supplier<WidgetGroup> widgetSupplier, final MachineGuiBinder<MetaBlockEntity<?>> binder) {
		this.widgetSupplier = widgetSupplier;
		this.binder = binder;
	}

	@Override
	public WidgetGroup createDefault() {
		return this.widgetSupplier.get();
	}

	@Override
	public void setupGui(final WidgetGroup template, final MetaBlockEntity<?> instance, final boolean fixBounds) {
		this.binder.bindTemplate(template, instance, fixBounds);
	}
}
