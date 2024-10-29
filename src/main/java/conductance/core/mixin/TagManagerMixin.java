package conductance.core.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import conductance.runtimepack.server.IConductanceTagLoader;

@SuppressWarnings("unchecked")
@Mixin(TagManager.class)
public class TagManagerMixin {

	@Inject(method = "createLoader", at = @At(value = "INVOKE", target = "Lnet/minecraft/tags/TagLoader;<init>(Ljava/util/function/Function;Ljava/lang/String;)V", shift = At.Shift.BY, by = 2), locals =
			LocalCapture.CAPTURE_FAILHARD)
	private <T> void conductance$setRegistryForTagLoaderUsage(final ResourceManager resourceManager, final Executor backgroundExecutor, final RegistryAccess.RegistryEntry<T> registryEntry,
			final CallbackInfoReturnable<CompletableFuture<TagManager.LoadResult<T>>> cir, final ResourceKey<? extends Registry<T>> resourceKey, final Registry<T> registry, final TagLoader<Holder<T>> tagLoader) {
		((IConductanceTagLoader<T>) tagLoader).conductance$setRegistry(registry);
	}
}
