package conductance.lib.mixin;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import com.google.gson.JsonElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import conductance.core.runtimepack.server.RuntimeDataPackBridge;

@Mixin(value = RecipeManager.class, priority = 250)
public class RecipeManagerMixin {

	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
	private void conductance$removeRecipes(final Map<ResourceLocation, JsonElement> recipeMap, final ResourceManager resourceManager, final ProfilerFiller profiler, final CallbackInfo ci) {
		RuntimeDataPackBridge.removeRecipes(recipeMap);
	}
}
