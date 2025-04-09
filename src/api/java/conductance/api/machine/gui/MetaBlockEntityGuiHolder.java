package conductance.api.machine.gui;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MetaBlockEntity;

public interface MetaBlockEntityGuiHolder extends IUIHolder.Block {

	@Override
	default MetaBlockEntity<?> self() {
		return (MetaBlockEntity<?>) Block.super.self();
	}

	default void populateWidgetPanel(final WidgetGroup panel) {
	}

	default int getPlayerInvX() {
		return 7;
	}

	default int getPlayerInvY() {
		return 101;
	}
}
