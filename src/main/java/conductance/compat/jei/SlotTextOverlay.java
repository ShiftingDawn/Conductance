package conductance.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import lombok.RequiredArgsConstructor;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

@RequiredArgsConstructor
final class SlotTextOverlay implements IDrawableStatic {

	public static final Component IN_CHANCE = Component.translatable("info.conductance.jei.in.chance_overlay");
	public static final Component IN_CHANCE_0 = Component.translatable("info.conductance.jei.in.chance_0_overlay");
	public static final Component OUT_CHANCE = Component.translatable("info.conductance.jei.out.chance_overlay");
	public static final Component OUT_CHANCE_0 = Component.translatable("info.conductance.jei.out.chance_0_overlay");

	private final @Nullable Component topText;
	private final @Nullable Component bottomText;

	@Override
	public void draw(final GuiGraphics guiGraphics, final int xOffset, final int yOffset) {
		final Font font = Minecraft.getInstance().font;

		if (this.topText != null) {
			guiGraphics.pose().pushMatrix();
			guiGraphics.pose().translate(xOffset + 16, yOffset);
			guiGraphics.pose().scale(0.5f, 0.5f);
			guiGraphics.drawString(font, this.topText, -font.width(this.topText), 0, -1);
			guiGraphics.pose().popMatrix();
		}
		if (this.bottomText != null) {
			final float scale = Math.min((float) (this.getWidth() - 2) / (float) font.width(this.bottomText), 0.7f);
			guiGraphics.pose().pushMatrix();
			guiGraphics.pose().translate(xOffset + this.getWidth() - font.width(this.bottomText) * scale, yOffset + this.getHeight() - font.lineHeight * scale);
			guiGraphics.pose().scale(scale, scale);
			guiGraphics.drawString(font, this.bottomText, -2, -2, -1, true);
			guiGraphics.pose().popMatrix();
		}
	}

	@Override
	public void draw(final GuiGraphics guiGraphics, final int xOffset, final int yOffset, final int maskTop, final int maskBottom, final int maskLeft, final int maskRight) {
		this.draw(guiGraphics, xOffset, yOffset);
	}

	@Override
	public int getWidth() {
		return 18;
	}

	@Override
	public int getHeight() {
		return 18;
	}
}
