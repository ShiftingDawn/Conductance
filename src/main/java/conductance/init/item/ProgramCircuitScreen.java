package conductance.init.item;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import conductance.api.machine.gui.GuiTheme;

public final class ProgramCircuitScreen extends AbstractContainerScreen<ProgramCircuitMenu> {

	public ProgramCircuitScreen(final ProgramCircuitMenu menu, final Inventory playerInventory, final Component title) {
		super(menu, playerInventory, title);
		this.titleLabelY = 4;
	}

	@Override
	protected void init() {
		super.init();
		this.titleLabelX = (this.getXSize() - this.font.width(this.getTitle())) / 2;
	}

	@Override
	protected void renderBg(final GuiGraphics guiGraphics, final float partialTick, final int absoluteMouseX, final int absoluteMouseY) {
		final int mouseX = absoluteMouseX - this.leftPos;
		final int mouseY = absoluteMouseY - this.topPos;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(this.leftPos, this.topPos);

		//Background and player inventory
		this.getTheme().getBackground().draw(guiGraphics, mouseX, mouseY, 0, 0, this.getXSize(), this.getYSize());
		this.getTheme().getPlayerInventory().draw(guiGraphics, mouseX, mouseY, 7, 83, 162, 54);
		this.getTheme().getPlayerHotbar().draw(guiGraphics, mouseX, mouseY, 7, 141, 162, 18);

		//Circuit selector part
		this.getTheme().getPlayerInventory().draw(guiGraphics, mouseX, mouseY, 7, 16, 162, 54);
		for (int i = 0; i <= 24; i++) {
			final int x = 8 + (i % 9) * 18;
			final int y = 17 + (i / 9) * 18;
			if (this.getMenu().getDataSlot().get() == i) {
				//Render twice to deepen color
				this.getTheme().getSlotHover().draw(guiGraphics, mouseX, mouseY, x - 1, y - 1, 18, 18);
				this.getTheme().getSlotHover().draw(guiGraphics, mouseX, mouseY, x - 1, y - 1, 18, 18);
			} else if (mouseX >= x && mouseX <= x + 18 && mouseY >= y && mouseY <= y + 18) {
				this.getTheme().getSlotHover().draw(guiGraphics, mouseX, mouseY, x - 1, y - 1, 18, 18);
			}
			guiGraphics.renderItem(ProgramCircuitItem.makeStack(i), x, y);
		}

		guiGraphics.pose().popMatrix();
	}

	@Override
	protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, this.getTextColor(), false);
		guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.getTextColor(), false);
	}

	@Override
	public void render(final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		this.renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(final double absoluteMouseX, final double absoluteMouseY, final int button) {
		final int mouseX = (int) (absoluteMouseX - this.leftPos);
		final int mouseY = (int) (absoluteMouseY - this.topPos);
		if (mouseX >= 8 && mouseX <= 170 && mouseY >= 17 && mouseY <= 71) {
			final int x = (mouseX - 8) / 18;
			final int y = (mouseY - 17) / 18;
			final int index = y * 9 + x;
			if (index != this.getMenu().getDataSlot().get()) {
				ClientPacketDistributor.sendToServer(new ProgramCircuitSetItemPacketC2S(this.getMenu().getHand(), index));
				this.getMenu().getThePlayer().playSound(SoundEvents.UI_BUTTON_CLICK.value());
			}
			return true;
		}
		return super.mouseClicked(absoluteMouseX, absoluteMouseY, button);
	}

	public GuiTheme getTheme() {
		return GuiTheme.THEME_DEFAULT;
	}

	public int getTextColor() {
		return this.getTheme().getTextColor();
	}
}
