package conductance.api.machine.gui;

import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import conductance.api.CAPI;

public final class GuiTextures {

	public static final ResourceBorderTexture BACKGROUND = new ResourceBorderTexture(GuiTextures.get("background"), 16, 16, 4, 4);
	public static final ResourceTexture PLAYER_INVENTORY = new ResourceTexture(GuiTextures.get("player_inventory"));
	public static final ResourceTexture PLAYER_HOTBAR = new ResourceTexture(GuiTextures.get("player_hotbar"));

	public static final ResourceTexture SLOT_ITEM = new ResourceTexture(GuiTextures.get("slot_item"));
	public static final ResourceTexture SLOT_FLUID = new ResourceTexture(GuiTextures.get("slot_fluid"));
	public static final ResourceTexture SLOT_FLUID_OVERLAY = new ResourceTexture(GuiTextures.get("slot_fluid_overlay"));

	private static String get(final String name) {
		return "%s:textures/gui/%s.png".formatted(CAPI.MOD_ID, name);
	}

	private GuiTextures() {
	}
}
