package conductance.api.machine.gui;

import java.util.Objects;
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
		MutableSize.of(new ManagedInt(null, () -> this.getXSize() - 14), new ManagedInt(null, () -> 67 + (this.getYSize() - GuiSetup.DEFAULT_CONTAINER_SIZE.height())))
	);

	public MachineScreen(final MachineMenu menu, final Inventory playerInventory, final Component title) {
		super(menu, playerInventory, title);
		this.machine = menu.getMachine();
		this.guiSetup = menu.getGuiSetup();
		this.screenBounds.size(this.guiSetup.getContainerSize());
		this.titleLabelX = 4;
		this.titleLabelY = -10;
		menu.getWidgets().values().forEach(widget -> {
			widget.setWidgetPacketHandler(this::sendToServer);
			widget.setScreen(() -> this);
		});
	}

	@Override
	protected void init() {
		super.init();
		this.guiSetup.preInit(this);
		Optional.ofNullable(this.guiSetup.getPlayerInventoryPos(this.screenBounds)).ifPresent(pos -> {
			this.inventoryLabelX = pos.x() - 1;
			this.inventoryLabelY = pos.y() - 11;
		});
		this.guiSetup.init(this, this.contentBounds);
		for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
			widget.initClient();
		}
	}

	@Override
	protected void renderBg(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
		this.translated(guiGraphics, () -> {
			this.getTheme().getTitleBackground().draw(guiGraphics, mouseX, mouseY, 0, this.titleLabelY - 4, this.font.width(this.title) + 8, 14);
			this.getTheme().getBackground().draw(guiGraphics, mouseX, mouseY, 0, 0, this.getXSize(), this.getYSize());
			Optional.ofNullable(this.guiSetup.getPlayerInventoryPos(this.screenBounds)).ifPresent(pos -> {
				this.getTheme().getPlayerInventory().draw(guiGraphics, mouseX, mouseY, pos.x() - 1, pos.y() - 1, 162, 54);
			});
			Optional.ofNullable(this.guiSetup.getPlayerHotbarPos(this.screenBounds)).ifPresent(pos -> {
				this.getTheme().getPlayerHotbar().draw(guiGraphics, mouseX, mouseY, pos.x() - 1, pos.y() - 1, 162, 18);
			});

			for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
				widget.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
			}
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
		for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
			widget.renderTooltips(guiGraphics, mouseX, mouseY, partialTick);
			if (widget.containsMouse(mouseX, mouseY)) {
				widget.handleTooltipCallbacks(guiGraphics, mouseX, mouseY);
			}
		}
	}

	@Override
	public void renderContents(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
		this.translated(guiGraphics, () -> {
			for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
				widget.renderForeground(guiGraphics, mouseX, mouseY, partialTick);
			}
		});
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
		if (this.handleMouseEvent(true, absoluteMouseX, absoluteMouseY, button)) {
			return true;
		}
		return super.mouseClicked(absoluteMouseX, absoluteMouseY, button);
	}

	@Override
	public boolean mouseReleased(final double absoluteMouseX, final double absoluteMouseY, final int button) {
		if (this.handleMouseEvent(false, absoluteMouseX, absoluteMouseY, button)) {
			return true;
		}
		return super.mouseReleased(absoluteMouseX, absoluteMouseY, button);
	}

	private boolean handleMouseEvent(final boolean click, final double absoluteMouseX, final double absoluteMouseY, final int button) {
		final int mouseX = (int) (absoluteMouseX - this.leftPos);
		final int mouseY = (int) (absoluteMouseY - this.topPos);
		for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
			if (!widget.getBounds().contains(mouseX, mouseY)) {
				continue;
			}
			final int mx = mouseX - widget.getPosition().x();
			final int my = mouseY - widget.getPosition().y();
			if (click) {
				if (widget.onMouseClicked(mx, my, button)) {
					return true;
				}
				if (widget.notifyMouseEventListeners(MouseEventListener.Event.PRESS, button, mx, my)) {
					return true;
				}
			} else {
				if (widget.onMouseReleased(mx, my, button)) {
					return true;
				}
				if (widget.notifyMouseEventListeners(MouseEventListener.Event.RELEASE, button, mx, my)) {
					return true;
				}
			}
		}
		return false;
	}

	public @Nullable GuiWidget getWidgetUnderMouse(final int mouseX, final int mouseY) {
		for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
			if (widget.containsMouse(mouseX, mouseY)) {
				if (widget instanceof final WidgetGroup widgetGroup) {
					return this.getWidgetUnderMouse(widgetGroup, mouseX, mouseY);
				}
				return widget;
			}
		}
		return null;
	}

	private GuiWidget getWidgetUnderMouse(final WidgetGroup group, final int mouseX, final int mouseY) {
		for (final GuiWidget widget : group.getWidgets().values()) {
			if (widget.containsMouse(mouseX, mouseY)) {
				if (widget instanceof final WidgetGroup widgetGroup) {
					return this.getWidgetUnderMouse(widgetGroup, mouseX, mouseY);
				}
				return widget;
			}
		}
		return group;
	}

	public final GuiTheme getTheme() {
		return this.guiSetup.getTheme();
	}

	public final int getTextColor() {
		return this.getTheme().getTextColor();
	}

	private void sendToServer(final GuiWidget widget, final int requestId, @Nullable final Consumer<ValueOutput> payloadFactory) {
		Internal.MACHINE_SCREEN_PACKET_SENDER.accept(this.getMenu(), null, contentFactory -> {
			final String key = Objects.requireNonNull(this.getMenu().getWidgetId(widget), "Cannot send client request for unknown widget.");
			contentFactory.putString("w", key);
			contentFactory.putInt("r", requestId);
			if (payloadFactory != null) {
				payloadFactory.accept(contentFactory.child("d"));
			}
		});
	}
}
