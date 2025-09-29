package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;

public interface GuiDrawable {

	void draw(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float uStart, float vStart, float uLength, float vLength);

	void draw(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height);

	GuiDrawable getSubTexture(int x, int y, int width, int height);

}
