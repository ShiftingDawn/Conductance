package conductance.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineRecipeCapabilityItems;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.Synchronized;
import conductance.api.util.IOMode;

public final class SteamSolidBoilerMachine extends SteamBoilerMachine<SteamSolidBoilerMachine> {

	@Persisted
	@Synchronized
	private final MachineRecipeCapabilityItems inputInventory;

	public SteamSolidBoilerMachine(final MachineType<SteamSolidBoilerMachine> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType, pos, blockState);
		this.inputInventory = new MachineRecipeCapabilityItems(this, 1, IOMode.INPUT, IOMode.INPUT);
	}

	@Override
	public void populateWidgetPanel(final WidgetGroup panel) {
		super.populateWidgetPanel(panel);
		panel.addWidget(new SlotWidget(this.inputInventory.inventory, 0, 115, 51).setBackgroundTexture(this.getGuiTheme().getSlotItem1()));
		panel.addWidget(new ProgressWidget(this::getBurnTimePercentage, 115, 30, 18, 18)
				.setProgressTexture(
						GuiTextures.BOILER_FUEL.getSubTexture(0, 0, 1, 0.5),
						GuiTextures.BOILER_FUEL.getSubTexture(0, 0.5, 1, 0.5)
				).setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
		);
	}
}
