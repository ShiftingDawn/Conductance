package conductance.client;

import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.gui.GeneratedGuiHolder;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.MachineGuiSupplier;
import static conductance.client.MetaBlockEntityUIFactory.GUI_HEIGHT;
import static conductance.client.MetaBlockEntityUIFactory.GUI_WIDTH;

public class RootWidget extends WidgetGroup {

	private final MetaBlockEntity<?> mbe;

	public RootWidget(final MetaBlockEntity<?> mbe) {
		super(0, 0, GUI_WIDTH, GUI_HEIGHT);
		this.mbe = mbe;
	}

	@Override
	public void initWidget() {
		super.initWidget();
		final MachineGuiSupplier guiSupplier = this.mbe.getMetaType().getGuiSupplier();
		if (guiSupplier != null) {
			final WidgetGroup contents = guiSupplier.createDefault();
			guiSupplier.setupGui(contents, this.mbe, true);

			//TODO Add default parts here (energy bar, etc)
		} else if (this.mbe instanceof final GeneratedGuiHolder holder) {
			final WidgetGroup group = new WidgetGroup(0, 0, GUI_WIDTH, GUI_HEIGHT / 2);
			group.setBackground(GuiTextures.WALLPAPER);
			this.addWidget(group);
			holder.populateWidgetPanel(group);
		}
	}
}
