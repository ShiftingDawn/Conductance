package conductance.client;

import net.minecraft.Util;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.api.machine.gui.MachineGuiSupplier;
import static conductance.client.GuiHelper.GUI_HEIGHT;
import static conductance.client.GuiHelper.GUI_WIDTH;

public class RootWidget extends WidgetGroup {

	private final MachineBlockEntity<?> machine;
	private final MachineGuiHolder holder;

	public <T extends MachineBlockEntity<?> & MachineGuiHolder> RootWidget(final T machine) {
		super(0, 0, GUI_WIDTH, GUI_HEIGHT);
		this.setBackground(GuiTextures.BACKGROUND);
		this.machine = machine;
		this.holder = machine;
	}

	@Override
	public void initWidget() {
		super.initWidget();
		Util.make(GuiHelper.createPlayerInventory(this.gui.entityPlayer.getInventory()), widget -> {
			this.addWidget(widget);
			widget.setSelfPosition(this.holder.getPlayerInvX(), this.holder.getPlayerInvY());
		});
		final MachineGuiSupplier guiSupplier = this.machine.getMachineType().getGuiSupplier();
		if (guiSupplier != null) {
			final WidgetGroup contents = guiSupplier.createDefault();
			guiSupplier.setupGui(contents, this.machine, true);
			this.addWidget(contents);
			//TODO Add default parts here (energy bar, etc)
		} else {
			final WidgetGroup group = new WidgetGroup(0, 0, GUI_WIDTH, GUI_HEIGHT / 2);
			this.addWidget(group);
			this.holder.populateWidgetPanel(group);
		}
	}
}
