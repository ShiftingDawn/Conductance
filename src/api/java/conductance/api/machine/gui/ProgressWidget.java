package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class ProgressWidget extends GuiWidget {

	private final GuiDrawable overlay;
	private final ProgressProvider provider;
	private final ProgressProvider.Direction direction;

	public ProgressWidget(
		final int x, final int y, final int width, final int height, final GuiDrawable background, final GuiDrawable overlay, final ProgressProvider provider, final ProgressProvider.Direction direction
	) {
		super(x, y, width, height);
		this.setBackground(background);
		this.overlay = overlay;
		this.provider = provider;
		this.direction = direction;
	}

	public ProgressWidget(final int x, final int y, final int width, final int height, final GuiDrawableTexture drawable, final ProgressProvider provider, final ProgressProvider.Direction direction) {
		this(
			x, y, width, height,
			drawable.getSubTexture(0, 0, drawable.getImageWidth(), drawable.getImageHeight() / 2),
			drawable.getSubTexture(0, drawable.getImageHeight() / 2, drawable.getImageWidth(), drawable.getImageHeight() / 2),
			provider, direction
		);
	}

	@Override
	public void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.renderForeground(guiGraphics, mouseX, mouseY, partialTick);
		if (this.provider.getCurrentProgress() == 0 || this.provider.getMaxProgress() == 0) {
			return;
		}
		if (this.provider.getCurrentProgress() == this.provider.getMaxProgress()) {
			this.overlay.draw(guiGraphics, mouseX, mouseY, this.getBounds());
			return;
		}
		final float factor = (float) this.provider.getCurrentProgress() / (float) this.provider.getMaxProgress();
		final int textureWidth = this.overlay instanceof final GuiDrawableTexture drawableTexture ? drawableTexture.getTextureWidth() : this.getWidth();
		final int textureHeight = this.overlay instanceof final GuiDrawableTexture drawableTexture ? drawableTexture.getTextureHeight() : this.getHeight();
		if (this.direction == ProgressProvider.Direction.LEFT_TO_RIGHT || this.direction == ProgressProvider.Direction.RIGHT_TO_LEFT) {
			final int width = Mth.ceil(this.getSize().width() * factor);
			final int uLength = (int) ((float) textureWidth / (float) this.getSize().width() * (float) width);
			if (this.direction == ProgressProvider.Direction.LEFT_TO_RIGHT) {
				this.overlay.draw(
					guiGraphics, mouseX, mouseY,
					this.getPosition().x(), this.getY(), width, this.getHeight(),
					0, 0, uLength, textureHeight
				);
			} else {
				this.overlay.draw(
					guiGraphics, mouseX, mouseY,
					this.getX() + this.getWidth() - width, this.getY(), width, this.getHeight(),
					textureWidth - uLength, 0, uLength, textureHeight
				);
			}
		} else {
			final int height = Mth.ceil(this.getHeight() * factor);
			final int vLength = (int) ((float) textureHeight / (float) this.getHeight() * (float) height);
			if (this.direction == ProgressProvider.Direction.UP_TO_DOWN) {
				this.overlay.draw(
					guiGraphics, mouseX, mouseY,
					this.getX(), this.getY(), this.getWidth(), height,
					0, 0, textureWidth, vLength
				);
			} else {
				this.overlay.draw(
					guiGraphics, mouseX, mouseY,
					this.getX(), this.getY() + this.getHeight() - height, this.getWidth(), height,
					0, textureHeight - vLength, textureWidth, vLength
				);
			}
		}
	}
}
