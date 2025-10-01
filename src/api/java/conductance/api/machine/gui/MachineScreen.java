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
import conductance.api.machine.MachineBlockEntity;
import conductance.api.util.Internal;

public class MachineScreen extends AbstractContainerScreen<MachineMenu> {

	private final @Getter MachineBlockEntity<?> machine;
	private final @Getter GuiSetup guiSetup;

	public MachineScreen(final MachineMenu menu, final Inventory playerInventory, final Component title) {
		super(menu, playerInventory, title);
		this.machine = menu.getMachine();
		this.guiSetup = menu.getGuiSetup();
		this.imageWidth = this.guiSetup.getContainerSize().width();
		this.imageHeight = this.guiSetup.getContainerSize().height();
		this.titleLabelX = 4;
		this.titleLabelY = -10;
		Optional.ofNullable(this.guiSetup.getPlayerInventoryPos()).ifPresent(pos -> {
			this.inventoryLabelX = pos.x() - 1;
			this.inventoryLabelY = pos.y() - 11;
		});
		menu.getWidgets().values().forEach(widget -> {
			widget.setWidgetPacketHandler(this::sendToServer);
			widget.setScreen(() -> this);
		});
	}

	@Override
	protected void init() {
		super.init();
		this.guiSetup.init(this);
		for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
			widget.initClient();
		}
	}

	@Override
	protected void renderBg(final GuiGraphics guiGraphics, final float partialTick, final int mouseX, final int mouseY) {
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().translate(this.leftPos, this.topPos);

		this.getTheme().getTitleBackground().draw(guiGraphics, mouseX, mouseY, 0, this.titleLabelY - 4, this.font.width(this.title) + 8, 14);
		this.getTheme().getBackground().draw(guiGraphics, mouseX, mouseY, 0, 0, this.imageWidth, this.imageHeight);
		Optional.ofNullable(this.guiSetup.getPlayerInventoryPos()).ifPresent(pos -> {
			this.getTheme().getPlayerInventory().draw(guiGraphics, mouseX, mouseY, pos.x() - 1, pos.y() - 1, 162, 54);
		});
		Optional.ofNullable(this.guiSetup.getPlayerHotbarPos()).ifPresent(pos -> {
			this.getTheme().getPlayerHotbar().draw(guiGraphics, mouseX, mouseY, pos.x() - 1, pos.y() - 1, 162, 18);
		});

		for (final GuiWidget widget : this.getMenu().getWidgets().values()) {
			//TODO check if mouse needs to be translated too
			widget.render(guiGraphics, mouseX, mouseY, partialTick);
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
			if (mouseX < widget.getX() || mouseX > widget.getX() + widget.getWidth()) {
				continue;
			}
			if (mouseY < widget.getY() || mouseY > widget.getY() + widget.getHeight()) {
				continue;
			}
			final int mx = mouseX - widget.getX();
			final int my = mouseY - widget.getY();
			if (click) {
				if (widget.onMouseClicked(mx, my, button)) {
					return true;
				}
			} else if (widget.onMouseReleased(mx, my, button)) {
				return true;
			}
		}
		return false;
	}

	public final GuiTheme getTheme() {
		return this.guiSetup.getTheme();
	}

	public final int getTextColor() {
		return this.getTheme().getTextColor();
	}

	private void sendToServer(final GuiWidget widget, final int requestId, final Consumer<ValueOutput> payloadFactory) {
		Internal.MACHINE_SCREEN_PACKET_SENDER.accept(this.getMenu(), contentFactory -> {
			final String key = Objects.requireNonNull(this.getMenu().getWidgetId(widget), "Cannot send client request for unknown widget.");
			contentFactory.putString("w", key);
			contentFactory.putInt("r", requestId);
			payloadFactory.accept(contentFactory.child("d"));
		});
	}
}
