package conductance.api.machine.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.UnknownNullability;

public final class WidgetGroup extends GuiWidget {

	private final Map<String, GuiWidget> widgets = new HashMap<>();
	private final Map<GuiWidget, String> widgetsReversed = new HashMap<>();

	public WidgetGroup(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);
	}

	public WidgetGroup addWidget(final String id, final GuiWidget widget) {
		this.widgets.put(id, widget);
		this.widgetsReversed.put(widget, id);
		widget.setWidgetPacketHandler(this::sendToServer);
		widget.setMenu(this::getMenu);
		widget.setScreen(this::getScreen);
		widget.setRelativeX(this.getX());
		widget.setRelativeY(this.getY());
		return this;
	}

	private void sendToServer(final GuiWidget widget, final int requestId, final Consumer<ValueOutput> payloadFactory) {
		this.sendToServer(0, contentFactory -> {
			final String key = Objects.requireNonNull(this.getMenu().getWidgetId(widget), "Cannot send client request for unknown widget.");
			contentFactory.putString("w", key);
			contentFactory.putInt("r", requestId);
			payloadFactory.accept(contentFactory.child("d"));
		});
	}

	@Override
	protected void handleClientRequest(final int requestId, final ValueInput input) {
		super.handleClientRequest(requestId, input);
		if (requestId == 0) {
			final String key = input.getString("w").orElseThrow(() -> new IllegalStateException("Missing widget key in client widget request"));
			final int req = input.getInt("r").orElseThrow(() -> new IllegalStateException("Missing request id in client widget request"));
			final ValueInput data = input.childOrEmpty("d");
			final GuiWidget widget = Objects.requireNonNull(this.widgets.get(key), "Invalid widget key in client widget request");
			widget.handleClientRequest(req, data);
		}
	}

	@Override
	public void initClient() {
		this.updateWidgetPositions();
		for (final GuiWidget child : this.widgets.values()) {
			child.initClient();
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
	public void onPositionChanged(final int newX, final int newY, final int oldX, final int oldY) {
		if (newX != oldX || newY != oldY) {
			this.updateWidgetPositions();
		}
	}

	@Override
	public boolean onMouseClicked(final int mouseX, final int mouseY, final int button) {
		return this.handleMouseEvent(true, mouseX, mouseY, button);
	}

	@Override
	public boolean onMouseReleased(final int mouseX, final int mouseY, final int button) {
		return this.handleMouseEvent(false, mouseX, mouseY, button);
	}

	private boolean handleMouseEvent(final boolean click, final int relativeMouseX, final int relativeMouseY, final int button) {
		//Widgets are positioned using absolute coordinates, so untranslate mouse coords
		final int mouseX = this.getX() + relativeMouseX;
		final int mouseY = this.getY() + relativeMouseY;
		for (final GuiWidget widget : this.widgets.values()) {
			if (mouseX < widget.getX() || mouseX > widget.getX() + widget.getWidth()) {
				continue;
			}
			if (mouseY < widget.getY() || mouseY > widget.getY() + widget.getHeight()) {
				continue;
			}
			final int mx = mouseX - widget.getX();
			final int my = mouseY - widget.getY();
			if (click) {
				if (widget.onMouseClicked(mx, my, button)) {
					return true;
				}
			} else if (widget.onMouseReleased(mx, my, button)) {
				return true;
			}
		}
		return false;
	}

	private void updateWidgetPositions() {
		for (final GuiWidget widget : this.widgets.values()) {
			widget.setRelativeX(this.getX());
			widget.setRelativeY(this.getY());
		}
	}

	public @UnknownNullability GuiWidget getWidgetById(final String id) {
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

	public @UnknownNullability String getWidgetId(final GuiWidget widget) {
		String result = this.widgetsReversed.get(widget);
		if (result == null) {
			for (final GuiWidget childWidget : this.widgets.values()) {
				if (childWidget instanceof final WidgetGroup group) {
					result = group.getWidgetId(widget);
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
