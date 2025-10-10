package conductance.api.machine.gui;

import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.util.GuiUtils;
import conductance.api.util.TextHelper;

public class EnergyBarWidget extends ProgressWidget {

	public EnergyBarWidget(final int x, final int y, final GuiTheme theme, final IEnergyHandler handler) {
		super(x, y, 162, 6, theme.getEnergyBar(), theme.getEnergyBarOverlay(), new EnergyHandlerProgressProvider(handler), ProgressProvider.Direction.LEFT_TO_RIGHT);
		this.addTooltipCallback(tooltip -> GuiUtils.tooltipTranslatable(tooltip,
			"guiWidget.conductance.energy_bar.energy_state",
			TextHelper.getFormattedEnergy(handler.getEnergyStored()), TextHelper.getFormattedEnergy(handler.getEnergyCapacity())
		));
	}
}
