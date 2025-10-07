package conductance.api.machine.gui;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.util.Internal;

public class MachineScreen extends AbstractContainerScreen<MachineMenu> {

	private final @Getter MachineBlockEntity<?> machine;
	private final @Getter GuiSetup guiSetup;
	private final MutableRectangle screenBounds = MutableRectangle.of(
		MutablePoint.of(new ManagedInt(null, () -> super.leftPos), new ManagedInt(null, () -> super.topPos)),
		MutableSize.of(new ManagedInt(i -> super.imageWidth = i, () -> super.imageWidth), new ManagedInt(i -> super.imageHeight = i, () -> super.imageHeight))
	);
	private final MutableRectangle contentBounds = MutableRectangle.of(
		MutablePoint.of(7, 7),
		MutableSize.of(new ManagedInt(null, () -> this.getXSize() - 14), new ManagedInt(null, () -> 68 + (this.getYSize() - GuiSetup.DEFAULT_CONTAINER_SIZE.height())))
	);

	public MachineScreen(final MachineMenu menu, final Inventory playerInventory, final Component title) {
		super(menu, playerInventory, title);
		this.machine = menu.getMachine();
		this.guiSetup = menu.getGuiSetup();
		this.screenBounds.size(this.guiSetup.getContainerSize());
		this.titleLabelX = 4;
		this.titleLabelY = -10;
		menu.getRootWidget().setWidgetPacketHandler(this::sendToServer);
		menu.getRootWidget().setScreen(() -> this);
	}

	@Override
	protected void init() {
		super.init();
		this.guiSetup.preInit(this);
		this.guiSetup.init(this, this.contentBounds);
		Optional.ofNullable(this.guiSetup.getPlayerInventoryPos(this.screenBounds)).ifPresent(pos -> {
			this.inventoryLabelX = pos.x();
			this.inventoryLabelY = pos.y() - 10;
			Optional.ofNullable(this.getMenu().getWidgetById("player")).ifPresent(playerContainer -> {
				playerContainer.setPosition(pos);
			});
		});
		this.menu.getRootWidget().initClient();
	}

	@Override
	protected void renderBg(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
		this.translated(guiGraphics, () -> {
			this.getTheme().getTitleBackground().draw(guiGraphics, mouseX, mouseY, 0, this.titleLabelY - 4, this.font.width(this.title) + 8, 14);
			this.getTheme().getBackground().draw(guiGraphics, mouseX, mouseY, 0, 0, this.getXSize(), this.getYSize());
			this.getMenu().getRootWidget().renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		});
	}

	@Override
	protected void renderLabels(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, this.getTextColor(), false);
		guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.getTextColor(), false);
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		this.getMenu().getRootWidget().renderTooltips(guiGraphics, mouseX, mouseY, partialTick);
		if (this.getMenu().getRootWidget().containsMouse(mouseX, mouseY)) {
			this.getMenu().getRootWidget().internalHandleTooltipCallbacks(guiGraphics, mouseX, mouseY);
		}
	}

	@Override
	public void renderContents(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
		this.translated(guiGraphics, () -> this.getMenu().getRootWidget().renderForeground(guiGraphics, mouseX, mouseY, partialTick));
	}

	private void translated(final GuiGraphics guiGraphics, final Runnable callback) {
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(this.leftPos, this.topPos);
		callback.run();
		guiGraphics.pose().popMatrix();
	}

	@Override
	protected void renderSlots(final GuiGraphics guiGraphics) {
		if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable()) {
			this.getTheme().getSlotHighlightBack().draw(guiGraphics, 0, 0, this.hoveredSlot.x - 1, this.hoveredSlot.y - 1, 18, 18);
		}
		super.renderSlots(guiGraphics);
		if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable()) {
			this.getTheme().getSlotHighlightFront().draw(guiGraphics, 0, 0, this.hoveredSlot.x - 1, this.hoveredSlot.y - 1, 18, 18);
		}
	}

	@Override
	public boolean mouseClicked(final double absoluteMouseX, final double absoluteMouseY, final int button) {
		if (this.handleMouseEvent(MouseEventListener.Event.PRESS, absoluteMouseX, absoluteMouseY, button)) {
			return true;
		}
		return super.mouseClicked(absoluteMouseX, absoluteMouseY, button);
	}

	@Override
	public boolean mouseReleased(final double absoluteMouseX, final double absoluteMouseY, final int button) {
		if (this.handleMouseEvent(MouseEventListener.Event.RELEASE, absoluteMouseX, absoluteMouseY, button)) {

			return true;
		}
		return super.mouseReleased(absoluteMouseX, absoluteMouseY, button);
	}

	private boolean handleMouseEvent(final MouseEventListener.Event event, final double absoluteMouseX, final double absoluteMouseY, final int button) {
		final int mouseX = (int) (absoluteMouseX - this.leftPos);
		final int mouseY = (int) (absoluteMouseY - this.topPos);
		if (this.getMenu().getRootWidget().internalHandleMouseEvent(event, mouseX, mouseY, button)) {
			return true;
		}
		return false;
	}

	public final void setImageWidth(final int width) {
		if (width == this.getXSize()) {
			return;
		}
		this.screenBounds.width(width);
		this.repositionElements();
	}

	public final void setImageHeight(final int height) {
		if (height == this.getYSize()) {
			return;
		}
		this.screenBounds.height(height);
		this.repositionElements();
	}

	@Override
	public final int getXSize() {
		return this.screenBounds.width();
	}

	@Override
	public final int getYSize() {
		return this.screenBounds.height();
	}

	public final GuiTheme getTheme() {
		return this.guiSetup.getTheme();
	}

	public final int getTextColor() {
		return this.getTheme().getTextColor();
	}

	private void sendToServer(final IGuiWidget widget, final int requestId, @Nullable final Consumer<ValueOutput> payloadFactory) {
		Internal.MACHINE_SCREEN_PACKET_SENDER.accept(this.getMenu(), null, contentFactory -> {
			contentFactory.putInt("r", requestId);
			if (payloadFactory != null) {
				payloadFactory.accept(contentFactory.child("d"));
			}
		});
	}
}
