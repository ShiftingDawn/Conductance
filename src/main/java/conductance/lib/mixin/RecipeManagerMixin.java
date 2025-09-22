package conductance.lib.mixin;

import java.util.SortedMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.lib.pack.server.RuntimeDataPackBridge;

@Mixin(value = RecipeManager.class, priority = 250)
public class RecipeManagerMixin {

	@Shadow
	@Final
	private HolderLookup.Provider registries;

	@Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;", at = @At(value = "INVOKE", target = "Ljava/util/SortedMap;forEach(Ljava/util/function/BiConsumer;)V"))
	private void conductance$removeRecipes(final ResourceManager p_379845_, final ProfilerFiller p_380058_, final CallbackInfoReturnable<RecipeMap> cir, @Local final SortedMap<ResourceLocation, Recipe<?>> sortedmap) {
		RuntimeDataPackBridge.insertRecipes(this.registries, sortedmap);
		RuntimeDataPackBridge.removeRecipes(sortedmap);
	}
}
