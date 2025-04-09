package conductance.api.machine.gui;

import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import conductance.api.CAPI;

public class GuiTextures {

	public static final ResourceBorderTexture WALLPAPER = new ResourceBorderTexture(GuiTextures.get("bg"), 16, 16, 4, 4);
	public static final ResourceTexture PLAYER_INVENTORY = new ResourceTexture(GuiTextures.get("player_inv"));
	public static final ResourceTexture PLAYER_HOTBAR = new ResourceTexture(GuiTextures.get("player_bar"));

	private static String get(final String name) {
		return "%s:textures/gui/%s.png".formatted(CAPI.MOD_ID, name);
	}
}
