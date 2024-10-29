package conductance.core.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.client.resourcepack.RuntimeResourcePack;

@Mixin(ModelManager.class)
public class ModelManagerMixin {

	@Inject(method = "reload", at = @At("HEAD"))
	private void conductance$injectRuntimeResourcePackModels(final PreparableReloadListener.PreparationBarrier preparationBarrier, final ResourceManager resourceManager, final ProfilerFiller preparationsProfiler,
			final ProfilerFiller reloadProfiler, final Executor backgroundExecutor, final Executor gameExecutor, final CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		RuntimeResourcePack.load();
	}
}
