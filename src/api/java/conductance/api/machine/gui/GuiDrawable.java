package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;

public interface GuiDrawable {

	void draw(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float uStart, float vStart, float uLength, float vLength);

	default void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final Rectangle bounds, final float uStart, final float vStart, final float uLength, final float vLength) {
		this.draw(graphics, mouseX, mouseY, bounds.x(), bounds.y(), bounds.width(), bounds.height(), uStart, vStart, uLength, vLength);
	}

	default void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final Point position, final Size size, final float uStart, final float vStart, final float uLength, final float vLength) {
		this.draw(graphics, mouseX, mouseY, position.x(), position.y(), size.width(), size.height(), uStart, vStart, uLength, vLength);
	}

	void draw(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height);

	default void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final Rectangle bounds) {
		this.draw(graphics, mouseX, mouseY, bounds.x(), bounds.y(), bounds.width(), bounds.height());
	}

	default void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final Point position, final Size size) {
		this.draw(graphics, mouseX, mouseY, position.x(), position.y(), size.width(), size.height());
	}

	GuiDrawable getSubTexture(int x, int y, int width, int height);

}
