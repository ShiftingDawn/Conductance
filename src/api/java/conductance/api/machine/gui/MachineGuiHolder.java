package conductance.api.machine.gui;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MachineBlockEntity;

public interface MachineGuiHolder extends IUIHolder.Block {

	@Override
	default MachineBlockEntity<?> self() {
		return (MachineBlockEntity<?>) Block.super.self();
	}

	default void populateWidgetPanel(final WidgetGroup panel) {
	}

	default int getPlayerInvX() {
		return 7;
	}

	default int getPlayerInvY() {
		return 101;
	}

	default GuiTheme getGuiTheme() {
		return GuiTheme.THEME_DEFAULT;
	}
}
