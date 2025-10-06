package conductance.init.machine;

import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import conductance.api.CAPI;
import conductance.api.machine.gui.GuiDrawableTexture;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.TextLabelWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;
import conductance.Conductance;

public class MultiBlockControllerGuiSetup extends GuiSetup {

	@Override
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, GuiWidget> adder) {
		final MultiControllerMachineBlockEntity<?> machine = (MultiControllerMachineBlockEntity<?>) menu.getMachine();
		adder.accept("root", CAPI.make(new WidgetGroup(0, 0, 0, 0), root -> {
			root.setBackground(new GuiDrawableTexture(Conductance.id(Conductance.MODID + "/screen")));
			root.addWidget("structure_status", new TextLabelWidget(2, 2, () -> {
				if (machine.checkStructure()) {
					return Component.translatable("guiWidget.conductance.multiblock.structure.formed");
				} else {
					return Component.translatable("guiWidget.conductance.multiblock.structure.invalid");
				}
			}));
		}));
	}

	@Override
	public void init(final MachineScreen screen) {
		final GuiWidget root = screen.getMenu().getWidgetById("root");
		root.setPosAndSize(7, 7, screen.getXSize() - 14, 67);
	}
}
