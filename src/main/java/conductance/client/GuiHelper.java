package conductance.client;

import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Inventory;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.gui.GuiTextures;

public final class GuiHelper {

	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 186;

	public static final String NAME_PROGRESS = "progress";
	public static final String NAME_PROGRESS_REGEX = "^progress$";
	public static final String NAME_SLOT_REGEX = "^%s_[0-9]+$";
	public static final String NAME_GROUP_REGEX = "^group_%s_[0-9]+$";

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

	public static WidgetGroup createPlayerInventory(final Inventory player) {
		return Util.make(new WidgetGroup(0, 0, 9 * 18, 4 * 18 + 4), group -> {
			group.addWidget(Util.make(new WidgetGroup(0, 0, group.getSizeWidth(), 3 * 18), container -> {
				for (int row = 0; row < 3; ++row) {
					for (int col = 0; col < 9; ++col) {
						container.addWidget(new SlotWidget(player, col + (row + 1) * 9, col * 18, row * 18)
								.setBackgroundTexture(null)
								.setLocationInfo(true, false));
					}
				}
				container.setBackground(GuiTextures.PLAYER_INVENTORY);
			}));
			group.addWidget(Util.make(new WidgetGroup(0, group.getSizeHeight() - 18, group.getSizeWidth(), 18), container -> {
				for (int i = 0; i < 9; ++i) {
					container.addWidget(new SlotWidget(player, i, i * 18, 0)
							.setBackgroundTexture(null)
							.setLocationInfo(true, true));
				}
				container.setBackground(GuiTextures.PLAYER_HOTBAR);
			}));
		});
	}

	private GuiHelper() {
	}
}
