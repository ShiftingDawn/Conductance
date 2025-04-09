package conductance.client;

import net.minecraft.Util;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.gui.MetaBlockEntityGuiHolder;
import static conductance.client.GuiHelper.GUI_HEIGHT;
import static conductance.client.GuiHelper.GUI_WIDTH;

public class RootWidget extends WidgetGroup {

	private final MetaBlockEntity<?> mbe;
	private final MetaBlockEntityGuiHolder holder;

	public <T extends MetaBlockEntity<?> & MetaBlockEntityGuiHolder> RootWidget(final T mbe) {
		super(0, 0, GUI_WIDTH, GUI_HEIGHT);
		this.setBackground(GuiTextures.BACKGROUND);
		this.mbe = mbe;
		this.holder = mbe;
	}

	@Override
	public void initWidget() {
		super.initWidget();
		Util.make(GuiHelper.createPlayerInventory(this.gui.entityPlayer.getInventory()), widget -> {
			this.addWidget(widget);
			widget.setSelfPosition(this.holder.getPlayerInvX(), this.holder.getPlayerInvY());
		});
		final MachineGuiSupplier guiSupplier = this.mbe.getMetaType().getGuiSupplier();
		if (guiSupplier != null) {
			final WidgetGroup contents = guiSupplier.createDefault();
			guiSupplier.setupGui(contents, this.mbe, true);
			this.addWidget(contents);
			//TODO Add default parts here (energy bar, etc)
		} else {
			final WidgetGroup group = new WidgetGroup(0, 0, GUI_WIDTH, GUI_HEIGHT / 2);
			this.addWidget(group);
			this.holder.populateWidgetPanel(group);
		}
	}
}
