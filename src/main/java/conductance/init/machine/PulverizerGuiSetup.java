package conductance.init.machine;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.inventory.Slot;
import conductance.api.NCRecipeTypes;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.gui.GuiDrawableTexture;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.machine.gui.ProgressWidget;
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
		adder.accept("root", Util.make(new WidgetGroup(0, 20, 0, 0), root -> {
			final GuiWidget groupItemsIn = Util.make(this.makeGroup(IO.IN, machine.getInputItems(), screen.getMenu()), group -> root.addWidget("items_in", group));
			final GuiWidget groupItemsOut = Util.make(this.makeGroup(IO.OUT, machine.getOutputItems(), screen.getMenu()), group -> root.addWidget("items_out", group));
			final GuiWidget progress = Util.make(new ProgressWidget(
				new GuiDrawableTexture(NCRecipeTypes.PULVERIZER.getGuiArrow()),
				new RecipeHandlerProgressProvider(machine.getRecipeHandler()),
				ProgressProvider.Direction.LEFT_TO_RIGHT,
				0, 0, 20, 20
			), progressWidget -> root.addWidget("progress", progressWidget));
			final int totalWidth = groupItemsIn.getWidth() + 5 + progress.getWidth() + 5 + groupItemsOut.getWidth();
			final int totalHeight = Math.max(Math.max(groupItemsIn.getHeight(), progress.getHeight()), groupItemsOut.getHeight());
			root.setWidth(totalWidth);
			root.setHeight(totalHeight);
			progress.setInitialX(groupItemsIn.getWidth() + 5);
			progress.setInitialY(totalHeight / 2 - 10);
			groupItemsOut.setInitialX(totalWidth - groupItemsOut.getWidth());
		}));
	}

	private WidgetGroup makeGroup(final IO io, final MachineRecipeCapabilityItems inv, final MachineMenu menu) {
		return Util.make(new WidgetGroup(0, 0, 0, 0), group -> {
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
		final GuiWidget root = screen.getWidgetById("root");
		root.setX((screen.getXSize() - root.getWidth()) / 2);
	}
}
