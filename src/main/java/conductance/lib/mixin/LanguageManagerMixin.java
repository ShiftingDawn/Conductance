package conductance.lib.mixin;

import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import conductance.core.runtimepack.client.RuntimeResourcePackBridge;

@Mixin(LanguageManager.class)
public abstract class LanguageManagerMixin {

	@Inject(method = "onResourceManagerReload", at = @At("HEAD"))
	private void conductance$injectRuntimeResourcePackTranslations(final ResourceManager resourceManager, final CallbackInfo ci) {
		RuntimeResourcePackBridge.loadTranslations();
	}
}
