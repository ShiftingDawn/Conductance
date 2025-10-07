package conductance.api.machine.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public interface WidgetHolder {

	//region Children
	default void addWidget(final String id, final IGuiWidget widget) {
		this.internalGetWidgets().put(id, widget);
		this.internalGetWidgetsReversed().put(widget, id);
		widget.internalBindToParentWidget(this.internalCreateBindingInfo(widget));
	}

	@UnknownNullability
	default IGuiWidget getWidgetById(final String id) {
		IGuiWidget result = this.internalGetWidgets().get(id);
		if (result == null) {
			for (final IGuiWidget widget : this.internalGetWidgets().values()) {
				if (widget instanceof final IWidgetContainer container) {
					result = container.getWidgetById(id);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return result;
	}

	default @UnknownNullability String getWidgetId(final IGuiWidget widget) {
		String result = this.internalGetWidgetsReversed().get(widget);
		if (result == null) {
			for (final IGuiWidget childWidget : this.internalGetWidgets().values()) {
				if (childWidget instanceof final IWidgetContainer container) {
					result = container.getWidgetId(widget);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return result;
	}

	default @Nullable IGuiWidget getWidgetUnderMouse(final int mouseX, final int mouseY) {
		for (final IGuiWidget widget : this.getWidgets().values()) {
			if (widget.containsMouse(mouseX, mouseY)) {
				if (widget instanceof final IWidgetContainer container) {
					return container.getWidgetUnderMouse(mouseX, mouseY);
				}
				return widget;
			}
		}
		return null;
	}

	default Map<String, IGuiWidget> getWidgets() {
		return Collections.unmodifiableMap(this.internalGetWidgets());
	}

	default Map<String, IGuiWidget> getWidgetsFlattened() {
		final Map<String, IGuiWidget> result = new HashMap<>();
		this.getWidgets().forEach((key, widget) -> {
			if (widget instanceof final IWidgetContainer container) {
				result.putAll(container.getWidgetsFlattened());
			} else {
				result.put(key, widget);
			}
		});
		return result;
	}
	//endregion

	//region Internal
	WidgetBindingInfo internalCreateBindingInfo(IGuiWidget targetChild);

	Map<String, IGuiWidget> internalGetWidgets();

	Map<IGuiWidget, String> internalGetWidgetsReversed();
	//endregion
}
