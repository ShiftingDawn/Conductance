package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GuiDrawableTexture implements GuiDrawable {

	private final ResourceLocation texture;

	@Override
	public void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final int x, final int y, final int width, final int height) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.texture, x, y, width, height);
	}
}
