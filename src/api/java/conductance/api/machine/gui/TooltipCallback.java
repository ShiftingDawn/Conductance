package conductance.api.machine.gui;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public interface TooltipCallback {

	void onTooltip(IGuiWidget widget, List<ClientTooltipComponent> tooltip);
}
