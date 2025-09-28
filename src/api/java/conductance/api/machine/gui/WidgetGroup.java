package conductance.api.machine.gui;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.UnknownNullability;

public class WidgetGroup extends GuiWidget {

	private final Map<String, GuiWidget> widgets = new HashMap<>();

	public WidgetGroup(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);
	}

	public WidgetGroup addWidget(final String id, final GuiWidget widget) {
		this.widgets.put(id, widget);
		return this;
	}

	@UnknownNullability
	public GuiWidget getWidgetById(final String id) {
		GuiWidget result = this.widgets.get(id);
		if (result == null) {
			for (final GuiWidget widget : this.widgets.values()) {
				if (widget instanceof final WidgetGroup group) {
					result = group.getWidgetById(id);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return result;
	}

	@Override
	public void init(final MachineScreen screen) {
		for (final GuiWidget child : this.widgets.values()) {
			child.setRelativeX(this.getX());
			child.setRelativeY(this.getY());
			child.init(screen);
		}
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		for (final GuiWidget child : this.widgets.values()) {
			child.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}
}
