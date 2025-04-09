package conductance.api.machine.gui;

import net.minecraft.Util;
import net.minecraft.world.entity.player.Inventory;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MetaBlockEntity;

public interface GeneratedGuiHolder extends IUIHolder.Block {

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

	default int getPlayerHotbarX() {
		return 7;
	}

	default int getPlayerHotbarY() {
		return 159;
	}

	default void addPlayerInventory(final WidgetGroup rootWidget, final Inventory player) {
		final WidgetGroup inv = Util.make(new WidgetGroup(this.getPlayerInvX(), this.getPlayerInvY(), 9 * 18, 3 * 18), group -> {
			for (int i = 0; i < 27; ++i) {
				final int row = i / 3;
				final int col = i % 3;
				group.addWidget(new SlotWidget(player, i + 9, col * 18, row * 18)
						.setBackgroundTexture(GuiTextures.PLAYER_INVENTORY)
						.setLocationInfo(true, false));
			}
		});
		rootWidget.addWidget(inv);
	}

	default void addPlayerHotbar(final WidgetGroup rootWidget, final Inventory player) {
		final WidgetGroup hotbar = Util.make(new WidgetGroup(this.getPlayerHotbarX(), this.getPlayerHotbarY(), 9 * 18, 18), group -> {
			for (int i = 0; i < 9; ++i) {
				group.addWidget(new SlotWidget(player, i, i * 18, 0)
						.setBackgroundTexture(GuiTextures.PLAYER_HOTBAR)
						.setLocationInfo(true, true));
			}
		});
		rootWidget.addWidget(hotbar);
	}
}