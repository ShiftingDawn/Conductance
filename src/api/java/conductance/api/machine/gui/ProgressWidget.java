package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class ProgressWidget extends GuiWidget {

	private final GuiDrawableTexture drawable;
	private final ProgressProvider provider;
	private final ProgressProvider.Direction direction;

	public ProgressWidget(final GuiDrawableTexture drawable, final ProgressProvider provider, final ProgressProvider.Direction direction, final int x, final int y, final int width, final int height) {
		super(x, y, width, height);
		this.setBackground(drawable.getSubTexture(0, 0, drawable.getImageWidth(), drawable.getImageHeight() / 2));
		this.drawable = drawable.getSubTexture(0, drawable.getImageHeight() / 2, drawable.getImageWidth(), drawable.getImageHeight() / 2);
		this.provider = provider;
		this.direction = direction;
	}

	@Override
	public void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.renderForeground(guiGraphics, mouseX, mouseY, partialTick);
		if (this.provider.getCurrentProgress() == 0 || this.provider.getMaxProgress() == 0) {
			return;
		}
		if (this.provider.getCurrentProgress() == this.provider.getMaxProgress()) {
			this.drawable.draw(guiGraphics, mouseX, mouseY, this.getBounds());
			return;
		}
		final float factor = (float) this.provider.getCurrentProgress() / (float) this.provider.getMaxProgress();
		if (this.direction == ProgressProvider.Direction.LEFT_TO_RIGHT || this.direction == ProgressProvider.Direction.RIGHT_TO_LEFT) {
			final int width = Mth.ceil(this.getSize().width() * factor);
			final int uLength = (int) ((float) this.drawable.getTextureWidth() / (float) this.getSize().width() * (float) width);
			if (this.direction == ProgressProvider.Direction.LEFT_TO_RIGHT) {
				this.drawable.draw(
					guiGraphics, mouseX, mouseY,
					this.getPosition().x(), this.getY(), width, this.getHeight(),
					0, 0, uLength, this.drawable.getTextureHeight()
				);
			} else {
				this.drawable.draw(
					guiGraphics, mouseX, mouseY,
					this.getX() + this.getWidth() - width, this.getY(), width, this.getHeight(),
					this.drawable.getTextureWidth() - uLength, 0, uLength, this.drawable.getTextureHeight()
				);
			}
		} else {
			final int height = Mth.ceil(this.getHeight() * factor);
			final int vLength = (int) ((float) this.drawable.getTextureHeight() / (float) this.getHeight() * (float) height);
			if (this.direction == ProgressProvider.Direction.UP_TO_DOWN) {
				this.drawable.draw(
					guiGraphics, mouseX, mouseY,
					this.getX(), this.getY(), this.getWidth(), height,
					0, 0, this.drawable.getTextureWidth(), vLength
				);
			} else {
				this.drawable.draw(
					guiGraphics, mouseX, mouseY,
					this.getX(), this.getY() + this.getHeight() - height, this.getWidth(), height,
					0, this.drawable.getTextureHeight() - vLength, this.drawable.getTextureWidth(), vLength
				);
			}
		}
	}
}
