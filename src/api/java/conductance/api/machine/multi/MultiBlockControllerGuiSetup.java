package conductance.api.machine.multi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.ManagedInt;
import conductance.api.machine.gui.MutableSize;
import conductance.api.machine.gui.Rectangle;
import conductance.api.machine.gui.WidgetGroup;

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
			root.setBackground(this.getTheme().getInfoDisplay());
			root.addWidget("display", CAPI.make(new MultiBlockDisplay(2, 2, root.getWidth(), root.getHeight(), machine), widget -> {
				widget.setSize(MutableSize.of(
					new ManagedInt(null, () -> root.getWidth() - 4),
					new ManagedInt(null, () -> root.getHeight() - 4)
				));
			}));
		}));
	}

	@Override
	public void init(final MachineScreen screen, final Rectangle rootBounds) {
		final IGuiWidget root = screen.getMenu().getWidgetById("root");
		root.setBounds(rootBounds);
	}

	private static class MultiBlockDisplay extends GuiWidget {

		private final IMultiBlockController<?> controller;
		private double scrollAmountY = 0;

		protected MultiBlockDisplay(final int x, final int y, final int width, final int height, final IMultiBlockController<?> controller) {
			super(x, y, width, height);
			this.controller = controller;
		}

		@Override
		public void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
			final List<Component> textList = new ArrayList<>();
			this.controller.addScreenInfo(textList);
			this.controller.getParts().forEach(part -> part.addScreenInfo(textList));
			final int maxScroll = textList.size() * this.getFont().lineHeight;
			if (this.scrollAmountY > maxScroll - this.getHeight()) {
				this.scrollAmountY = maxScroll - this.getHeight();
			}
			guiGraphics.enableScissor(this.getX(), this.getY(), this.getBounds().maxX(), this.getBounds().maxY());
			guiGraphics.pose().pushMatrix();
			if (this.scrollAmountY > 0) {
				guiGraphics.pose().translate(0, (float) -this.scrollAmountY);
			}
			int y = this.getY();
			for (final Component line : textList) {
				guiGraphics.drawString(this.getFont(), line, this.getX(), y, -1);
				y += this.getFont().lineHeight;
			}
			guiGraphics.pose().popMatrix();
			guiGraphics.disableScissor();
		}

		@Override
		public boolean onMouseScrolled(final int mouseX, final int mouseY, final double deltaX, final double deltaY) {
			this.scrollAmountY = Math.max(this.scrollAmountY - (deltaY * 3), 0);
			return true;
		}

		@Override
		public boolean onMouseDragged(final int mouseX, final int mouseY, final int button, final double dragX, final double dragY) {
			this.scrollAmountY = Math.max(this.scrollAmountY - dragY, 0);
			return true;
		}
	}
}
