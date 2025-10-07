package conductance.init.machine;

import java.util.function.BiConsumer;
import conductance.api.CAPI;
import conductance.api.machine.CapIO;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.Rectangle;
import conductance.api.machine.gui.TankWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.util.IO;

public class MultiBlockFluidHatchPartMachineGuiSetup extends GuiSetup {

	@Override
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		final MultiBlockFluidHatchPartMachine machine = (MultiBlockFluidHatchPartMachine) menu.getMachine();
		final int tanks = machine.getFluids().getTanks();
		final int rowsAndColumns = (int) Math.sqrt(tanks);
		adder.accept("root", CAPI.make(new WidgetGroup(0, 7, rowsAndColumns * 18, rowsAndColumns * 18), root -> {
			root.setBackground(this.getTheme().getFluidSlots(tanks, false));
			for (int i = 0; i < tanks; ++i) {
				final int x = (i % rowsAndColumns) * 18;
				final int y = (i / rowsAndColumns) * 18;
				root.addWidget("fluid_" + i, new TankWidget(x, y, machine.getFluids().getRealFluidHandler(), i, machine.getIo() == IO.IN ? CapIO.BOTH : CapIO.OUT));
			}
		}));
	}

	@Override
	public void init(final MachineScreen screen, final Rectangle rootBounds) {
		final IGuiWidget root = screen.getMenu().getWidgetById("root");
		root.setPosition(rootBounds.center(root.getWidth(), root.getHeight()));
	}
}
