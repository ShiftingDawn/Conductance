package conductance.api.util;

import java.util.List;
import java.util.Objects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.platform.InputConstants;

public final class GuiUtils {

	public static void tooltip(final List<ClientTooltipComponent> tooltip, final Component text) {
		tooltip.add(ClientTooltipComponent.create(text.getVisualOrderText()));
	}

	public static void tooltipLiteral(final List<ClientTooltipComponent> tooltip, final Object text) {
		GuiUtils.tooltip(tooltip, Component.literal(Objects.toString(text)));
	}

	public static void tooltipTranslatable(final List<ClientTooltipComponent> tooltip, final String key, final Object... format) {
		GuiUtils.tooltip(tooltip, Component.translatable(key, format));
	}

	public static void tooltipMoreInfo(final List<ClientTooltipComponent> tooltip, final Runnable callback) {
		if (GuiUtils.isShiftDown()) {
			callback.run();
		} else {
			GuiUtils.tooltipTranslatable(tooltip, "info.conductance.generic.hold_shift", Minecraft.getInstance().options.keyShift.getKey().getDisplayName());
		}
	}

	public static boolean isShiftDown() {
		final KeyMapping key = Minecraft.getInstance().options.keyShift;
		return key.isDown() || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue());
	}

	private GuiUtils() {
	}
}
