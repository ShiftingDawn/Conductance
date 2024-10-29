package conductance.core.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.core.apiimpl.ApiBridge;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

	@Inject(method = "loadResources", at = @At("HEAD"))
	private static void conductance$unfreezeRegistries(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> registries, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, int functionCompilationLevel, Executor backgroundExecutor, Executor gameExecutor, CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {
		ApiBridge.handleDataPackRegistryStage(ApiBridge.DataPackRegistryLoadStage.UNFREEZE);
		ApiBridge.handleDataPackRegistryStage(ApiBridge.DataPackRegistryLoadStage.RESET);
	}

	@Inject(method = "loadResources", at = @At("RETURN"), cancellable = true)
	private static void conductance$freezeRegistries(ResourceManager resourceManager, LayeredRegistryAccess<RegistryLayer> registries, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, int functionCompilationLevel, Executor backgroundExecutor, Executor gameExecutor, CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {
		cir.setReturnValue(cir.getReturnValue().thenApply(o -> {
			ApiBridge.handleDataPackRegistryStage(ApiBridge.DataPackRegistryLoadStage.REFREEZE);
			return o;
		}));
	}
}
