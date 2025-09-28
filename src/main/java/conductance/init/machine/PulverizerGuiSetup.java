package conductance.init.machine;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.inventory.Slot;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.RepositionableSlotItemHandler;
import conductance.api.machine.gui.SlotWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.util.IO;

public class PulverizerGuiSetup extends GuiSetup {

	@Override
	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
		final PulverizerMachine machine = (PulverizerMachine) menu.getMachine();
		for (int i = 0; i < machine.getInputItems().getSlots(); ++i) {
			adder.accept(new RepositionableSlotItemHandler(machine.getInputItems().getInventory(), i, i * 18, 0));
		}
		for (int i = 0; i < machine.getOutputItems().getSlots(); ++i) {
			adder.accept(new RepositionableSlotItemHandler(machine.getOutputItems().getInventory(), i, i * 18, 0));
		}
	}

	@Override
	public void addWidgets(final MachineScreen screen, final BiConsumer<String, GuiWidget> adder) {
		final PulverizerMachine machine = (PulverizerMachine) screen.getMachine();
		adder.accept("items_in", Util.make(this.makeGroup(IO.IN, machine.getInputItems(), screen.getMenu()), group -> {
			group.setBackground(screen.getTheme().getItemSlots(machine.getInputItems().getSlots(), false));
		}));
		adder.accept("items_out", Util.make(this.makeGroup(IO.OUT, machine.getOutputItems(), screen.getMenu()), group -> {
			group.setBackground(screen.getTheme().getItemSlots(machine.getOutputItems().getSlots(), true));
		}));
	}

	private WidgetGroup makeGroup(final IO io, final MachineRecipeCapabilityItems inv, final MachineMenu menu) {
		return Util.make(new WidgetGroup(0, 20, 0, 0), group -> {
			final int cols = inv.getSlots() == 4 ? 2 : Math.min(inv.getSlots(), 3);
			final int rows = inv.getSlots() == 0 ? 0 : inv.getSlots() / cols + Math.min(1, inv.getSlots() % cols);
			group.setWidth(cols * 18);
			group.setHeight(rows * 18);
			for (int i = 0; i < inv.getSlots(); ++i) {
				final RepositionableSlotItemHandler slot = (RepositionableSlotItemHandler) menu.getSlot(inv.getInventory(), i);
				slot.setRelativeX((i % cols) * 18 + 1);
				slot.setRelativeY((i / cols) * 18 + 1);
				group.addWidget("items_" + io + "_" + i, new SlotWidget(slot));
			}
		});
	}

	@Override
	public void init(final MachineScreen screen) {
		Util.make(screen.getWidgetById("items_in"), group -> {
			group.setX(screen.getXSize() / 2 - 5 - group.getWidth());
		});
		Util.make(screen.getWidgetById("items_out"), group -> {
			group.setX(screen.getXSize() / 2 + 5);
		});
	}
}
