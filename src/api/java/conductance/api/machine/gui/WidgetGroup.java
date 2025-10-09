package conductance.api.machine.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class WidgetGroup extends GuiWidget implements IWidgetContainer {

	private final Map<String, IGuiWidget> widgets = new HashMap<>();
	private final Map<IGuiWidget, String> widgetsReversed = new HashMap<>();

	public WidgetGroup(final int x, final int y, final int width, final int height) {
		super(x, y, width, height);
	}

	//region Data
	@Override
	public void handleClientRequest(final int requestId, final ValueInput input) {
		super.handleClientRequest(requestId, input);
		if (requestId == 0) {
			final String key = input.getString("w").orElseThrow(() -> new IllegalStateException("Missing widget key in client widget request"));
			final int req = input.getInt("r").orElseThrow(() -> new IllegalStateException("Missing request id in client widget request"));
			final ValueInput data = input.childOrEmpty("d");
			final IGuiWidget widget = Objects.requireNonNull(this.widgets.get(key), "Invalid widget key in client widget request");
			widget.handleClientRequest(req, data);
		}
	}

	private void sendToServer(final IGuiWidget widget, final int requestId, @Nullable final Consumer<ValueOutput> payloadFactory) {
		this.sendToServer(0, contentFactory -> {
			final String key = Objects.requireNonNull(this.getWidgetId(widget), "Cannot send client request for unknown widget.");
			contentFactory.putString("w", key);
			contentFactory.putInt("r", requestId);
			if (payloadFactory != null) {
				payloadFactory.accept(contentFactory.child("d"));
			}
		});
	}
	//endregion

	//region Rendering
	@Override
	public void renderBackground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		IWidgetContainer.super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
	}
	//endregion

	//region Internal
	@Override
	public WidgetBindingInfo internalCreateBindingInfo(final IGuiWidget targetChild) {
		return new WidgetBindingInfo(this::sendToServer, this::getMenu, this::getScreen, this.internalGetBounds());
	}

	@Override
	public Map<String, IGuiWidget> internalGetWidgets() {
		return this.widgets;
	}

	@Override
	public Map<IGuiWidget, String> internalGetWidgetsReversed() {
		return this.widgetsReversed;
	}
	//endregion
}
