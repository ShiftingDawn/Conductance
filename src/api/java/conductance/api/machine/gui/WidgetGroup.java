package conductance.api.machine.gui;

import java.util.Collections;
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
		widget.setRelativeX(this.getX());
		widget.setRelativeY(this.getY());
		return this;
	}

	public void updateWidgetPositions() {
		for (final GuiWidget widget : this.widgets.values()) {
			widget.setRelativeX(this.getX());
			widget.setRelativeY(this.getY());
		}
	}

	@Override
	public void init(final MachineScreen screen) {
		this.updateWidgetPositions();
		for (final GuiWidget child : this.widgets.values()) {
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

	@Override
	public void setInitialX(final int initialX) {
		super.setInitialX(initialX);
		this.updateWidgetPositions();
	}

	@Override
	public void setInitialY(final int initialY) {
		super.setInitialY(initialY);
		this.updateWidgetPositions();
	}

	@Override
	public void setX(final int x) {
		super.setX(x);
		this.updateWidgetPositions();
	}

	@Override
	public void setY(final int y) {
		super.setY(y);
		this.updateWidgetPositions();
	}

	@Override
	public void setRelativeX(final int newX) {
		super.setRelativeX(newX);
		this.updateWidgetPositions();
	}

	@Override
	public void setRelativeY(final int newY) {
		super.setRelativeY(newY);
		this.updateWidgetPositions();
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

	public final Map<String, GuiWidget> getWidgets() {
		return Collections.unmodifiableMap(this.widgets);
	}

	public final Map<String, GuiWidget> getWidgetsFlattened() {
		final Map<String, GuiWidget> result = new HashMap<>();
		this.widgets.forEach((key, widget) -> {
			if (widget instanceof final WidgetGroup group) {
				result.putAll(group.getWidgetsFlattened());
			} else {
				result.put(key, widget);
			}
		});
		return result;
	}
}
