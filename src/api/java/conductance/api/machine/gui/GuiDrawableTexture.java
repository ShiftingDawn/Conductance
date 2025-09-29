package conductance.api.machine.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;

public class GuiDrawableTexture implements GuiDrawable {

	public static final ResourceLocation ATLAS = ResourceLocation.withDefaultNamespace("textures/atlas/gui.png");
	private final ResourceLocation texture;
	private final boolean allowSubbing;
	private final @Getter int offsetX;
	private final @Getter int offsetY;
	private final @Getter int textureWidth;
	private final @Getter int textureHeight;
	private final @Getter int imageWidth;
	private final @Getter int imageHeight;

	public GuiDrawableTexture(final ResourceLocation texture, final int offsetX, final int offsetY, final int textureWidth, final int textureHeight, final int imageWidth, final int imageHeight) {
		this.texture = texture;
		final TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(this.texture);
		this.allowSubbing = Minecraft.getInstance().getGuiSprites().getSpriteScaling(sprite) instanceof GuiSpriteScaling.Stretch;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	public GuiDrawableTexture(final ResourceLocation texture) {
		this(
			texture,
			0, 0,
			Minecraft.getInstance().getGuiSprites().getSprite(texture).contents().width(), Minecraft.getInstance().getGuiSprites().getSprite(texture).contents().height(),
			Minecraft.getInstance().getGuiSprites().getSprite(texture).contents().width(), Minecraft.getInstance().getGuiSprites().getSprite(texture).contents().height()
		);
	}

	@Override
	public void draw(
		final GuiGraphics graphics, final int mouseX, final int mouseY, final int x, final int y, final int width, final int height, final float uStart, final float vStart, final float uLength, final float vLength
	) {
		if (this.allowSubbing) {
			final TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(this.texture);
			final float u0 = sprite.getU((this.offsetX + uStart) / (float) this.imageWidth);
			final float u1 = sprite.getU((this.offsetX + uStart + uLength) / (float) this.imageWidth);
			final float v0 = sprite.getV((this.offsetY + vStart) / (float) this.imageHeight);
			final float v1 = sprite.getV((this.offsetY + vStart + vLength) / (float) this.imageHeight);
			graphics.blit(GuiDrawableTexture.ATLAS, x, y, x + width, y + height, u0, u1, v0, v1);
		} else {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.texture, x, y, width, height);
		}
	}

	@Override
	public void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final int x, final int y, final int width, final int height) {
		this.draw(graphics, mouseX, mouseY, x, y, width, height, 0, 0, this.textureWidth, this.textureHeight);
	}

	@Override
	public GuiDrawableTexture getSubTexture(final int subX, final int subY, final int subWidth, final int subHeight) {
		if (!this.allowSubbing) {
			return this;
		}
		return new GuiDrawableTexture(this.texture, subX, subY, subWidth, subHeight, this.imageWidth, this.imageHeight);
	}
}
