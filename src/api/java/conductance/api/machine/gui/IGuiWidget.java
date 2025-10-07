package conductance.api.machine.gui;

import java.util.function.Consumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public interface IGuiWidget {

	default void initClient() {
	}

	//region Data
	void sendToServer(int requestId, @Nullable Consumer<ValueOutput> output);

	void sendToClient(int requestId, @Nullable Consumer<ValueOutput> output);

	default void handleClientRequest(final int requestId, final ValueInput input) {
	}

	default void handleServerRequest(final int requestId, final ValueInput input) {
	}
	//endregion

	//region Rendering
	default void renderBackground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
	}

	default void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
	}

	default void renderTooltips(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
	}

	void setBackground(@Nullable GuiDrawable background);

	void addTooltipCallback(TooltipCallback listener);
	//endregion

	//region Event
	void addMouseListener(MouseEventListener listener);

	default void onPositionChanged(final int newX, final int newY, final int oldX, final int oldY) {
	}

	default void onSizeChanged(final int newWidth, final int newHeight, final int oldWidth, final int oldHeight) {
	}

	default boolean onMouseClicked(final int mouseX, final int mouseY, final int button) {
		return false;
	}

	default boolean onMouseReleased(final int mouseX, final int mouseY, final int button) {
		return false;
	}

	default boolean containsMouse(final int mouseX, final int mouseY) {
		return this.getBounds().contains(mouseX - this.getScreen().getGuiLeft(), mouseY - this.getScreen().getGuiTop());
	}
	//endregion

	//region Properties
	void setBounds(Rectangle newBounds);

	void setX(ManagedInt newX);

	void setX(int newX);

	void setY(ManagedInt newY);

	void setY(int newY);

	void setPosition(Point newPosition);

	void setWidth(int width);

	void setHeight(int height);

	void setSize(Size newSize);

	Rectangle getBounds();

	default Point getPosition() {
		return this.getBounds();
	}

	default Size getSize() {
		return this.getBounds();
	}

	default int getX() {
		return this.getPosition().x();
	}

	default int getY() {
		return this.getPosition().y();
	}

	default int getWidth() {
		return this.getSize().width();
	}

	default int getHeight() {
		return this.getSize().height();
	}

	MachineMenu getMenu();

	MachineScreen getScreen();

	default GuiTheme getTheme() {
		return this.getScreen().getTheme();
	}

	default Font getFont() {
		return this.getScreen().getFont();
	}
	//endregion

	//region Internal
	MutableRectangle internalGetBounds();

	void internalBindToParentWidget(WidgetBindingInfo info);

	void internalHandleTooltipCallbacks(GuiGraphics guiGraphics, int mouseX, int mouseY);

	boolean internalNotifyMouseEventListeners(MouseEventListener.Event event, int button, int mouseX, int mouseY);
	//endregion
}
