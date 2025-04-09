package conductance.api.machine.gui;

import java.util.function.Supplier;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

public interface MachineGuiTemplate<WIDGET extends Widget, T> {

	WIDGET createDefault();

	void setupGui(WidgetGroup template, T instance, boolean autoCalc);

	record Default<A extends Widget, B>(Supplier<A> supplier, MachineGuiBinder<B> binder) implements MachineGuiTemplate<A, B> {

		@Override
		public A createDefault() {
			return this.supplier.get();
		}

		@Override
		public void setupGui(final WidgetGroup template, final B instance, final boolean autoCalc) {
			this.binder.bindTemplate(template, instance, autoCalc);
		}
	}
}