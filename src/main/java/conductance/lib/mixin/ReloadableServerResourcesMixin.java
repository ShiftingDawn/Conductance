package conductance.lib.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.lib.pack.server.RuntimeDataPackBridge;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {

	@Inject(method = "loadResources", at = @At("HEAD"))
	private static void conductance$loadResources(
			final ResourceManager resourceManager, final LayeredRegistryAccess<RegistryLayer> registryAccess, final List<Registry.PendingTags<?>> postponedTags, final FeatureFlagSet enabledFeatures,
			final Commands.CommandSelection commandSelection, final int functionCompilationLevel, final Executor backgroundExecutor, final Executor gameExecutor,
			final CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir
	) {
		RuntimeDataPackBridge.reload();
	}
}
