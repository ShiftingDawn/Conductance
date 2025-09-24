package conductance.api.machine.gui;

import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import lombok.Getter;
import conductance.api.machine.MachineBlockEntity;

public class MachineScreen extends AbstractContainerScreen<MachineMenu> {

	private final @Getter MachineBlockEntity<?> machine;

	public MachineScreen(final MachineMenu menu, final Inventory playerInventory, final Component title) {
		super(menu, playerInventory, title);
		this.machine = menu.getMachine();
		this.imageWidth = menu.getContainerSize().width();
		this.imageHeight = menu.getContainerSize().height();
		this.titleLabelX = 4;
		this.titleLabelY = 4;
		Optional.ofNullable(this.getMenu().getPlayerInventoryPos()).ifPresent(pos -> {
			this.inventoryLabelX = pos.x() - 1;
			this.inventoryLabelY = pos.y() - 11;
		});
	}

	@Override
	protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, this.getGuiTheme().getTextColor(), false);
		guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.getGuiTheme().getTextColor(), false);
	}

	@Override
	protected void renderBg(final GuiGraphics guiGraphics, final float partialTicks, final int mouseX, final int mouseY) {
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(this.leftPos, this.topPos);

		this.getGuiTheme().getBackground().draw(guiGraphics, mouseX, mouseY, 0, 0, this.imageWidth, this.imageHeight);
		Optional.ofNullable(this.getMenu().getPlayerInventoryPos()).ifPresent(pos -> {
			this.getGuiTheme().getPlayerInventory().draw(guiGraphics, mouseX, mouseY, pos.x() - 1, pos.y() - 1, 162, 54);
		});
		Optional.ofNullable(this.getMenu().getPlayerHotbarPos()).ifPresent(pos -> {
			this.getGuiTheme().getPlayerHotbar().draw(guiGraphics, mouseX, mouseY, pos.x() - 1, pos.y() - 1, 162, 18);
		});

		guiGraphics.pose().popMatrix();
	}

	protected GuiTheme getGuiTheme() {
		//TODO return machine type setting
		return GuiTheme.THEME_DEFAULT;
	}
}
