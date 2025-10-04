package conductance.api.material;

import java.util.Arrays;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.Level;
import lombok.Getter;

@Getter
public final class MaterialColor {

	private final int[] colors;
	private final int[] frames;

	public MaterialColor(final int[] colors, final int frametime) {
		this.colors = Util.make(new int[colors.length], arr -> {
			for (int i = 0; i < arr.length; ++i) {
				arr[i] = ARGB.opaque(colors[i]);
			}
		});
		this.frames = new int[colors.length * frametime];
		if (colors.length == 1) {
			Arrays.fill(this.frames, ARGB.opaque(colors[0]));
		} else {
			this.frames[0] = ARGB.opaque(colors[0]);
			for (int colorIndex = 0; colorIndex < this.colors.length; ++colorIndex) {
				final int colorA = this.colors[colorIndex];
				final int colorB = colorIndex + 1 == colors.length ? this.colors[0] : this.colors[colorIndex + 1];
				final int r1 = ARGB.red(colorA);
				final int g1 = ARGB.green(colorA);
				final int b1 = ARGB.blue(colorA);
				final int r2 = ARGB.red(colorB);
				final int g2 = ARGB.green(colorB);
				final int b2 = ARGB.blue(colorB);
				final float deltaR = (float) (r2 - r1) / (float) frametime;
				final float deltaG = (float) (g2 - g1) / (float) frametime;
				final float deltaB = (float) (b2 - b1) / (float) frametime;
				for (int frame = 0; frame < frametime; ++frame) {
					final int r = r1 + (int) (deltaR * frame);
					final int g = g1 + (int) (deltaG * frame);
					final int b = b1 + (int) (deltaB * frame);
					this.frames[colorIndex * frametime + frame] = ARGB.color(255, r, g, b);
				}
			}
		}
	}

	public boolean hasMultipleColors() {
		return this.colors.length > 1;
	}

	public int getCurrentColor() {
		if (this.colors.length == 1) {
			return this.colors[0];
		}
		final Level level = Minecraft.getInstance().level;
		if (level == null) {
			return this.colors[0];
		}
		final int currentFrame = (int) (level.getGameTime() % this.frames.length);
		return this.frames[currentFrame];
	}
}
