package conductance.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import lombok.RequiredArgsConstructor;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import org.joml.Matrix3x2fStack;

@RequiredArgsConstructor
final class SlotTextOverlay implements IDrawableStatic {

	public static final SlotTextOverlay IN_CHANCE = new SlotTextOverlay(Component.translatable("info.conductance.jei.in.chance_overlay"));
	public static final SlotTextOverlay IN_CHANCE_0 = new SlotTextOverlay(Component.translatable("info.conductance.jei.in.chance_0_overlay"));
	public static final SlotTextOverlay OUT_CHANCE = new SlotTextOverlay(Component.translatable("info.conductance.jei.out.chance_overlay"));
	public static final SlotTextOverlay OUT_CHANCE_0 = new SlotTextOverlay(Component.translatable("info.conductance.jei.out.chance_0_overlay"));

	private final Component text;

	@Override
	public void draw(final GuiGraphics guiGraphics, final int xOffset, final int yOffset) {
		final Matrix3x2fStack pose = guiGraphics.pose().pushMatrix();

		pose.translate(xOffset + 16, yOffset);
		pose.scale(0.5f, 0.5f);

		final Font font = Minecraft.getInstance().font;
		guiGraphics.drawString(font, this.text, -font.width(this.text), 0, -1);

		pose.popMatrix();
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
