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
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public abstract class GuiWidget {

	private final List<MouseEventListener> mouseEventListeners = new ArrayList<>();
	private final List<TooltipCallback> tooltipCallbacks = new ArrayList<>();
	private final MutableRectangle bounds;
	@Setter(AccessLevel.PACKAGE)
	private @Nullable Rectangle parentBounds;
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineMenu> menu;
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineScreen> screen;
	@Setter(AccessLevel.PACKAGE)
	private WidgetPacketHandler widgetPacketHandler;
	private ManagedInt offsetX;
	private ManagedInt offsetY;
	private boolean useAbsolutePositioning = false;
	private @Nullable GuiDrawable background;

	protected GuiWidget(final int x, final int y, final int width, final int height) {
		this.offsetX = new ManagedInt(x);
		this.offsetY = new ManagedInt(y);
		final ManagedInt managedX = new ManagedInt(this.offsetX, () -> this.parentBounds == null || this.useAbsolutePositioning ? this.offsetX.getAsInt() : this.parentBounds.x() + this.offsetX.getAsInt());
		final ManagedInt managedY = new ManagedInt(this.offsetY, () -> this.parentBounds == null || this.useAbsolutePositioning ? this.offsetY.getAsInt() : this.parentBounds.y() + this.offsetY.getAsInt());
		this.bounds = MutableRectangle.of(MutablePoint.of(managedX, managedY), Size.of(width, height));
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
			this.background.draw(guiGraphics, mouseX, mouseY, this.getBounds());
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
		return this.bounds.contains(mouseX - this.getScreen().getGuiLeft(), mouseY - this.getScreen().getGuiTop());
	}
	//endregion

	//region Properties
	public final void setBounds(final Rectangle newBounds) {
		final int oldX = this.getX();
		final int oldY = this.getY();
		final int oldWidth = this.getWidth();
		final int oldHeight = this.getHeight();
		if (newBounds instanceof final MutableRectangle mut) {
			this.bounds.position(mut.position());
			this.bounds.size(mut.size());
		} else {
			this.offsetX.accept(newBounds.x());
			this.offsetY.accept(newBounds.y());
			this.bounds.width(newBounds.width());
			this.bounds.height(newBounds.height());
		}
		this.onPositionChanged(this.getX(), this.getY(), oldX, oldY);
		this.onSizeChanged(this.getWidth(), this.getHeight(), oldWidth, oldHeight);
	}

	public final void setX(final ManagedInt newX) {
		this.offsetX = newX;
	}

	public final void setX(final int newX) {
		final int oldX = this.getX();
		this.offsetX.accept(newX);
		this.onPositionChanged(this.getX(), this.getY(), oldX, this.getY());
	}

	public final void setY(final ManagedInt newY) {
		this.offsetY = newY;
	}

	public final void setY(final int newY) {
		final int oldY = this.getY();
		this.offsetY.accept(newY);
		this.onPositionChanged(this.getX(), this.getY(), this.getX(), oldY);
	}

	public final void setPosition(final Point newPosition) {
		final int oldX = this.getX();
		final int oldY = this.getY();
		if (newPosition instanceof final MutablePoint mut) {
			this.offsetX = mut.holderX();
			this.offsetY = mut.holderY();
			this.useAbsolutePositioning = true;
		} else {
			if (this.useAbsolutePositioning) {
				this.offsetX = new ManagedInt(newPosition.x());
				this.offsetY = new ManagedInt(newPosition.y());
				this.useAbsolutePositioning = false;
			} else {
				this.offsetX.accept(newPosition.x());
				this.offsetY.accept(newPosition.y());
			}
		}
		this.onPositionChanged(this.getX(), this.getY(), oldX, oldY);
	}

	public final void setWidth(final int width) {
		final int oldWidth = this.bounds.width(width);
		this.onSizeChanged(this.getWidth(), this.getHeight(), oldWidth, this.getHeight());
	}

	public final void setHeight(final int height) {
		final int oldHeight = this.bounds.height(height);
		this.onSizeChanged(this.getWidth(), this.getHeight(), this.getWidth(), oldHeight);
	}

	public final void setSize(final Size newSize) {
		final int oldWidth = this.getWidth();
		final int oldHeight = this.getHeight();
		if (newSize instanceof final MutableSize mut) {
			this.bounds.size(mut);
		} else {
			this.bounds.width(newSize.width());
			this.bounds.height(newSize.height());
		}
		this.onSizeChanged(this.getWidth(), this.getHeight(), oldWidth, oldHeight);
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

	public final MutableRectangle getBoundsUnsafe() {
		return this.bounds;
	}

	public final Rectangle getBounds() {
		return this.bounds;
	}

	public final Point getPosition() {
		return this.getBounds();
	}

	public final Size getSize() {
		return this.getBounds();
	}

	public final int getX() {
		return this.getPosition().x();
	}

	public final int getY() {
		return this.getPosition().y();
	}

	public final int getWidth() {
		return this.getSize().width();
	}

	public final int getHeight() {
		return this.getSize().height();
	}

	//endregion
}
