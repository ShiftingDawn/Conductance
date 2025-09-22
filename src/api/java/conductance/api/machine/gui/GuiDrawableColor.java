package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GuiDrawableColor implements GuiDrawable {

	private final int argb;

	@Override
	public void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final int x, final int y, final int width, final int height) {
		graphics.fill(x, y, x + width, y + height, this.argb);
	}
}
