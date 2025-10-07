package conductance.api.machine.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class GuiSetup {

	public static final Size DEFAULT_CONTAINER_SIZE = Size.of(176, 166);

	public void addSlots(final MachineMenu menu, final Consumer<Slot> adder) {
	}

	public void addWidgets(final MachineMenu menu, final BiConsumer<String, GuiWidget> adder) {
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
		return Point.of(8, screenSize.height() - 79);
	}

	public @Nullable Point getPlayerHotbarPos(final Size screenSize) {
		return Point.of(8, screenSize.height() - 23);
	}
}
