package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GuiDrawableColor implements GuiDrawable {

	private final int argb;

	@Override
	public void draw(
		final GuiGraphics graphics, final int mouseX, final int mouseY, final int x, final int y, final int width, final int height, final float uStart, final float vStart, final float uLength, final float vLength
	) {
		graphics.fill(x, y, x + width, y + height, this.argb);
	}

	@Override
	public void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final int x, final int y, final int width, final int height) {
		this.draw(graphics, mouseX, mouseY, x, y, width, height, 0, 0, 0, 0);
	}

	@Override
	public GuiDrawable getSubTexture(final int x, final int y, final int width, final int height) {
		return new GuiDrawableColor(this.argb);
	}
}
