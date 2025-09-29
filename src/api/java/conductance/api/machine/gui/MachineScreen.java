package conductance.api.machine.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import lombok.Getter;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.machine.MachineBlockEntity;

public class MachineScreen extends AbstractContainerScreen<MachineMenu> {

	private final Map<String, GuiWidget> widgets = new HashMap<>();
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
	}

	@Override
	protected void init() {
		super.init();
		if (this.widgets.isEmpty()) {
			this.guiSetup.addWidgets(this, this::addWidget);
		}
		this.guiSetup.init(this);
		for (final GuiWidget widget : this.widgets.values()) {
			widget.init(this);
		}
	}

	public final <T extends GuiWidget> T addWidget(final String id, final T widget) {
		this.widgets.put(id, widget);
		return widget;
	}

	@UnknownNullability
	public GuiWidget getWidgetById(final String id) {
		GuiWidget result = this.widgets.get(id);
		if (result == null) {
			for (final GuiWidget widget : this.widgets.values()) {
				if (widget instanceof final WidgetGroup group) {
					result = group.getWidgetById(id);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return result;
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

		for (final GuiWidget widget : this.widgets.values()) {
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

	public final GuiTheme getTheme() {
		return this.guiSetup.getTheme();
	}

	public final int getTextColor() {
		return this.getTheme().getTextColor();
	}
}
