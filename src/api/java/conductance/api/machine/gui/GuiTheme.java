package conductance.api.machine.gui;

import net.minecraft.resources.ResourceLocation;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import conductance.api.CAPI;

public class GuiTheme {
	public static final GuiTheme THEME_DEFAULT = new GuiTheme("default");
	public static final GuiTheme THEME_BRONZE = new GuiTheme("bronze");

	private final String themeName;
	private final Later<IGuiTexture> textureBackground = new Later<>();
	private final Later<IGuiTexture> texturePlayerInventory = new Later<>();
	private final Later<IGuiTexture> texturePlayerHotbar = new Later<>();
	private final Later<IGuiTexture> textureSlotItem1 = new Later<>();
	private final Later<IGuiTexture> textureSlotItem2 = new Later<>();
	private final Later<IGuiTexture> textureSlotItem3 = new Later<>();
	private final Later<IGuiTexture> textureSlotItem4 = new Later<>();
	private final Later<IGuiTexture> textureSlotItem5 = new Later<>();
	private final Later<IGuiTexture> textureSlotItem5Mirrored = new Later<>();
	private final Later<IGuiTexture> textureSlotItem6 = new Later<>();
	private final Later<IGuiTexture> textureSlotItem9 = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid1 = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid2 = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid3 = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid4 = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid5 = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid5Mirrored = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid6 = new Later<>();
	private final Later<IGuiTexture> textureSlotFluid9 = new Later<>();
	private final Later<IGuiTexture> textureSlotHover = new Later<>();

	private final Later<IGuiTexture> textureEnergyBar = new Later<>();
	private final Later<IGuiTexture> textureEnergyBarOverlay = new Later<>();

	public GuiTheme(final String themeName) {
		this.themeName = themeName;
	}

	public IGuiTexture getBackground() {
		return this.textureBackground.getOrSet(() -> new ResourceBorderTexture(this.getPath("background"), 16, 16, 4, 4));
	}

	public IGuiTexture getPlayerInventory() {
		return this.texturePlayerInventory.getOrSet(() -> new ResourceTexture(this.getPath("player_inventory")));
	}

	public IGuiTexture getPlayerHotbar() {
		return this.texturePlayerHotbar.getOrSet(() -> new ResourceTexture(this.getPath("player_hotbar")));
	}

	public IGuiTexture getSlotItem1() {
		return this.textureSlotItem1.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_1")));
	}

	public IGuiTexture getSlotItem2() {
		return this.textureSlotItem2.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_2")));
	}

	public IGuiTexture getSlotItem3() {
		return this.textureSlotItem3.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_3")));
	}

	public IGuiTexture getSlotItem4() {
		return this.textureSlotItem4.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_4")));
	}

	public IGuiTexture getSlotItem5() {
		return this.textureSlotItem5.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_5")));
	}

	public IGuiTexture getSlotItem5Mirrored() {
		return this.textureSlotItem5Mirrored.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_5_mirrored")));
	}

	public IGuiTexture getSlotItem6() {
		return this.textureSlotItem6.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_6")));
	}

	public IGuiTexture getSlotItem9() {
		return this.textureSlotItem9.getOrSet(() -> new ResourceTexture(this.getPath("slot_item_9")));
	}

	public IGuiTexture getSlotFluid1() {
		return this.textureSlotFluid1.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_1")));
	}

	public IGuiTexture getSlotFluid2() {
		return this.textureSlotFluid2.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_2")));
	}

	public IGuiTexture getSlotFluid3() {
		return this.textureSlotFluid3.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_3")));
	}

	public IGuiTexture getSlotFluid4() {
		return this.textureSlotFluid4.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_4")));
	}

	public IGuiTexture getSlotFluid5() {
		return this.textureSlotFluid5.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_5")));
	}

	public IGuiTexture getSlotFluid5Mirrored() {
		return this.textureSlotFluid5Mirrored.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_5_mirrored")));
	}

	public IGuiTexture getSlotFluid6() {
		return this.textureSlotFluid6.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_6")));
	}

	public IGuiTexture getSlotFluid9() {
		return this.textureSlotFluid9.getOrSet(() -> new ResourceTexture(this.getPath("slot_fluid_9")));
	}

	public IGuiTexture getItemSlots(final int amount, final boolean mirrored) {
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

	public IGuiTexture getFluidSlots(final int amount, final boolean mirrored) {
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

	public IGuiTexture getSlotHover() {
		return this.textureSlotHover.getOrSet(() -> new ColorRectTexture(0x669cd3ff));
	}

	public IGuiTexture getEnergyBar() {
		return this.textureEnergyBar.getOrSet(() -> new ResourceTexture(this.getPath("energy_bar")));
	}

	public IGuiTexture getEnergyBarOverlay() {
		return this.textureEnergyBarOverlay.getOrSet(() -> new ResourceTexture(this.getPath("energy_bar_overlay")));
	}

	protected final String getPath(final String name) {
		String path = "%s:textures/gui/%s/%s.png".formatted(CAPI.MOD_ID, this.themeName, name);
		if (CAPI.resourceFinder().isResourceValid(ResourceLocation.parse(path))) {
			return path;
		} else if (!this.themeName.equals("default")) {
			path = "%s:textures/gui/default/%s.png".formatted(CAPI.MOD_ID, name);
			if (CAPI.resourceFinder().isResourceValid(ResourceLocation.parse(path))) {
				return path;
			}
		}
		return "%s:textures/gui/%s.png".formatted(CAPI.MOD_ID, name);
	}
}
