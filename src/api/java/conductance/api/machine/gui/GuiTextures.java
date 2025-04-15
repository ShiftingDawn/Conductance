package conductance.api.machine.gui;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import conductance.api.CAPI;

public final class GuiTextures {

	public static final ResourceBorderTexture BACKGROUND = new ResourceBorderTexture(GuiTextures.get("background"), 16, 16, 4, 4);
	public static final ResourceTexture PLAYER_INVENTORY = new ResourceTexture(GuiTextures.get("player_inventory"));
	public static final ResourceTexture PLAYER_HOTBAR = new ResourceTexture(GuiTextures.get("player_hotbar"));

	public static final ResourceTexture SLOT_ITEM_1 = new ResourceTexture(GuiTextures.get("slot_item_1"));
	public static final ResourceTexture SLOT_ITEM_2 = new ResourceTexture(GuiTextures.get("slot_item_2"));
	public static final ResourceTexture SLOT_ITEM_3 = new ResourceTexture(GuiTextures.get("slot_item_3"));
	public static final ResourceTexture SLOT_ITEM_4 = new ResourceTexture(GuiTextures.get("slot_item_4"));
	public static final ResourceTexture SLOT_ITEM_5 = new ResourceTexture(GuiTextures.get("slot_item_5"));
	public static final ResourceTexture SLOT_ITEM_5_MIRRORED = new ResourceTexture(GuiTextures.get("slot_item_5_mirrored"));
	public static final ResourceTexture SLOT_ITEM_6 = new ResourceTexture(GuiTextures.get("slot_item_6"));
	public static final ResourceTexture SLOT_ITEM_9 = new ResourceTexture(GuiTextures.get("slot_item_9"));
	public static final ResourceTexture SLOT_FLUID_1 = new ResourceTexture(GuiTextures.get("slot_fluid_1"));
	public static final ResourceTexture SLOT_FLUID_2 = new ResourceTexture(GuiTextures.get("slot_fluid_2"));
	public static final ResourceTexture SLOT_FLUID_3 = new ResourceTexture(GuiTextures.get("slot_fluid_3"));
	public static final ResourceTexture SLOT_FLUID_4 = new ResourceTexture(GuiTextures.get("slot_fluid_4"));
	public static final ResourceTexture SLOT_FLUID_5 = new ResourceTexture(GuiTextures.get("slot_fluid_5"));
	public static final ResourceTexture SLOT_FLUID_5_MIRRORED = new ResourceTexture(GuiTextures.get("slot_fluid_5_mirrored"));
	public static final ResourceTexture SLOT_FLUID_6 = new ResourceTexture(GuiTextures.get("slot_fluid_6"));
	public static final ResourceTexture SLOT_FLUID_9 = new ResourceTexture(GuiTextures.get("slot_fluid_9"));
	public static final ColorRectTexture SLOT_HOVER = new ColorRectTexture(0x669cd3ff);

	public static final ResourceTexture ENERGY_BAR = new ResourceTexture(GuiTextures.get("energy_bar"));
	public static final ResourceTexture ENERGY_BAR_OVERLAY = new ResourceTexture(GuiTextures.get("energy_bar_overlay"));

	private static String get(final String name) {
		return "%s:textures/gui/%s.png".formatted(CAPI.MOD_ID, name);
	}

	public static ResourceTexture getItemSlots(final int amount, final boolean mirrored) {
		return switch (amount) {
			case 1 -> GuiTextures.SLOT_ITEM_1;
			case 2 -> GuiTextures.SLOT_ITEM_2;
			case 3 -> GuiTextures.SLOT_ITEM_3;
			case 4 -> GuiTextures.SLOT_ITEM_4;
			case 5 -> mirrored ? GuiTextures.SLOT_ITEM_5_MIRRORED : GuiTextures.SLOT_ITEM_5;
			case 6 -> GuiTextures.SLOT_ITEM_6;
			default -> GuiTextures.SLOT_ITEM_9;
		};
	}

	public static ResourceTexture getFluidSlots(final int amount, final boolean mirrored) {
		return switch (amount) {
			case 1 -> GuiTextures.SLOT_FLUID_1;
			case 2 -> GuiTextures.SLOT_FLUID_2;
			case 3 -> GuiTextures.SLOT_FLUID_3;
			case 4 -> GuiTextures.SLOT_FLUID_4;
			case 5 -> mirrored ? GuiTextures.SLOT_FLUID_5_MIRRORED : GuiTextures.SLOT_FLUID_5;
			case 6 -> GuiTextures.SLOT_FLUID_6;
			default -> GuiTextures.SLOT_FLUID_9;
		};
	}

	private GuiTextures() {
	}
}
