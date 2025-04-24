package conductance.client;

import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import com.mojang.blaze3d.systems.RenderSystem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import conductance.api.machine.gui.GuiTheme;
import conductance.Conductance;

public final class GuiHelper {

	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 186;

	public static final String NAME_PROGRESS = "progress";
	public static final String NAME_PROGRESS_REGEX = "^progress$";
	public static final String NAME_SLOT_REGEX = "^%s_[0-9]+$";

	public static List<Widget> getWidgetsById(final WidgetGroup group, final String regex) {
		return group.getWidgetsById(Pattern.compile(regex));
	}

	public static <T extends Widget> void getWidgetByIdForEach(final WidgetGroup group, final String regex, final Class<T> clazz, final Consumer<T> consumer) {
		for (final Widget widget : GuiHelper.getWidgetsById(group, regex)) {
			if (clazz.isInstance(widget)) {
				consumer.accept(clazz.cast(widget));
			}
		}
	}

	public static int getWidgetIndex(final Widget widget) {
		try {
			final String id = widget.getId();
			final String[] parts = id.split("_");
			return Integer.parseInt(parts[parts.length - 1]);
		} catch (final Exception ignored) {
		}
		return -1;
	}

	public static WidgetGroup createPlayerInventory(final Inventory player, final GuiTheme theme) {
		return Util.make(new WidgetGroup(0, 0, 9 * 18, 4 * 18 + 4), group -> {
			group.addWidget(Util.make(new WidgetGroup(0, 0, group.getSizeWidth(), 3 * 18), container -> {
				for (int row = 0; row < 3; ++row) {
					for (int col = 0; col < 9; ++col) {
						container.addWidget(new SlotWidget(player, col + (row + 1) * 9, col * 18, row * 18)
								.setBackgroundTexture(null)
								.setLocationInfo(true, false)
								.setDrawHoverOverlay(false)
								.setHoverTexture(theme.getSlotHover()));
					}
				}
				container.setBackground(theme.getPlayerInventory());
			}));
			group.addWidget(Util.make(new WidgetGroup(0, group.getSizeHeight() - 18, group.getSizeWidth(), 18), container -> {
				for (int i = 0; i < 9; ++i) {
					container.addWidget(new SlotWidget(player, i, i * 18, 0)
							.setBackgroundTexture(null)
							.setLocationInfo(true, true)
							.setDrawHoverOverlay(false)
							.setHoverTexture(theme.getSlotHover()));
				}
				container.setBackground(theme.getPlayerHotbar());
			}));
		});
	}

	public static IGuiTexture createRecipeSlotOverlay(final boolean perTick, final int chance) {
		return new IGuiTexture() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public void draw(final GuiGraphics graphics, final int mouseX, final int mouseY, final float x, final float y, final int width, final int height) {
				GuiHelper.drawRecipeSlotOverlayChance(graphics, x, y, width, height, chance);
				if (perTick) {
					GuiHelper.drawRecipeSlotOverlayPerTick(graphics, x, y, width, height, chance);
				}
			}
		};
	}

	@OnlyIn(Dist.CLIENT)
	private static void drawRecipeSlotOverlayChance(final GuiGraphics graphics, final float x, final float y, final int width, final int height, final float chance) {
		if (chance == 100) {
			return;
		}
		graphics.pose().pushPose();
		graphics.pose().translate(0, 0, 400);
		graphics.pose().scale(0.5f, 0.5f, 1);
		final String text = chance == 0 ? LocalizationUtils.format(Conductance.tooltipText("recipe.overlay_chance_0")) : String.format("%.1f", chance * 100) + "%";
		final int color = chance == 0 ? 0xff0000 : 0xFFFF00;
		final Font fontRenderer = Minecraft.getInstance().font;
		graphics.drawString(fontRenderer, text, (int) ((x + (width / 3f)) * 2 - fontRenderer.width(text) + 23), (int) ((y + (height / 3f) + 6) * 2 - height), color, true);
		graphics.pose().popPose();
	}

	@OnlyIn(Dist.CLIENT)
	private static void drawRecipeSlotOverlayPerTick(final GuiGraphics graphics, final float x, final float y, final int width, final int height, final float chance) {
		graphics.pose().pushPose();
		RenderSystem.disableDepthTest();
		graphics.pose().translate(0, 0, 400);
		graphics.pose().scale(0.5f, 0.5f, 1);
		final String text = LocalizationUtils.format(Conductance.tooltipText("recipe.overlay_per_tick"));
		final int color = 0xFFFF00;
		final Font fontRenderer = Minecraft.getInstance().font;
		graphics.drawString(fontRenderer, text, (int) ((x + (width / 3f)) * 2 - fontRenderer.width(text) + 23), (int) ((y + (height / 3f) + 6) * 2 - height + (chance == 1 ? 0 : 10)), color);
		graphics.pose().popPose();
	}

	private GuiHelper() {
	}
}
