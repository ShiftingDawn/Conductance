package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import lombok.Getter;
import conductance.api.machine.MachineType;

public class MachineScreen extends AbstractContainerScreen<MachineMenu> {

	private final @Getter MachineType<?> machineType;

	public MachineScreen(final MachineMenu menu, final Inventory playerInventory, final Component title) {
		super(menu, playerInventory, title);
		this.machineType = menu.getMachineType();
	}

	@Override
	protected void renderBg(final GuiGraphics guiGraphics, final float partialTicks, final int mouseX, final int mouseY) {
		this.getGuiTheme().getBackground().draw(guiGraphics, mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
	}

	protected GuiTheme getGuiTheme() {
		//TODO return machine type setting
		return GuiTheme.THEME_DEFAULT;
	}
}
