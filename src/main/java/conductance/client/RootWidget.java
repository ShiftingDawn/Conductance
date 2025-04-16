package conductance.client;

import net.minecraft.Util;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import conductance.api.capability.energy.IEnergyHandler;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.capability.MachineRecipeCapabilityEnergy;
import conductance.api.machine.gui.EnergyBarWidget;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.api.machine.gui.MachineGuiSupplier;
import static conductance.client.GuiHelper.GUI_HEIGHT;
import static conductance.client.GuiHelper.GUI_WIDTH;

public class RootWidget extends WidgetGroup {

	private final MachineBlockEntity<?> machine;
	private final MachineGuiHolder holder;

	public <T extends MachineBlockEntity<?> & MachineGuiHolder> RootWidget(final T machine) {
		super(0, 0, GUI_WIDTH, GUI_HEIGHT);
		this.machine = machine;
		this.holder = machine;
		this.setBackground(this.holder.getGuiTheme().getBackground());
	}

	@Override
	public void initWidget() {
		super.initWidget();
		Util.make(GuiHelper.createPlayerInventory(this.gui.entityPlayer.getInventory(), this.holder.getGuiTheme()), widget -> {
			this.addWidget(widget);
			widget.setSelfPosition(this.holder.getPlayerInvX(), this.holder.getPlayerInvY());
		});
		final MachineGuiSupplier guiSupplier = this.machine.getMachineType().getGuiSupplier();
		if (guiSupplier != null) {
			final WidgetGroup contents = guiSupplier.createDefault();
			guiSupplier.setupGui(contents, this.machine, this.holder.getGuiTheme(), true);
			this.addWidget(contents);

			if (this.machine.getCapability(MachineRecipeCapabilityEnergy.class) instanceof final IEnergyHandler energyHandler) {
				final ProgressWidget energyBar = new EnergyBarWidget(this.holder.getGuiTheme(), energyHandler);
				energyBar.setSelfPosition(new Position(this.holder.getPlayerInvX(), this.holder.getPlayerInvY() - 8));
				contents.addWidget(energyBar);
			}
			//TODO add auto output buttons
		} else {
			final WidgetGroup group = new WidgetGroup(0, 0, GUI_WIDTH, GUI_HEIGHT / 2);
			this.addWidget(group);
			this.holder.populateWidgetPanel(group);
		}
	}
}
