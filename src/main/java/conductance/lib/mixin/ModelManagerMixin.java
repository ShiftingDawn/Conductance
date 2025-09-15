package conductance.lib.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.lib.pack.client.RuntimeResourcePackBridge;

@Mixin(ModelManager.class)
public abstract class ModelManagerMixin {

	@Inject(method = "reload", at = @At("HEAD"))
	private void conductance$injectRuntimeResourcePackModels(final PreparableReloadListener.PreparationBarrier p_249079_, final ResourceManager p_251134_, final Executor p_250550_, final Executor p_249221_,
	                                                         final CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		RuntimeResourcePackBridge.loadModels();
	}
}
