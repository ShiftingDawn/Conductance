package conductance.api.machine.gui;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.util.Lazy;

public final class GuiTextures {

	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_ITEM_OUTPUT_OFF = GuiTextures.make("item_auto_output_off");
	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_ITEM_OUTPUT_ON = GuiTextures.make("item_auto_output_on");
	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_ITEM_INPUT_OFF = GuiTextures.make("item_auto_input_off");
	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_ITEM_INPUT_ON = GuiTextures.make("item_auto_input_on");
	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_FLUID_OUTPUT_OFF = GuiTextures.make("fluid_auto_output_off");
	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_FLUID_OUTPUT_ON = GuiTextures.make("fluid_auto_output_on");
	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_FLUID_INPUT_OFF = GuiTextures.make("fluid_auto_input_off");
	public static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_FLUID_INPUT_ON = GuiTextures.make("fluid_auto_input_on");

	private static Lazy<GuiDrawableTexture> make(final String name) {
		return Lazy.of(() -> new GuiDrawableTexture(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, CAPI.MOD_ID + "/" + name)));
	}

	private GuiTextures() {
	}
}
