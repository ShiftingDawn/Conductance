package conductance.init.machine;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.world.inventory.Slot;
import conductance.api.CAPI;
import conductance.api.machine.CapIO;
import conductance.api.machine.gui.GuiDrawableTexture;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.Rectangle;
import conductance.api.machine.gui.RepositionableSlotItemHandler;
import conductance.api.machine.gui.SlotWidget;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.util.GuiUtils;
import conductance.api.util.IO;
import conductance.Conductance;

public class MultiBlockItemBusPartMachineGuiSetup extends GuiSetup {

	@Override
	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
		final MultiBlockItemBusPartMachine machine = (MultiBlockItemBusPartMachine) menu.getMachine();
		final int rowsAndColumns = (int) Math.sqrt(machine.getItems().getSlots());
		for (int i = 0; i < machine.getItems().getSlots(); ++i) {
			final int x = (i % rowsAndColumns) * 18;
			final int y = (i / rowsAndColumns) * 18;
			adder.accept(new RepositionableSlotItemHandler(machine.getItems().getInventory(), i, x, y, machine.getIo() == IO.IN ? CapIO.BOTH : CapIO.OUT));
		}
	}

	@Override
	public void preInit(final MachineScreen screen) {
		final int rowsAndColumns = (int) Math.sqrt(((MultiBlockItemBusPartMachine) screen.getMachine()).getItems().getSlots());
		if (rowsAndColumns > 3) {
			screen.setImageHeight(this.getContainerSize().height() - 14 + (rowsAndColumns - 3) * 18);
		}
		if (rowsAndColumns > 9) {
			screen.setImageWidth(this.getContainerSize().width() + (rowsAndColumns - 9) * 18);
		}
	}

	@Override
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		final MultiBlockItemBusPartMachine machine = (MultiBlockItemBusPartMachine) menu.getMachine();
		final int slots = machine.getItems().getSlots();
		final int rowsAndColumns = (int) Math.sqrt(slots);
		adder.accept("root", CAPI.make(new WidgetGroup(0, 7, rowsAndColumns * 18, rowsAndColumns * 18), root -> {
			root.setBackground(new GuiDrawableTexture(Conductance.id("conductance/item_bus_" + slots)));
			for (int i = 0; i < slots; ++i) {
				final RepositionableSlotItemHandler slot = (RepositionableSlotItemHandler) menu.getSlot(machine.getItems().getInventory(), i);
				root.addWidget("item_" + i, new SlotWidget(slot));
			}
		}));
	}

	@Override
	public void addControlWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		final MultiBlockItemBusPartMachine machine = (MultiBlockItemBusPartMachine) menu.getMachine();
		adder.accept("auto", CAPI.make(new ToggleButtonWidget(
			0, 0, 0, 0,
			new ManagedBoolean(machine::setAutoEnabled, machine::isCurrentlyWorking),
			toggled -> {
				if (machine.getIo() == IO.IN) {
					return toggled ? GuiTextures.TEXTURE_AUTO_ITEM_INPUT_ON.get() : GuiTextures.TEXTURE_AUTO_ITEM_INPUT_OFF.get();
				} else {
					return toggled ? GuiTextures.TEXTURE_AUTO_ITEM_OUTPUT_ON.get() : GuiTextures.TEXTURE_AUTO_ITEM_OUTPUT_OFF.get();
				}
			}, null
		), button -> button.addTooltipCallback((widget, tooltip) -> {
			final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
			if (machine.getIo() == IO.IN) {
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.auto_input.item.disable" : "guiWidget.conductance.auto_input.item.enable");
			} else {
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.auto_output.item.disable" : "guiWidget.conductance.auto_output.item.enable");
			}
		})));
	}

	@Override
	public void init(final MachineScreen screen, final Rectangle rootBounds) {
		final IGuiWidget root = screen.getMenu().getWidgetById("root");
		root.setPosition(rootBounds.center(root.getWidth(), root.getHeight()));
	}
}
