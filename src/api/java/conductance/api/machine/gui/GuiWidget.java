package conductance.api.machine.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public abstract class GuiWidget {

	private final List<MouseEventListener> mouseEventListeners = new ArrayList<>();
	private final List<TooltipCallback> tooltipCallbacks = new ArrayList<>();
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineMenu> menu;
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineScreen> screen;
	@Setter(AccessLevel.PACKAGE)
	private WidgetPacketHandler widgetPacketHandler;
	private @Getter int initialX;
	private @Getter int initialY;
	private @Getter int x;
	private @Getter int y;
	private @Getter int width;
	private @Getter int height;
	private @Nullable GuiDrawable background;

	protected GuiWidget(final int x, final int y, final int width, final int height) {
		this.initialX = x;
		this.initialY = y;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void initClient() {
	}

	public final void sendToServer(final int requestId, @Nullable final Consumer<ValueOutput> output) {
		if (this.getMenu().getPlayerInventory().player instanceof ServerPlayer) {
			throw new IllegalStateException("Already on server!");
		}
		this.widgetPacketHandler.sendPacket(this, requestId, output);
	}

	public final void sendToClient(final int requestId, @Nullable final Consumer<ValueOutput> output) {
		if (!(this.getMenu().getPlayerInventory().player instanceof ServerPlayer)) {
			throw new IllegalStateException("Already on client!");
		}
		this.widgetPacketHandler.sendPacket(this, requestId, output);
	}

	protected void handleClientRequest(final int requestId, final ValueInput input) {
	}

	protected void handleServerRequest(final int requestId, final ValueInput input) {
	}

	//region Rendering
	public void renderBackground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		if (this.background != null) {
			this.background.draw(guiGraphics, mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
	}

	public void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
	}

	public void renderTooltips(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
	}

	final void handleTooltipCallbacks(final GuiGraphics guiGraphics, final int mouseX, final int mouseY) {
		final List<ClientTooltipComponent> tooltip = new ArrayList<>();
		for (final TooltipCallback callback : this.tooltipCallbacks) {
			callback.onTooltip(tooltip);
		}
		if (!tooltip.isEmpty()) {
			guiGraphics.renderTooltip(this.getFont(), tooltip, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
		}
	}
	//endregion

	//region Events
	public final GuiWidget addMouseListener(final MouseEventListener listener) {
		this.mouseEventListeners.add(listener);
		return this;
	}

	public void onPositionChanged(final int newX, final int newY, final int oldX, final int oldY) {
	}

	public void onSizeChanged(final int newWidth, final int newHeight, final int oldWidth, final int oldHeight) {
	}

	final boolean notifyMouseEventListeners(final MouseEventListener.Event event, final int button, final int mouseX, final int mouseY) {
		for (final MouseEventListener listener : new ArrayList<>(this.mouseEventListeners)) {
			if (listener.onMouseEvent(this, event, button, mouseX, mouseY)) {
				return true;
			}
		}
		return false;
	}

	public boolean onMouseClicked(final int mouseX, final int mouseY, final int button) {
		return false;
	}

	public boolean onMouseReleased(final int mouseX, final int mouseY, final int button) {
		return false;
	}

	public final boolean containsMouse(final int mouseX, final int mouseY) {
		final int mx = mouseX - this.getScreen().getGuiLeft();
		final int my = mouseY - this.getScreen().getGuiTop();
		return mx >= this.getX() && mx <= this.getX() + this.getWidth() && my >= this.getY() && my <= this.getY() + this.getHeight();
	}
	//endregion

	//region Properties
	public final void setInitialX(final int initialX) {
		final int oldX = this.x;
		final int offset = this.x - this.initialX;
		this.initialX = initialX;
		this.x = this.initialX + offset;
		this.onPositionChanged(this.x, this.y, oldX, this.y);
	}

	public final void setInitialY(final int initialY) {
		final int oldY = this.y;
		final int offset = this.y - this.initialY;
		this.initialY = initialY;
		this.y = this.initialY + offset;
		this.onPositionChanged(this.x, this.y, this.x, oldY);
	}

	public final void setX(final int x) {
		final int oldX = this.x;
		this.x = x;
		this.onPositionChanged(x, this.y, oldX, this.y);
	}

	public final void setY(final int y) {
		final int oldY = this.y;
		this.y = y;
		this.onPositionChanged(this.x, this.y, this.x, oldY);
	}

	public final void setRelativeX(final int newX) {
		final int oldX = this.x;
		this.x = this.initialX + newX;
		this.onPositionChanged(this.x, this.y, oldX, this.y);
	}

	public final void setRelativeY(final int newY) {
		final int oldY = this.y;
		this.y = this.initialY + newY;
		this.onPositionChanged(this.x, this.y, this.x, oldY);
	}

	public final void setWidth(final int width) {
		final int oldWidth = this.width;
		this.width = width;
		this.onSizeChanged(this.width, this.height, oldWidth, this.height);
	}

	public final void setHeight(final int height) {
		final int oldHeight = this.height;
		this.height = height;
		this.onSizeChanged(this.width, this.height, this.width, oldHeight);
	}

	public final GuiWidget addTooltipCallback(final TooltipCallback listener) {
		this.tooltipCallbacks.add(listener);
		return this;
	}

	public final GuiWidget setBackground(@Nullable final GuiDrawable background) {
		this.background = background;
		return this;
	}

	public final MachineMenu getMenu() {
		return this.menu.get();
	}

	public final MachineScreen getScreen() {
		return this.screen.get();
	}

	public final GuiTheme getTheme() {
		return this.screen.get().getTheme();
	}

	public final Font getFont() {
		return this.getScreen().getFont();
	}
	//endregion
}
