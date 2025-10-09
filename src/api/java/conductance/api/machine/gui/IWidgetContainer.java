package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;

public interface IWidgetContainer extends IGuiWidget, WidgetHolder {

	@Override
	default void initClient() {
		for (final IGuiWidget child : this.getWidgets().values()) {
			child.initClient();
		}
	}

	//region Rendering
	@Override
	default void renderBackground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		IGuiWidget.super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		for (final IGuiWidget child : this.getWidgets().values()) {
			child.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	@Override
	default void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		for (final IGuiWidget child : this.getWidgets().values()) {
			child.renderForeground(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	@Override
	default void renderTooltips(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		for (final IGuiWidget child : this.getWidgets().values()) {
			child.renderTooltips(guiGraphics, mouseX, mouseY, partialTick);
			if (child.containsMouse(mouseX, mouseY)) {
				child.internalHandleTooltipCallbacks(guiGraphics, mouseX, mouseY);
			}
		}
	}
	//endregion

	//region Event
	@Override
	default boolean onMouseClicked(final int mouseX, final int mouseY, final int button) {
		return this.internalHandleMouseEvent(MouseEventListener.Event.PRESS, mouseX, mouseY, button);
	}

	@Override
	default boolean onMouseReleased(final int mouseX, final int mouseY, final int button) {
		return this.internalHandleMouseEvent(MouseEventListener.Event.RELEASE, mouseX, mouseY, button);
	}

	@Override
	default boolean onMouseScrolled(final int mouseX, final int mouseY, final double deltaX, final double deltaY) {
		//Widgets are positioned using absolute coordinates, so untranslate mouse coords
		final int absoluteMouseX = this.getPosition().x() + mouseX;
		final int absoluteMouseY = this.getPosition().y() + mouseY;
		for (final IGuiWidget widget : this.getWidgets().values()) {
			if (!widget.getBounds().contains(absoluteMouseX, absoluteMouseY)) {
				continue;
			}
			final int mx = absoluteMouseX - widget.getBounds().x();
			final int my = absoluteMouseY - widget.getBounds().y();
			if (widget.onMouseScrolled(mx, my, deltaX, deltaY)) {
				return true;
			}
		}
		return false;
	}

	@Override
	default boolean onMouseDragged(final int mouseX, final int mouseY, final int button, final double dragX, final double dragY) {
		//Widgets are positioned using absolute coordinates, so untranslate mouse coords
		final int absoluteMouseX = this.getPosition().x() + mouseX;
		final int absoluteMouseY = this.getPosition().y() + mouseY;
		for (final IGuiWidget widget : this.getWidgets().values()) {
			if (!widget.getBounds().contains(absoluteMouseX, absoluteMouseY)) {
				continue;
			}
			final int mx = absoluteMouseX - widget.getBounds().x();
			final int my = absoluteMouseY - widget.getBounds().y();
			if (widget.onMouseDragged(mx, my, button, dragX, dragY)) {
				return true;
			}
		}
		return IGuiWidget.super.onMouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	//endregion

	//region Internal
	default boolean internalHandleMouseEvent(final MouseEventListener.Event event, final int mouseX, final int mouseY, final int button) {
		//Widgets are positioned using absolute coordinates, so untranslate mouse coords
		final int absoluteMouseX = this.getPosition().x() + mouseX;
		final int absoluteMouseY = this.getPosition().y() + mouseY;
		for (final IGuiWidget widget : this.getWidgets().values()) {
			if (!widget.getBounds().contains(absoluteMouseX, absoluteMouseY)) {
				continue;
			}
			final int mx = absoluteMouseX - widget.getBounds().x();
			final int my = absoluteMouseY - widget.getBounds().y();
			switch (event) {
				case PRESS -> {
					if (widget.onMouseClicked(mx, my, button)) {
						return true;
					}
					if (widget.internalNotifyMouseEventListeners(MouseEventListener.Event.PRESS, button, mx, my)) {
						return true;
					}
				}
				case RELEASE -> {
					if (widget.onMouseReleased(mx, my, button)) {
						return true;
					}
					if (widget.internalNotifyMouseEventListeners(MouseEventListener.Event.RELEASE, button, mx, my)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	//endregion
}
