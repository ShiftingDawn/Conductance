package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public abstract class GuiWidget implements Renderable {

	private final @Getter int initialX;
	private final @Getter int initialY;
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

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		if (this.background != null) {
			this.background.draw(guiGraphics, mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
	}

	public final void setX(final int x) {
		this.x = x;
	}

	public final void setY(final int y) {
		this.y = y;
	}

	public final void setRelativeX(final int newX) {
		this.x = this.initialX + newX;
	}

	public final void setRelativeY(final int newY) {
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
