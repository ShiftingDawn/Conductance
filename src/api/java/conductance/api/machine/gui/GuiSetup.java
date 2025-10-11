package conductance.api.machine.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.CapIO;

public class GuiSetup {

	public static final Size DEFAULT_CONTAINER_SIZE = Size.of(176, 166);

	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
	}

	public void addPlayerInventorySlots(final Inventory playerInventory, final Consumer<Slot> adder) {
		if (this.getPlayerInventoryPos(GuiSetup.DEFAULT_CONTAINER_SIZE) == null) {
			return;
		}
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				adder.accept(new RepositionableSlot(playerInventory, x + (y + 1) * 9, x * 18, y * 18, CapIO.BOTH));
			}
		}
		for (int i = 0; i < 9; ++i) {
			adder.accept(new RepositionableSlot(playerInventory, i, i * 18, 0, CapIO.BOTH));
		}
	}

	public void addPlayerInventoryWidget(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		final WidgetGroup container = new WidgetGroup(0, 0, 162, 74);
		final WidgetGroup hotbarGroup = new WidgetGroup(0, 0, 162, 18);
		for (int i = 0; i < 9; ++i) {
			final Slot slot = menu.getSlot(menu.getPlayerInventory(), i);
			if (slot instanceof final RepositionableSlot reposSlot) {
				hotbarGroup.addWidget("player_" + i, new SlotWidget(reposSlot));
			}
		}
		if (!hotbarGroup.getWidgets().isEmpty()) {
			hotbarGroup.setBackground(this.getTheme().getPlayerHotbar());
			hotbarGroup.setPosition(MutablePoint.of(
				new ManagedInt(null, container::getX),
				new ManagedInt(null, () -> container.getBounds().maxY() - 18)
			));
			container.addWidget("player_hotbar", hotbarGroup);
		}
		final WidgetGroup mainGroup = new WidgetGroup(0, 0, 162, 54);
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				final int index = x + (y + 1) * 9;
				final Slot slot = menu.getSlot(menu.getPlayerInventory(), index);
				if (slot instanceof final RepositionableSlot reposSlot) {
					mainGroup.addWidget("player_" + index, new SlotWidget(reposSlot));
				}
			}
		}
		if (!mainGroup.getWidgets().isEmpty()) {
			mainGroup.setBackground(this.getTheme().getPlayerInventory());
			container.addWidget("player_main", mainGroup);
		}
		if (!container.getWidgets().isEmpty()) {
			adder.accept("player", container);
		}
	}

	public void addWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
	}

	public void addControlWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
	}

	public void preInit(final MachineScreen screen) {
	}

	public void init(final MachineScreen screen, final Rectangle rootBounds) {
	}

	/**
	 * Override to change the look and feel of this machine gui.
	 * While changing what this returns
	 *
	 * @return the theme for this machine gui
	 */
	public GuiTheme getTheme() {
		return GuiTheme.THEME_DEFAULT;
	}

	public Size getContainerSize() {
		return GuiSetup.DEFAULT_CONTAINER_SIZE;
	}

	public @Nullable Point getPlayerInventoryPos(final Size screenSize) {
		return Point.of((screenSize.width() - 162) / 2, screenSize.height() - 79);
	}
}
