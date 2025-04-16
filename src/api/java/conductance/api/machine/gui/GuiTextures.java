package conductance.api.machine.gui;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import conductance.api.CAPI;

public final class GuiTextures {

	public static final ResourceTexture BOILER_SLOT = new ResourceTexture(GuiTextures.get("boiler_slot"));
	public static final ResourceTexture BOILER_FUEL = new ResourceTexture(GuiTextures.get("boiler_fuel"));
	public static final ResourceTexture BOILER_TEMPERATURE = new ResourceTexture(GuiTextures.get("boiler_temperature"));

	private static String get(final String name) {
		return "%s:textures/gui/%s.png".formatted(CAPI.MOD_ID, name);
	}

	private GuiTextures() {
	}
}
