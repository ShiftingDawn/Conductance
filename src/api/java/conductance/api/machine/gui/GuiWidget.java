package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public abstract class GuiWidget implements Renderable {

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

	public void init(final MachineScreen screen) {
	}

	public void renderBackground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		if (this.background != null) {
			this.background.draw(guiGraphics, mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
	}

	public void setInitialX(final int initialX) {
		final int offset = this.x - this.initialX;
		this.initialX = initialX;
		this.x = this.initialX + offset;
	}

	public void setInitialY(final int initialY) {
		final int offset = this.y - this.initialY;
		this.initialY = initialY;
		this.y = this.initialY + offset;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public void setRelativeX(final int newX) {
		this.x = this.initialX + newX;
	}

	public void setRelativeY(final int newY) {
		this.y = this.initialY + newY;
	}

	public final void setWidth(final int width) {
		this.width = width;
	}

	public final void setHeight(final int height) {
		this.height = height;
	}

	public final GuiWidget setBackground(@Nullable final GuiDrawable background) {
		this.background = background;
		return this;
	}
}
