package conductance.lib.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import conductance.api.machine.gui.MachineScreen;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

	@Inject(method = "renderSlotHighlightBack", at = @At("HEAD"), cancellable = true)
	private void conductance$renderSlotHighlightBack(final GuiGraphics guiGraphics, final CallbackInfo ci) {
		if ((Object) this instanceof MachineScreen) {
			ci.cancel();
		}
	}

	@Inject(method = "renderSlotHighlightFront", at = @At("HEAD"), cancellable = true)
	private void conductance$renderSlotHighlightFront(final GuiGraphics guiGraphics, final CallbackInfo ci) {
		if ((Object) this instanceof MachineScreen) {
			ci.cancel();
		}
	}
}
