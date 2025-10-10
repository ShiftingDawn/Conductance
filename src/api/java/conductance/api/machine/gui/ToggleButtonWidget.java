package conductance.api.machine.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.storage.ValueInput;
import it.unimi.dsi.fastutil.booleans.Boolean2ObjectFunction;
import org.jetbrains.annotations.Nullable;

public class ToggleButtonWidget extends GuiWidget {

	private final ManagedBoolean handler;
	private final Boolean2ObjectFunction<@Nullable GuiDrawable> foregroundTextureProvider;

	public ToggleButtonWidget(final int x, final int y, final int width, final int height, final ManagedBoolean handler, final Boolean2ObjectFunction<@Nullable GuiDrawable> foregroundTextureProvider) {
		super(x, y, width, height);
		this.handler = handler;
		this.foregroundTextureProvider = foregroundTextureProvider;
	}

	protected @Nullable GuiDrawable getBackground(final boolean toggled) {
		return toggled ? this.getTheme().getButtonActive() : this.getTheme().getButton();
	}

	protected @Nullable GuiDrawable getForeground(final boolean toggled) {
		return this.foregroundTextureProvider.get(toggled);
	}

	@Override
	public void renderBackground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		final GuiDrawable background = this.getBackground(this.handler.getAsBoolean());
		if (background != null) {
			background.draw(guiGraphics, mouseX, mouseY, this.getBounds());
		}
	}

	@Override
	public void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		final GuiDrawable foreground = this.getForeground(this.handler.getAsBoolean());
		if (foreground != null) {
			foreground.draw(guiGraphics, mouseX, mouseY, this.getBounds());
		}
	}

	@Override
	public boolean onMouseClicked(final int mouseX, final int mouseY, final int button) {
		this.sendToServer(1, null);
		return true;
	}

	@Override
	public void handleClientRequest(final int requestId, final ValueInput input) {
		super.handleClientRequest(requestId, input);
		if (requestId == 1) {
			final boolean newState = !this.handler.getAsBoolean();
			this.handler.accept(newState);
			this.sendToClient(1, out -> out.putBoolean("s", newState));
		}
	}

	@Override
	public void handleServerRequest(final int requestId, final ValueInput input) {
		super.handleServerRequest(requestId, input);
		if (requestId == 1) {
			this.handler.accept(input.getBooleanOr("s", false));
		}
	}
}
