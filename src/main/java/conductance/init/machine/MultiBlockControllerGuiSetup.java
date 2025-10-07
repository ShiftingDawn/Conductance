package conductance.init.machine;

import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.gui.GuiDrawableTexture;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.Rectangle;
import conductance.api.machine.gui.TextLabelWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;
import conductance.api.machine.multi.StructureCheckContext;
import conductance.Conductance;

public class MultiBlockControllerGuiSetup extends GuiSetup {

	private final @Getter GuiTheme theme;

	public MultiBlockControllerGuiSetup(@Nullable final GuiTheme theme) {
		this.theme = Objects.requireNonNullElse(theme, GuiTheme.THEME_DEFAULT);
	}

	public MultiBlockControllerGuiSetup() {
		this(null);
	}

	@Override
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		final MultiControllerMachineBlockEntity<?> machine = (MultiControllerMachineBlockEntity<?>) menu.getMachine();
		adder.accept("root", CAPI.make(new WidgetGroup(0, 0, 0, 0), root -> {
			root.setBackground(new GuiDrawableTexture(Conductance.id(Conductance.MODID + "/screen")));
			root.addWidget("structure_status", new TextLabelWidget(2, 2, () -> {
				if (machine.checkStructure(new StructureCheckContext())) {
					return Component.translatable("guiWidget.conductance.multiblock.structure.formed");
				} else {
					return Component.translatable("guiWidget.conductance.multiblock.structure.invalid");
				}
			}));
		}));
	}

	@Override
	public void init(final MachineScreen screen, final Rectangle rootBounds) {
		final IGuiWidget root = screen.getMenu().getWidgetById("root");
		root.setBounds(rootBounds);
	}
}
