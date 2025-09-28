package conductance.api.machine.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class GuiSetup {

	public static final Size DEFAULT_CONTAINER_SIZE = new Size(176, 166);
	public static final Coordinate DEFAULT_INVENTORY_POS = new Coordinate(8, 87);
	public static final Coordinate DEFAULT_HOTBAR_POS = new Coordinate(8, 143);

	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
	}

	public void addWidgets(final MachineScreen screen, final BiConsumer<String, GuiWidget> adder) {
	}

	public void init(final MachineScreen screen) {
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

	public @Nullable Coordinate getPlayerInventoryPos() {
		return GuiSetup.DEFAULT_INVENTORY_POS;
	}

	public @Nullable Coordinate getPlayerHotbarPos() {
		return GuiSetup.DEFAULT_HOTBAR_POS;
	}
}
