package conductance.api.machine.gui;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import lombok.Getter;
import conductance.api.CAPI;

public class GuiTheme {

	public static final GuiTheme THEME_DEFAULT = new GuiTheme("default", 0xD2D2D2);
	public static final GuiTheme THEME_BRONZE = new GuiTheme("bronze", 0xDEC6B7);

	private final String themeName;
	private final @Getter int textColor;
	private final Later<GuiDrawable> textureBackground = new Later<>();
	private final Later<GuiDrawable> textureTitleBackground = new Later<>();
	private final Later<GuiDrawable> textureInfoDisplay = new Later<>();
	private final Later<GuiDrawable> texturePlayerInventory = new Later<>();
	private final Later<GuiDrawable> texturePlayerHotbar = new Later<>();
	private final Later<GuiDrawable> textureSlotItem1 = new Later<>();
	private final Later<GuiDrawable> textureSlotItem2 = new Later<>();
	private final Later<GuiDrawable> textureSlotItem3 = new Later<>();
	private final Later<GuiDrawable> textureSlotItem4 = new Later<>();
	private final Later<GuiDrawable> textureSlotItem5 = new Later<>();
	private final Later<GuiDrawable> textureSlotItem5Mirrored = new Later<>();
	private final Later<GuiDrawable> textureSlotItem6 = new Later<>();
	private final Later<GuiDrawable> textureSlotItem9 = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid1 = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid2 = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid3 = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid4 = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid5 = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid5Mirrored = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid6 = new Later<>();
	private final Later<GuiDrawable> textureSlotFluid9 = new Later<>();
	private final Later<GuiDrawable> textureSlotHighlightBack = new Later<>();
	private final Later<GuiDrawable> textureSlotHighlightFront = new Later<>();

	private final Later<GuiDrawable> textureEnergyBar = new Later<>();
	private final Later<GuiDrawable> textureEnergyBarOverlay = new Later<>();

	public GuiTheme(final String themeName, final int textColor) {
		this.themeName = themeName;
		this.textColor = ARGB.opaque(textColor);
	}

	public GuiDrawable getBackground() {
		return this.textureBackground.getOrSet(() -> new GuiDrawableTexture(this.getPath("background")));
	}

	public GuiDrawable getTitleBackground() {
		return this.textureTitleBackground.getOrSet(() -> new GuiDrawableTexture(this.getPath("title_background")));
	}

	public GuiDrawable getInfoDisplay() {
		return this.textureInfoDisplay.getOrSet(() -> new GuiDrawableTexture(this.getPath("info_display")));
	}

	public GuiDrawable getPlayerInventory() {
		return this.texturePlayerInventory.getOrSet(() -> new GuiDrawableTexture(this.getPath("player_inventory")));
	}

	public GuiDrawable getPlayerHotbar() {
		return this.texturePlayerHotbar.getOrSet(() -> new GuiDrawableTexture(this.getPath("player_hotbar")));
	}

	public GuiDrawable getSlotItem1() {
		return this.textureSlotItem1.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_1")));
	}

	public GuiDrawable getSlotItem2() {
		return this.textureSlotItem2.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_2")));
	}

	public GuiDrawable getSlotItem3() {
		return this.textureSlotItem3.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_3")));
	}

	public GuiDrawable getSlotItem4() {
		return this.textureSlotItem4.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_4")));
	}

	public GuiDrawable getSlotItem5() {
		return this.textureSlotItem5.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_5")));
	}

	public GuiDrawable getSlotItem5Mirrored() {
		return this.textureSlotItem5Mirrored.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_5_mirrored")));
	}

	public GuiDrawable getSlotItem6() {
		return this.textureSlotItem6.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_6")));
	}

	public GuiDrawable getSlotItem9() {
		return this.textureSlotItem9.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_item_9")));
	}

	public GuiDrawable getSlotFluid1() {
		return this.textureSlotFluid1.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_1")));
	}

	public GuiDrawable getSlotFluid2() {
		return this.textureSlotFluid2.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_2")));
	}

	public GuiDrawable getSlotFluid3() {
		return this.textureSlotFluid3.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_3")));
	}

	public GuiDrawable getSlotFluid4() {
		return this.textureSlotFluid4.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_4")));
	}

	public GuiDrawable getSlotFluid5() {
		return this.textureSlotFluid5.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_5")));
	}

	public GuiDrawable getSlotFluid5Mirrored() {
		return this.textureSlotFluid5Mirrored.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_5_mirrored")));
	}

	public GuiDrawable getSlotFluid6() {
		return this.textureSlotFluid6.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_6")));
	}

	public GuiDrawable getSlotFluid9() {
		return this.textureSlotFluid9.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_fluid_9")));
	}

	public GuiDrawable getItemSlots(final int amount, final boolean mirrored) {
		return switch (amount) {
			case 1 -> this.getSlotItem1();
			case 2 -> this.getSlotItem2();
			case 3 -> this.getSlotItem3();
			case 4 -> this.getSlotItem4();
			case 5 -> mirrored ? this.getSlotItem5Mirrored() : this.getSlotItem5();
			case 6 -> this.getSlotItem6();
			default -> this.getSlotItem9();
		};
	}

	public GuiDrawable getFluidSlots(final int amount, final boolean mirrored) {
		return switch (amount) {
			case 1 -> this.getSlotFluid1();
			case 2 -> this.getSlotFluid2();
			case 3 -> this.getSlotFluid3();
			case 4 -> this.getSlotFluid4();
			case 5 -> mirrored ? this.getSlotFluid5Mirrored() : this.getSlotFluid5();
			case 6 -> this.getSlotFluid6();
			default -> this.getSlotFluid9();
		};
	}

	public GuiDrawable getSlotHighlightBack() {
		return this.textureSlotHighlightBack.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_highlight_back")));
	}

	public GuiDrawable getSlotHighlightFront() {
		return this.textureSlotHighlightFront.getOrSet(() -> new GuiDrawableTexture(this.getPath("slot_highlight_front")));
	}

	public GuiDrawable getEnergyBar() {
		return this.textureEnergyBar.getOrSet(() -> new GuiDrawableTexture(this.getPath("energy_bar")));
	}

	public GuiDrawable getEnergyBarOverlay() {
		return this.textureEnergyBarOverlay.getOrSet(() -> new GuiDrawableTexture(this.getPath("energy_bar_overlay")));
	}

	protected final ResourceLocation getPath(final String name) {
		String path = "%s:textures/gui/conductance/%s/%s.png".formatted(CAPI.MOD_ID, this.themeName, name);
		if (CAPI.resourceFinder().isResourceValid(ResourceLocation.parse(path))) {
			return ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "%s/%s/%s".formatted(CAPI.MOD_ID, this.themeName, name));
		} else if (!this.themeName.equals("default")) {
			path = "%s:textures/gui/conductance/default/%s.png".formatted(CAPI.MOD_ID, name);
			if (CAPI.resourceFinder().isResourceValid(ResourceLocation.parse(path))) {
				return ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "%s/default/%s".formatted(CAPI.MOD_ID, name));
			}
		}
		return ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "%s/%s".formatted(CAPI.MOD_ID, name));
	}
}
