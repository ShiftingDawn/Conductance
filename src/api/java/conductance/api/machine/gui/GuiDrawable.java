package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;

public interface GuiDrawable {

	void draw(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height);

}
