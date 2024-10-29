package conductance.core.mixin;

import conductance.core.apiimpl.ApiBridge;
import conductance.core.datapack.RuntimeDataPackBridge;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

	@Inject(method = "loadResources", at = @At("HEAD"))
	private static void conductance$unfreezeRegistries(final ResourceManager resourceManager, final LayeredRegistryAccess<RegistryLayer> registries, final FeatureFlagSet enabledFeatures,
			final Commands.CommandSelection commandSelection, final int functionCompilationLevel, final Executor backgroundExecutor, final Executor gameExecutor,
			final CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {
		ApiBridge.handleDataPackRegistryStage(ApiBridge.DataPackRegistryLoadStage.UNFREEZE);
		ApiBridge.handleDataPackRegistryStage(ApiBridge.DataPackRegistryLoadStage.RESET);

		RuntimeDataPackBridge.reload(registries.compositeAccess());
	}

	@Inject(method = "loadResources", at = @At("RETURN"), cancellable = true)
	private static void conductance$freezeRegistries(final ResourceManager resourceManager, final LayeredRegistryAccess<RegistryLayer> registries, final FeatureFlagSet enabledFeatures,
			final Commands.CommandSelection commandSelection, final int functionCompilationLevel, final Executor backgroundExecutor, final Executor gameExecutor,
			final CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir) {
		cir.setReturnValue(cir.getReturnValue().thenApply(o -> {
			ApiBridge.handleDataPackRegistryStage(ApiBridge.DataPackRegistryLoadStage.REFREEZE);
			return o;
		}));
	}
}
